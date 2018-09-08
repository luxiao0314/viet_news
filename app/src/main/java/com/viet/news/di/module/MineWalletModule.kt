package com.viet.news.di.module

import android.support.v4.app.FragmentManager
import com.viet.mine.activity.MineWalletActivity
import com.viet.news.core.di.ActivityScope
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
class MineWalletModule {
    @ActivityScope
    @Provides
    internal fun providesMineWalletActivity(activity: MineWalletActivity): FragmentManager? = activity.supportFragmentManager
}