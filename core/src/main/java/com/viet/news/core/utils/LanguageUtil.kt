package com.viet.news.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.text.TextUtils
import com.chenenyu.router.Router
import com.viet.news.core.BaseApplication
import com.viet.news.core.config.Config
import java.util.*


/**
 * @author shuqing
 * @email shuqing.li@magicwindow.cn
 * @date 2018年07月09日15:48:32
 *
 * 切换语言调用方式：
 *          //position为语言列表index，对应常亮 AUTO、CHINESE、ENGLISH 越南语待添加
 *   if (LanguageUtil.needChange(position)) {
 *          LanguageUtil.saveSelectLanguage(this@SelectLanguageActivity, position)
 *          LanguageUtil.routToMain(this@SelectLanguageActivity)
 *      } else {
 *          //当不需要切换语言时，判断是否是【跟随】 与【系统语言】之间的切换
 *          val localIndex = SPHelper.create().getInt(Config.SELECTED_LANGUAGE)
 *          if ((localIndex == 0 || position == 0) && localIndex != position) {
 *              SPHelper.create().putInt(Config.SELECTED_LANGUAGE, position)
 *          }
 *      finishWithAnim()
 *      }
 */
object LanguageUtil {
    private const val AUTO = 0
    private const val CHINESE = 1
    private const val ENGLISH = 2
    /**
     * 用于设置Http的header language设置
     * 获取系统当前语言，中文则返回中文，非中文则返回英文
     * @return 例如 zh_cn,en_us ...
     */
    fun getHttpLanguageHeader() = combineHeader(getSelectedLocale())


    /**
     * 无动画模式，打开MainActivity
     * 有可能会清空栈，如果不是语言切换，一般不需要使用这个方式!!!
     */
    fun routToMainForce(activity: Activity? ) = Router.build(Config.ROUTER_MAIN_ACTIVITY)
            .anim(0, 0)//不使用跳转动画
            .with(Config.LANGUAGE_CHANGED, true)
            .also {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                    //android 28 需要重新初始化界面，否则无法切换语言
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            .go(activity)

    /**
     * 判断是否需要切换语言
     * @param select 语言列表中的索引
     * @return true 选择的语言与App语言不一致，需要切换
     */
    fun needChange(select: Int) = getAppLocale() != getLocalByIndex(select)

    /**
     * 根据索引保存用户选择的语言环境
     * @param select 语言列表中的索引
     */
    fun saveSelectLanguage(context: Context, select: Int) {
        SPHelper.create().putInt(Config.SELECTED_LANGUAGE, select)
        setApplicationLanguage(context)
    }

    /**
     * 从缓存中获取用户选择的语言环境，设置到当前Context中
     * 若用户选择跟随系统，则使用当前系统的语言环境
     */
    fun setLocal(context: Context): Context {
        return updateResources(context, getSelectedLocale(context))
    }

    /**
     * 设置application的语言类型
     * 有些资源是通过applicationContext获取的，在切换语言环境后需要更新applicationContext的配置
     */
    fun setApplicationLanguage(context: Context) {
        val resources = context.applicationContext.resources
        val dm = resources.displayMetrics
        val config = resources.configuration
        val locale = getSelectedLocale(context)
        config.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            config.locales = localeList
            context.applicationContext.createConfigurationContext(config)
            Locale.setDefault(locale)
        }
        resources.updateConfiguration(config, dm)
    }

    /**
     * 用于在Application和Activity的onConfigurationChanged回调中设置选择的语言
     * 在系统语言切换后，未销毁的界面调用，例如MainActivity和App
     */
    fun onConfigurationChanged(context: Context) {
        setLocal(context)
        setApplicationLanguage(context)
    }

    /**
     * 检查当前Activity是否需要根据缓存设置 切换到选择的语言
     * 若App进入后台，此时系统语言被修改，再次进入App需要在onCreate方法中调用此方法
     * @description 在打开app状态下系统语言被修改，再次进入app则切换到相应语言后跳转到MainActivity
     */
    fun checkLocalLanguage(activity: Activity) {
        //获取本机语言
        val locale = getSystemLocale() ?: return
        //获取缓存中的本地语言
        val cacheStr = SPHelper.create().getString(Config.LAST_LANGUAGE)
        //生成本地语言组合header 用于存入SP
        val localeStr = combineHeader(locale)
        //若缓存为空则将当前语言存入缓存
        if (TextUtils.isEmpty(cacheStr)) {
            SPHelper.create().putString(Config.LAST_LANGUAGE, localeStr)
        } else {
            //若缓存非空，则对比当前和缓存是否相同
            if (cacheStr != localeStr) {
                //不同则将当前存入缓存后重启界面
                SPHelper.create().putString(Config.LAST_LANGUAGE, localeStr)
                routToMainForce(activity)
            } else {
                //缓存与本机相同。
                // 若为跟随系统，则检查App语言和系统语言是否一致，若不一致 则重新进
                if (getSelectedLanguage(BaseApplication.instance) == AUTO) {
                    val appLocaleStr = combineHeader(getAppLocale())
                    if (appLocaleStr != localeStr) {
                        routToMainForce(activity)
                    }
                }
            }
        }
    }

    /**
     * 获取用户选择的语言环境
     * 若为跟随系统，则返回当前系统的语言环境
     */
    fun getSelectedLocale(context: Context = BaseApplication.instance): Locale {
        val selectedLanguage = getSelectedLanguage(context)
        return getLocalByIndex(selectedLanguage)
    }

    /**
     * 获取选择的语言模式
     * 例如 auto=0，Chinese=1，English=2
     */
    fun getSelectedLanguage(context: Context): Int {
        return SPHelper.create(context).getInt(Config.SELECTED_LANGUAGE, AUTO)
    }

    /**
     * 根据索引从语言列表中取得Locale对象
     * 若为跟随系统，则返回系统当前的语言环境
     */
    private fun getLocalByIndex(selectedLanguage: Int): Locale {
        return when (selectedLanguage) {
            CHINESE -> Locale.CHINA
            ENGLISH -> Locale.US
            else -> getSystemLocale()
        }
    }

    /**
     * 组合语言和国家信息 zh_cn
     * @param locale Locale
     * @return String 只能是zh_CN而不能是zh_CN_#Hans，所以不能用locale.toString()
     */
//    private fun combineHeader(locale: Locale): String = locale.toString()
    private fun combineHeader(locale: Locale): String = locale.language + "_" + locale.country

    /*
     * 获取App的当前语言
     * 不一定是手机系统的当前语言
     */
    private fun getAppLocale() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        BaseApplication.instance.resources.configuration.locales[0]
    } else {
        BaseApplication.instance.resources.configuration.locale
    }

    /*
    * 获取当前手机的语言环境，
    * 在8.0系统上 Locale.getDefault()会返回当前App的语言而不是系统的 因此使用Resources的方式获取
    */
    private fun getSystemLocale() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Resources.getSystem().configuration.locales[0]
    } else {
        Resources.getSystem().configuration.locale
    }

    /*
     * 将给定的语言环境设置到当前Context中
     */
    private fun updateResources(context: Context, locale: Locale): Context {
        val res = context.resources
        val config = res.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
            context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
            context
        }
    }
}
