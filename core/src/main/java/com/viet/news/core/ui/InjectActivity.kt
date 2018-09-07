package com.viet.news.core.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import com.jaeger.library.StatusBarUtil
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * @Description
 * 使用注入的activity需要继承此activity,或者如下在activity中加入AndroidInjection.inject(this)
 * 如果activity中fragment使用到activity里面注入的实例,此需要实现HasSupportFragmentInjector
 * @Author Sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 23/04/2018 4:39 PM
 * @Version 1.0.0
 */
open class InjectActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    @JvmField
    var supportFragmentInjector: DispatchingAndroidInjector<Fragment>? = null

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? = supportFragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        //一处声明，处处依赖注入，before calling super.onCreate()
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}