package com.library.aaron.rac.di

import com.library.aaron.core.di.ActivityScope
import com.library.aaron.rac.ui.MainActivity
import com.library.aaron.rac.ui.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {
    @ActivityScope
    @ContributesAndroidInjector()
    internal abstract fun bindSplashModule(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector()
    internal abstract fun bindMainModule(): MainActivity
}
