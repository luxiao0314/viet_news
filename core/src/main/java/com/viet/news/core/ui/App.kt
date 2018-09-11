package com.viet.news.core.ui

import android.content.Context
import android.support.multidex.MultiDex
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.viet.news.core.BaseApplication
import com.viet.news.core.R
import com.viet.news.core.utils.LanguageUtil
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
        initRouter()
    }

    private fun initRouter() {
//        Router.initialize(Configuration.Builder()
//                // 调试模式，开启后会打印log
//                .setDebuggable(BuildConfig.DEBUG)
//                // 模块名，每个使用Router的module都要在这里注册
//                .registerModules("app", "follow", "task", "mine")
//                .build())
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