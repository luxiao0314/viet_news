package com.library.aaron.core


import android.app.Activity
import android.support.multidex.MultiDexApplication
import com.library.aaron.core.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject
import kotlin.properties.Delegates

class App : MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        instance = this
        //        DaggerAppComponent
        //				.builder()
        //				.application(this)
        //				.build()
        //				.inject(this);
        AppInjector.init(this)

    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }

    companion object {

        //方式1.通过标准代理实现late init
        var instance: App by Delegates.notNull()
            private set
    }

}
