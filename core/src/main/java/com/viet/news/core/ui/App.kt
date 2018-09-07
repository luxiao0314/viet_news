package com.viet.news.core.ui

import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.viet.news.core.BaseApplication
import com.viet.news.core.R
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
    }

    companion object {
        //方式1.通过标准代理实现late init
        var instance: BaseApplication by Delegates.notNull()
            private set
    }

    init {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setHeaderHeight(40F)
            val header = ClassicsHeader(context).setEnableLastTime(false).setTextSizeTitle(14F)
            ClassicsHeader.REFRESH_HEADER_REFRESHING = getString(R.string.refreshing)
            header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setFooterHeight(40F)
            ClassicsFooter(context).setDrawableSize(20f)
        }
    }
}