package com.library.aaron.core.ui

import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.view.View

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 18/03/2018 22:12
 * @description
 */
interface LoadingInterface {

    fun setErrorLayoutReloadListener(layoutClickListener: View.OnClickListener)
    fun setEmptyLayoutReloadListener(layoutClickListener: View.OnClickListener)


    fun setLoadingButtonClickListener(loadingButtonClickListener: View.OnClickListener)


    fun setEmptyButtonClickListener(emptyButtonClickListener: View.OnClickListener)


    fun setErrorButtonClickListener(errorButtonClickListener: View.OnClickListener)


    fun setViewType(viewType: Int)

    fun showErrorWithMessage(@DrawableRes resId: Int, text: CharSequence)

    fun showErrorWithLayout(@LayoutRes resId: Int)

    fun showError()

    fun showEmptyWithMessage(@DrawableRes resId: Int, text: CharSequence)

    fun showEmpty()


    fun showLoading()

    fun showContent()

}