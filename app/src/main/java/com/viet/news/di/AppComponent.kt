package com.viet.news.di

import com.viet.news.core.di.AppScope
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@AppScope
@Component(modules = [
    BuildersModule::class,
    AndroidSupportInjectionModule::class])
interface AppComponent {
    fun inject(app: App)
}