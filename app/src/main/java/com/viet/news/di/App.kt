package com.viet.news.di

import android.app.Activity
import com.viet.news.core.BaseApplication
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * @Author Aaron
 * @Date 2018/5/7
 * @Email aaron@magicwindow.cn
 * @Description
 */
class App : BaseApplication(), HasActivityInjector {


    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        instance = this

        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)

        AppInjector.init(this)

    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }

    companion object {

        //方式1.通过标准代理实现late init
        var instance: BaseApplication by Delegates.notNull()
            private set
    }

}