package com.viet.news.di

import com.viet.follow.activity.FunsAndFollowActivity
import com.viet.follow.activity.PersonalPageActivity
import com.viet.follow.fragment.FunsAndFollowFragment
import com.viet.follow.fragment.NewsFragment
import com.viet.mine.activity.InviteFriendActivity
import com.viet.mine.activity.LoginActivity
import com.viet.mine.fragment.LoginFragment
import com.viet.news.core.di.ActivityScope
import com.viet.news.core.di.FragmentScope
import com.viet.news.di.module.*
import com.viet.news.ui.activity.MainActivity
import com.viet.news.ui.fragment.FindFragment
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

    @ActivityScope
    @ContributesAndroidInjector()
    internal abstract fun bindPersonalHomePageActivity(): PersonalPageActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [FunsAndFollowModule::class])
    internal abstract fun bindFunsAndFollowActivity(): FunsAndFollowActivity

    @ActivityScope
    @ContributesAndroidInjector()
    internal abstract fun bindInviteFriendActivity(): InviteFriendActivity

    @FragmentScope
    @ContributesAndroidInjector(modules = [LoginFragmentModule::class])
    internal abstract fun bindLoginFragment(): LoginFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FindModule::class])
    internal abstract fun bindFindFragment(): FindFragment

    @FragmentScope
    @ContributesAndroidInjector()
    internal abstract fun bindNewsFragment(): NewsFragment

    @FragmentScope
    @ContributesAndroidInjector()
    internal abstract fun bindFunsAndFollowFragment(): FunsAndFollowFragment
}
