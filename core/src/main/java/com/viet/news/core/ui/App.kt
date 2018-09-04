package com.viet.news.core.ui

import com.viet.news.core.BaseApplication
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
}