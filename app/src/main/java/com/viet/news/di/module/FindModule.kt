package com.viet.news.di.module

import android.support.v4.app.FragmentManager
import com.viet.news.core.di.FragmentScope
import com.viet.news.ui.fragment.FindFragment
import dagger.Module
import dagger.Provides

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 3:52 PM
 * @Version
 */
@Module
class FindModule {

    lateinit var fragment: FindFragment

    @FragmentScope
    @Provides
    internal fun providesFragmentManager(fragment: FindFragment): FragmentManager? = fragment.fragmentManager

//    @FragmentScope
//    @Provides
//    internal fun providesViewModel(fragment: FindFragment): FindViewModel = fragment.viewModelDelegate(FindViewModel::class).getValue(fragment,null)
}