package com.viet.news.di.module

import android.support.v4.app.FragmentManager
import com.viet.mine.fragment.LoginFragment
import com.viet.news.core.di.FragmentScope
import dagger.Module
import dagger.Provides

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 3:58 PM
 * @Version
 */
@Module
class LoginFragmentModule {

    @FragmentScope
    @Provides
    internal fun providesLoginFragment(fragment: LoginFragment): FragmentManager? = fragment.childFragmentManager
}