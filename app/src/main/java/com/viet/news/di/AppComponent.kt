package com.viet.news.di

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class,
        //		ToolsModule.class,
        //		RepositoriesModule.class,
        BuildersModule::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}
