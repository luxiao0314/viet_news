package com.viet.news.di

import com.viet.mine.activity.LoginActivity
import com.viet.news.core.di.ActivityScope
import com.viet.news.core.di.FragmentScope
import com.viet.news.di.module.FindModule
import com.viet.news.di.module.LoginModule
import com.viet.news.di.module.MainModule
import com.viet.news.ui.activity.MainActivity
import com.viet.news.ui.fragment.FindFragment
import com.viet.news.ui.fragment.NewsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * 每个activity和fragment必须使用自己对应的module
 */
@Module
abstract class BuildersModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun bindMainModule(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [LoginModule::class])
    internal abstract fun bindLoginActivity(): LoginActivity

    @FragmentScope
    @ContributesAndroidInjector(modules = [FindModule::class])
    internal abstract fun bindFindFragment(): FindFragment

    @FragmentScope
    @ContributesAndroidInjector()
    internal abstract fun bindNewsFragment(): NewsFragment
}
