package com.viet.news.core.ui

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.IdRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.viet.news.core.ext.goFragment
import com.viet.news.core.ext.routerWithAnim
import io.reactivex.disposables.CompositeDisposable
import me.yokeyword.swipebackfragment.SwipeBackFragment

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 18/03/2018 22:13
 * @description
 */
abstract class BaseFragment : SwipeBackFragment() {

    protected lateinit var mContext: Context
    protected var compositeDisposable = CompositeDisposable()
    private var mContainerView: View? = null

    /**
     * Deprecated on API 23
     * @param activity
     */
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (Build.VERSION.SDK_INT < 23) {
            this.mContext = activity
        }
    }

    @TargetApi(23)
    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is Activity) {
//            this.mContext = context
//        }
        this.mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(getLayoutId(), container, false)
        return if (isSupportSwipeBack()) attachToSwipeBack(mContainerView) else mContainerView
    }

    abstract fun isSupportSwipeBack(): Boolean

    abstract fun getLayoutId(): Int

    protected abstract fun initView(view: View)

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear() // 防止内存泄露
    }

    protected fun openPage(context: BaseFragment, path: String, @IdRes id: Int, addToBackStack: Boolean = true) {
        routerWithAnim(path).goFragment(context, id, addToBackStack)
    }


}