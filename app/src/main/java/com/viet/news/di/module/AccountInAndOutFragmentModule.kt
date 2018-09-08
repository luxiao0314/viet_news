package com.viet.news.di.module

import android.support.v4.app.FragmentManager
import com.viet.mine.fragment.AccountInAndOutFragment
import com.viet.news.core.di.FragmentScope
import dagger.Module
import dagger.Provides

/**
 * @Description
 * @author null
 * @date 2018/9/8
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Module
class AccountInAndOutFragmentModule {
    @FragmentScope
    @Provides
    internal fun providesAccountInAndOutFragment(fragment: AccountInAndOutFragment): FragmentManager? = fragment.childFragmentManager
}