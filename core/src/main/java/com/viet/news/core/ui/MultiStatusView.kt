package com.viet.news.core.ui

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.RelativeLayout
import android.widget.TextView
import com.viet.news.core.R
import java.util.*

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 18/03/2018 22:03
 * @description
 */
class MultiStatusView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr), LoadingInterface {


    //    private List<View> childViews;
    private val mOtherIds = ArrayList<Int>()
    private val defaultLayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT)
    private var mLoadingView: ViewGroup? = null
    private var mEmptyView: ViewGroup? = null
    private var mErrorView: ViewGroup? = null
    private var mEmptyLayoutResId: Int = 0
    private var mErrorLayoutResId: Int = 0
    private var mLoadingLayoutResId: Int = 0
    private val mInflater: LayoutInflater by lazy {
        LayoutInflater.from(context)
    }
    private var mErrorMessage: CharSequence? = null
    private var mEmptyMessage: CharSequence? = null
    private var mErrorDrawable = R.drawable.ic_error
    private var mEmptyDrawable = R.drawable.ic_wait
    // ---------------------------
    // static variables
    // ---------------------------
    private var mLoadingButtonClickListener: View.OnClickListener? = null
    private var mEmptyButtonClickListener: View.OnClickListener? = null
    private var mErrorButtonClickListener: View.OnClickListener? = null
    // ---------------------------
    // default values
    // ---------------------------
    private var mErrorLayoutReloadListener: View.OnClickListener? = null
    private var mEmptyLayoutReloadListener: View.OnClickListener? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MultiStatusView, defStyleAttr, 0)
        mEmptyLayoutResId = a.getResourceId(R.styleable.MultiStatusView_emptyView, R.layout.view_empty)
        mErrorLayoutResId = a.getResourceId(R.styleable.MultiStatusView_errorView, R.layout.view_error)
        mLoadingLayoutResId = a.getResourceId(R.styleable.MultiStatusView_loadingView, R.layout.view_loading)
        a.recycle()
    }

    /**
     * 展示错误信息
     *
     * @param resId 图片资源id，如果不想重置请赋值为0或者-1
     * @param text
     */
    @SuppressLint("ResourceType")
    override fun showErrorWithMessage(@DrawableRes resId: Int, text: CharSequence) {
        if (resId > 0) {
            this.mErrorDrawable = resId
        }
        if (!TextUtils.isEmpty(text)) {
            this.mErrorMessage = text
        }
        showError()
    }

    /**
     * 重写ErrorView的Layout
     *
     * @param resId layout资源id
     */
    @SuppressLint("ResourceType")
    override fun showErrorWithLayout(@LayoutRes resId: Int) {
        if (resId > 0) {
            this.mErrorLayoutResId = resId
        }
        showError()
    }

    /**
     * 展示默认ErrorView
     */
    override fun showError() {
        switchEmptyType(TYPE_ERROR)
    }

    /**
     * 展示EmptyView
     *
     * @param resId 图片资源id，如果不想重置请赋值为0或者-1
     * @param text
     */
    @SuppressLint("ResourceType")
    override fun showEmptyWithMessage(@DrawableRes resId: Int, text: CharSequence) {
        if (resId > 0) {
            this.mEmptyDrawable = resId
        }
        if (!TextUtils.isEmpty(text)) {
            this.mEmptyMessage = text
        }
        showEmpty()
    }

    /**
     * 展示默认EmptyView
     */
    override fun showEmpty() {
        switchEmptyType(TYPE_EMPTY)
    }

    /**
     * 展示默认LoadingView
     */
    override fun showLoading() {
        switchEmptyType(TYPE_LOADING)
    }


    /**
     * 展示内容
     */
    override fun showContent() {
        switchEmptyType(TYPE_CONTENT)
    }

    /**
     * 根据type显示内容
     * @param viewType
     */
    override fun setViewType(viewType: Int) {
        switchEmptyType(viewType)
    }

    /**
     * 为ErrorView设置OnClickListener
     *
     * @param layoutReloadClickListener OnClickListener Object
     */
    override fun setErrorLayoutReloadListener(layoutReloadClickListener: View.OnClickListener) {
        this.mErrorLayoutReloadListener = layoutReloadClickListener
    }

    /**
     * 为EmptyView设置OnClickListener
     *
     * @param layoutReloadClickListener OnClickListener Object
     */
    override fun setEmptyLayoutReloadListener(layoutReloadClickListener: View.OnClickListener) {
        this.mEmptyLayoutReloadListener = layoutReloadClickListener
    }

    /**
     * 为LoadingView设置OnClickListener
     *
     * @param loadingButtonClickListener OnClickListener Object
     */
    override fun setLoadingButtonClickListener(loadingButtonClickListener: View.OnClickListener) {
        this.mLoadingButtonClickListener = loadingButtonClickListener
    }

    /**
     * Sets the OnClickListener to EmptyView
     *
     * @param emptyButtonClickListener OnClickListener Object
     */
    override fun setEmptyButtonClickListener(emptyButtonClickListener: View.OnClickListener) {
        this.mEmptyButtonClickListener = emptyButtonClickListener
    }

    /**
     * Sets the OnClickListener to ErrorView
     *
     * @param errorButtonClickListener OnClickListener Object
     */
    override fun setErrorButtonClickListener(errorButtonClickListener: View.OnClickListener) {
        this.mErrorButtonClickListener = errorButtonClickListener
    }


    // ---------------------------
    // getters and setters
    // ---------------------------

    private fun switchEmptyType(type: Int) {

        var loadingAnimationView: View? = null
        if (mLoadingView != null) {
            loadingAnimationView = mLoadingView!!.findViewById(R.id.img_loading)
        }

        when (type) {
            TYPE_EMPTY -> {
                initEmptyValues()
                Log.i(TAG, "type: TYPE_EMPTY")
                if (loadingAnimationView != null && loadingAnimationView.animation != null) {
                    loadingAnimationView.animation.cancel()
                }

                if (!TextUtils.isEmpty(mEmptyMessage)) {
                    val emptyMsg = mEmptyView!!.findViewById<TextView>(R.id.text_empty_msg)
                    emptyMsg.text = mEmptyMessage
                    setTopDrawables(emptyMsg, mEmptyDrawable)
                }

                showViewById(mEmptyView!!.id)
            }
            TYPE_ERROR -> {
                initErrorValues()
                Log.i(TAG, "type: TYPE_ERROR")

                if (!TextUtils.isEmpty(mErrorMessage)) {
                    val errorMsg = mErrorView!!.findViewById<TextView>(R.id.text_error_msg)
                    errorMsg.text = mErrorMessage
                    setTopDrawables(errorMsg, mErrorDrawable)
                }

                if (loadingAnimationView != null && loadingAnimationView.animation != null) {
                    loadingAnimationView.animation.cancel()
                }

                showViewById(mErrorView!!.id)
            }
            TYPE_LOADING -> {
                Log.i(TAG, "type: TYPE_LOADING")
                initLoadingValues()
                if (loadingAnimationView != null) {
                    loadingAnimationView.startAnimation(rotateAnimation)
                }

                showViewById(mLoadingView!!.id)
            }
            else -> {
                Log.i(TAG, "type: TYPE_CONTENT")
                if (loadingAnimationView != null && loadingAnimationView.animation != null) {
                    loadingAnimationView.animation.cancel()
                }
                showChildView()
            }
        }
    }

    private fun showViewById(viewId: Int) {
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.visibility = if (view.id == viewId) View.VISIBLE else View.GONE
        }
    }

    private fun showChildView() {

        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.visibility = if (mOtherIds.contains(view.id)) View.GONE else View.VISIBLE
        }
    }


    private fun setTopDrawables(textView: TextView, resId: Int) {
        val drawable = resources.getDrawable(resId)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)//必须设置图片大小，否则不显示
        textView.setCompoundDrawables(null, drawable, null, null)
    }

    private fun initErrorValues() {

        if (mErrorView == null) {
            mErrorView = mInflater.inflate(mErrorLayoutResId, null) as ViewGroup
            if (mErrorLayoutReloadListener != null) {
                mErrorView!!.setOnClickListener(mErrorLayoutReloadListener)
            }
            if (mErrorButtonClickListener != null) {
                val errorViewButton = mErrorView?.findViewById<View>(R.id.btn_error)
                errorViewButton?.setOnClickListener(mErrorButtonClickListener)
                errorViewButton?.visibility = View.VISIBLE
            } else {
                val errorViewButton = mErrorView?.findViewById<View>(R.id.btn_error)
                errorViewButton?.visibility = View.GONE
            }

            addView(mErrorView, 0, defaultLayoutParams)
            mOtherIds.add(mErrorView!!.id)
        }
    }

    private fun initEmptyValues() {

        if (mEmptyView == null) {
            mEmptyView = mInflater.inflate(mEmptyLayoutResId, null) as ViewGroup
            if (mEmptyLayoutReloadListener != null) {
                mEmptyView!!.setOnClickListener(mEmptyLayoutReloadListener)
            }
            if (mEmptyButtonClickListener != null) {
                val emptyViewButton = mEmptyView?.findViewById<View>(R.id.btn_empty)
                emptyViewButton?.setOnClickListener(mEmptyButtonClickListener)
                emptyViewButton?.visibility = View.VISIBLE
            } else {
                val emptyViewButton = mEmptyView?.findViewById<View>(R.id.btn_empty)
                emptyViewButton?.visibility = View.GONE
            }

            addView(mEmptyView, 0, defaultLayoutParams)
            mOtherIds.add(mEmptyView!!.id)
        }

    }

    private fun initLoadingValues() {


        if (mLoadingView == null) {
            mLoadingView = mInflater.inflate(mLoadingLayoutResId, null) as ViewGroup
            if (mLoadingButtonClickListener != null) {
                val loadingViewButton = mLoadingView?.findViewById<View>(R.id.btn_loading)
                loadingViewButton?.setOnClickListener(mLoadingButtonClickListener)
                loadingViewButton?.visibility = View.VISIBLE
            } else {
                val loadingViewButton = mLoadingView?.findViewById<View>(R.id.btn_loading)
                loadingViewButton?.visibility = View.GONE
            }
            addView(mLoadingView, 0, defaultLayoutParams)
            mOtherIds.add(mLoadingView!!.id)
        }

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clear(mEmptyView, mLoadingView, mErrorView)
        if (null != mEmptyButtonClickListener) {
            mEmptyButtonClickListener = null
        }
        if (null != mErrorButtonClickListener) {
            mErrorButtonClickListener = null
        }
        if (null != mErrorLayoutReloadListener) {
            mErrorLayoutReloadListener = null
        }
        if (null != mEmptyLayoutReloadListener) {
            mEmptyLayoutReloadListener = null
        }
        if (null != mLoadingButtonClickListener) {
            mLoadingButtonClickListener = null
        }
    }


    private fun clear(vararg views: View?) {
        if (null == views) {
            return
        }
        try {
            for (view in views) {
                if (null != view) {
                    removeView(view)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {

        private const val TAG = "MultiStatusView"
        /**
         * The empty state
         */
        const val TYPE_EMPTY = 1
        /**
         * The loading state
         */
        const val TYPE_LOADING = 2
        /**
         * The error state
         */
        const val TYPE_ERROR = 3

        const val TYPE_CONTENT = 4

        private val rotateAnimation: Animation
            get() {
                val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f)
                rotateAnimation.duration = 1500
                rotateAnimation.interpolator = LinearInterpolator()
                rotateAnimation.repeatCount = Animation.INFINITE
                return rotateAnimation
            }
    }
}