package com.viet.news.di

import com.viet.follow.activity.FansAndFollowActivity
import com.viet.follow.activity.PersonalPageActivity
import com.viet.follow.fragment.FollowFragment
import com.viet.follow.fragment.FollowTabFragment
import com.viet.follow.fragment.FansTabFragment
import com.viet.follow.fragment.NewsFragment
import com.viet.mine.activity.*
import com.viet.mine.fragment.AccountInAndOutFragment
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
    internal abstract fun bindFunsAndFollowActivity(): FansAndFollowActivity

    @ActivityScope
    @ContributesAndroidInjector()
    internal abstract fun bindInviteFriendActivity(): InviteFriendActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MineWalletModule::class])
    internal abstract fun bindMineWalletActivity(): MineWalletActivity

    @ActivityScope
    @ContributesAndroidInjector()
    internal abstract fun bindSettingActivity(): SettingActivity

    @ActivityScope
    @ContributesAndroidInjector()
    internal abstract fun bindCollectionActivity(): CollectionActivity

    @ActivityScope
    @ContributesAndroidInjector()
    internal abstract fun bindAccountInfoActivity(): AccountInfoActivity

    @ActivityScope
    @ContributesAndroidInjector()
    internal abstract fun bindFindPwdActivity(): FindPwdActivity

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
    internal abstract fun bindFollowFragment(): FollowFragment

    @FragmentScope
    @ContributesAndroidInjector()
    internal abstract fun bindFollowTagFragment(): FollowTabFragment

     @FragmentScope
    @ContributesAndroidInjector()
    internal abstract fun bindFunsTagFragment(): FansTabFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [AccountInAndOutFragmentModule::class])
    internal abstract fun bindAccountInAndOutFragment(): AccountInAndOutFragment

}
