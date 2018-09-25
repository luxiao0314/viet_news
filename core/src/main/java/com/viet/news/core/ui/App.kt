package com.viet.news.core.ui

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.multidex.MultiDex
import com.safframework.log.L
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.viet.news.core.BaseApplication
import com.viet.news.core.R
import com.viet.news.core.utils.LanguageUtil
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlin.properties.Delegates


/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 04/09/2018 5:12 PM
 * @Version
 */
open class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        LanguageUtil.setApplicationLanguage(this)
        initConfig()
    }

    /**
     * 初始化一些配置
     * 每一个配置的初始化单独使用一个线程来处理
     */
    private fun initConfig() {
        val initLogObservable = Observable.create<Any> { L.header(getHeader()) }.subscribeOn(Schedulers.newThread()) // 初始化日志框架的Header
        Observable.mergeArray(initLogObservable).subscribe()
    }

    private fun getHeader(): String {
        val sb = StringBuilder().append("(appName:" + getString(R.string.app_name))
        val manager = this.packageManager
        try {
            val info = manager.getPackageInfo(this.packageName, 0)
            if (info != null) sb.append(" app version:" + info.versionName)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return sb.append(" model:" + Build.MODEL)
                .append(" osVersion:" + Build.VERSION.RELEASE)
                .append(")").toString()
    }


    companion object {
        //方式1.通过标准代理实现late init
        var instance: BaseApplication by Delegates.notNull()
            private set
    }

    init {
        initRefreshLayout()
    }

    //------------切换语言 start--------------------------------------
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LanguageUtil.setLocal(base))
        MultiDex.install(this)
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        //保存系统选择语言
        LanguageUtil.onConfigurationChanged(applicationContext)
        initRefreshLayout()
    }
    //------------切换语言 end--------------------------------------


    private fun initRefreshLayout() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setHeaderHeight(40F)
            layout.setEnableAutoLoadMore(true)
            layout.setEnableFooterFollowWhenLoadFinished(true)
            ClassicsHeader.REFRESH_HEADER_REFRESHING = getString(R.string.refreshing)
            ClassicsHeader(context).setEnableLastTime(false).setTextSizeTitle(14F).setSpinnerStyle(SpinnerStyle.Translate)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setFooterHeight(40F)
            ClassicsFooter(context).setDrawableSize(20f).setTextSizeTitle(14F)
        }
    }
}