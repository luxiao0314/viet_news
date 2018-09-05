package com.viet.news.di.module

import android.support.v4.app.FragmentManager
import com.viet.mine.activity.LoginActivity
import com.viet.news.core.di.ActivityScope
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
class LoginModule {

    @ActivityScope
    @Provides
    internal fun providesLoginActivity(activity: LoginActivity): FragmentManager? = activity.supportFragmentManager
}