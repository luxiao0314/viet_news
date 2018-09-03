package com.viet.news.core.ui

import android.content.Context
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * @Description
 * 使用注入的fragment需要继承此fragment,或者如下在fragment中加入AndroidSupportInjection.inject(this)
 * 如果fragment使用到activity里面注入的实例,该activity需要实现HasSupportFragmentInjector
 * @Author Sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 23/04/2018 4:41 PM
 * @Version 1.0.0
 */
abstract class InjectFragment : BaseFragment(), HasSupportFragmentInjector {

    @Inject
    @JvmField
    var childFragmentInjector: DispatchingAndroidInjector<Fragment>? = null

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return childFragmentInjector
    }

    override fun onAttach(context: Context) {
        //使用的Fragment 是V4 包中的，不然就是AndroidInjection.inject(this)
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}