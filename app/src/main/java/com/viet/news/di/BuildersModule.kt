package com.viet.news.di

import com.viet.news.core.di.ActivityScope
import com.viet.news.ui.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

//    @ActivityScope
//    @ContributesAndroidInjector()
//    internal abstract fun bindSplashModule(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector()
    internal abstract fun bindMainModule(): MainActivity
}
