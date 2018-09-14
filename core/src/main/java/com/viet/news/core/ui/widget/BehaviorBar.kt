package com.viet.news.core.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.annotation.IdRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatCheckedTextView
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.safframework.ext.clickWithTrigger
import com.viet.news.core.R
import kotlinx.android.synthetic.main.behaviorbar.view.*

/**
 * @author null
 * 社交互动行为工具栏
 */
class BehaviorBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    private var hasRead: Boolean = false
    private var hasFavorite: Boolean = false
    private var hasLike: Boolean = false
    private var readNum: Int = 0
    private var favoriteNum: Int = 0
    private var likeNum: Int = 0

    private var mDelegate: Delegate? = null

    init {
        View.inflate(context, R.layout.behaviorbar, this)
        initAttrs(context, attrs)
        setListener()
    }


    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BehaviorBar)
        val count = typedArray.indexCount
        for (i in 0 until count) {
            initAttr(typedArray.getIndex(i), typedArray)
        }
        typedArray.recycle()
    }

    /**
     * 初始化属性值
     */
    @SuppressLint("ResourceType")
    private fun initAttr(index: Int, typedArray: TypedArray) {
        when (index) {
            R.styleable.BehaviorBar_read_number -> {
                readNum = typedArray.getInt(index, 0)
                tv_read_num.text = readNum.toString()
            }
            R.styleable.BehaviorBar_favorite_number -> {
                favoriteNum = typedArray.getInt(index, 0)
                tv_favorite_num.text = favoriteNum.toString()
            }
            R.styleable.BehaviorBar_like_number -> {
                likeNum = typedArray.getInt(index, 0)
                tv_like_num.text = likeNum.toString()
            }
            R.styleable.BehaviorBar_has_read -> {
                hasRead = typedArray.getBoolean(index, false)
                if (hasRead) {
                    setStatus(tv_read_num, R.drawable.ic_coin_enable, R.color.behavior_enable)
                } else {
                    setStatus(tv_read_num, R.drawable.ic_coin, R.color.behavior_normal)
                }
            }
            R.styleable.BehaviorBar_has_favorite -> {
                hasFavorite = typedArray.getBoolean(index, false)
                if (hasFavorite) {
                    setStatus(tv_favorite_num, R.drawable.ic_favorite_enable, R.color.behavior_enable)
                } else {
                    setStatus(tv_favorite_num, R.drawable.ic_favorite, R.color.behavior_normal)
                }
            }
            R.styleable.BehaviorBar_has_like -> {
                hasLike = typedArray.getBoolean(index, false)
                if (hasLike) {
                    setStatus(tv_like_num, R.drawable.ic_like_enable, R.color.behavior_enable)
                } else {
                    setStatus(tv_like_num, R.drawable.ic_like, R.color.behavior_normal)
                }
            }
        }
    }

    /**
     * 设置监听
     */
    @SuppressLint("ResourceType")
    private fun setListener() {
        tv_favorite_num.clickWithTrigger {
            mDelegate?.onFavoriteClick()
        }
        tv_like_num.clickWithTrigger {
            mDelegate?.onLikeClick()
        }
    }

    /**
     * 设置状态
     * @param view 控件
     * @param status 状态
     * @param count 数量
     */
    @SuppressLint("ResourceType")
    private fun setStatus(view: AppCompatCheckedTextView, @IdRes id: Int, @IdRes color: Int) {
        view.setCompoundDrawables(getDrawable(id), null, null, null)
        view.setTextColor(ContextCompat.getColor(context, color))
    }

    /**
     * 重新设置图标
     * @param id 资源id
     */
    @SuppressLint("ResourceType")
    private fun getDrawable(@IdRes id: Int): Drawable? {
        val drawable = ContextCompat.getDrawable(context, id)
        drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        return drawable
    }

    private fun setDelegate(delegate: ClickDelegate): BehaviorBar {
        mDelegate = delegate
        return this
    }

    /**
     * 设置喜欢初始状态
     * @param status 状态
     * @param count 数量
     */
    @SuppressLint("ResourceType")
    fun setReadNumStatus(count: Int): BehaviorBar {
        tv_read_num.text = count.toString()
        hasRead = count != 0
        readNum = count
        if (hasRead) {
            setStatus(tv_read_num, R.drawable.ic_coin_enable, R.color.behavior_enable)
        } else {
            setStatus(tv_read_num, R.drawable.ic_coin, R.color.behavior_normal)
        }
        tv_read_num.text = readNum.toString()
        return this
    }

    /**
     * 设置收藏初始状态
     */
    @SuppressLint("ResourceType")
    fun setFavoriteStatus(status: Boolean, count: Int): BehaviorBar {
        tv_favorite_num.text = count.toString()
        hasFavorite = status
        favoriteNum = count
        if (status) {
            setStatus(tv_favorite_num, R.drawable.ic_favorite_enable, R.color.behavior_enable)
        } else {
            setStatus(tv_favorite_num, R.drawable.ic_favorite, R.color.behavior_normal)
        }
        tv_favorite_num.text = favoriteNum.toString()
        return this
    }

    /**
     * 设置点赞初始状态
     * @param status 状态
     * @param count 数量
     */
    @SuppressLint("ResourceType")
    fun setLikeStatus(status: Boolean, count: Int): BehaviorBar {
        tv_like_num.text = count.toString()
        hasLike = status
        likeNum = count
        if (status) {
            setStatus(tv_like_num, R.drawable.ic_like_enable, R.color.behavior_enable)
        } else {
            setStatus(tv_like_num, R.drawable.ic_like, R.color.behavior_normal)
        }
        tv_like_num.text = likeNum.toString()
        return this
    }

    /**
     * 设置点击事件
     * @param init [@kotlin.ExtensionFunctionType] Function1<ClickDelegate, Unit>
     */
    fun setClickDelegate(init: ClickDelegate.() -> Unit) {
        val delegate = ClickDelegate()
        delegate.init()
        setDelegate(delegate)
    }

    /**
     * 点击事件监听代理
     */
    interface Delegate {
        fun onLikeClick()
        fun onFavoriteClick()
    }

    class ClickDelegate : Delegate {

        var onLikeClick: (() -> Unit)? = null
        var onFavoriteClick: (() -> Unit)? = null

        override fun onLikeClick() {
            onLikeClick?.let { it() }
        }

        override fun onFavoriteClick() {
            onFavoriteClick?.let { it() }
        }
    }
}
