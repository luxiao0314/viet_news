package com.viet.news.core.delegate

import android.arch.lifecycle.ViewModelProviders
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.viewmodel.BaseViewModel
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 *
 * @FileName:
 *          cn.magicwindow.core.delegate.ViewModelDelegate.kt
 * @author: Tony Shen
 * @date: 2018-07-16 20:18
 * @version V1.0 <描述当前版本功能>
 */
class ViewModelDelegate<out T : BaseViewModel>(private val clazz: KClass<T>, private val fromActivity: Boolean) {

    private var viewModel: T? = null

    operator fun getValue(thisRef: BaseActivity, property: KProperty<*>) = buildViewModel(activity = thisRef)

    operator fun getValue(thisRef: BaseFragment, property: KProperty<*>) = if (fromActivity)
        buildViewModel(activity = thisRef.activity as? BaseActivity
                ?: throw IllegalStateException("Activity must be as BaseActivity"))
    else buildViewModel(fragment = thisRef)

    private fun buildViewModel(activity: BaseActivity? = null, fragment: BaseFragment? = null): T {
        if (viewModel != null) return viewModel!!

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(clazz.java)
        } ?: fragment?.let {
            viewModel = ViewModelProviders.of(it).get(clazz.java)
        } ?: throw IllegalStateException("Activity and Fragment null! =(")

        return viewModel!!
    }
}

fun <T : BaseViewModel> BaseActivity.viewModelDelegate(clazz: KClass<T>) = ViewModelDelegate(clazz, true)

// fromActivity默认为true，viewModel生命周期默认跟activity相同 by aaron 2018/7/24
fun <T : BaseViewModel> BaseFragment.viewModelDelegate(clazz: KClass<T>, fromActivity: Boolean = true) = ViewModelDelegate(clazz, fromActivity)