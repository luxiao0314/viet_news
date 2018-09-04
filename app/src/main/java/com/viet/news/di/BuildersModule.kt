package com.viet.news.di

import com.viet.news.core.di.ActivityScope
import com.viet.news.core.di.FragmentScope
import com.viet.news.di.module.FindModule
import com.viet.news.di.module.MainModule
import com.viet.news.ui.activity.MainActivity
import com.viet.news.ui.fragment.FindFragment
import com.viet.news.ui.fragment.NewsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun bindMainModule(): MainActivity

    @FragmentScope
    @ContributesAndroidInjector(modules = [FindModule::class])
    internal abstract fun bindFindFragment(): FindFragment

    @FragmentScope
    @ContributesAndroidInjector()
    internal abstract fun bindNewsFragment(): NewsFragment
}
