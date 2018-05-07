package com.library.aaron.core


import android.support.multidex.MultiDexApplication
import kotlin.properties.Delegates

open class BaseApplication : MultiDexApplication(){


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
