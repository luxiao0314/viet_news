package com.viet.news.core.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.annotation.IdRes
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
    private var isLiked: Boolean = false
    private var isCollected: Boolean = false
    private var isPraised: Boolean = false
    private var likedNum: Int = 0
    private var collectedNum: Int = 0
    private var praisedNum: Int = 0

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
            R.styleable.BehaviorBar_like_number -> {
                likedNum = typedArray.getInt(index, 0)
                tv_like_num.text = likedNum.toString()
            }
            R.styleable.BehaviorBar_collect_number -> {
                collectedNum = typedArray.getInt(index, 0)
                tv_collect_num.text = collectedNum.toString()
            }
            R.styleable.BehaviorBar_praise_number -> {
                praisedNum = typedArray.getInt(index, 0)
                tv_praise_num.text = praisedNum.toString()
            }
            R.styleable.BehaviorBar_is_liked -> {
                isLiked = typedArray.getBoolean(index, false)
                if (isLiked) {
                    setStatus(tv_like_num, R.drawable.ic_like_enable, R.color.behavior_enable)
                } else {
                    setStatus(tv_like_num, R.drawable.ic_like, R.color.behavior_normal)
                }
            }
            R.styleable.BehaviorBar_is_collected -> {
                isCollected = typedArray.getBoolean(index, false)
                if (isCollected) {
                    setStatus(tv_collect_num, R.drawable.ic_collect_enable, R.color.behavior_enable)
                } else {
                    setStatus(tv_collect_num, R.drawable.ic_collect, R.color.behavior_normal)
                }
            }
            R.styleable.BehaviorBar_is_praised -> {
                isPraised = typedArray.getBoolean(index, false)
                if (isPraised) {
                    setStatus(tv_praise_num, R.drawable.ic_praise_enable, R.color.behavior_enable)
                } else {
                    setStatus(tv_praise_num, R.drawable.ic_praise, R.color.behavior_normal)
                }
            }
        }
    }


    /**
     * 设置监听
     */
    @SuppressLint("ResourceType")
    private fun setListener() {
        tv_like_num.clickWithTrigger {
            if (isLiked) {
                tv_like_num.text = if (likedNum == 0) "0" else (--likedNum).toString()
                setStatus(tv_like_num, R.drawable.ic_like, R.color.behavior_normal)
                isLiked = false
            } else {
                tv_like_num.text = (++likedNum).toString()
                setStatus(tv_like_num, R.drawable.ic_like_enable, R.color.behavior_enable)
                isLiked = true
            }
            mDelegate?.onLikeClick(isLiked, likedNum)
        }

        tv_collect_num.clickWithTrigger {
            if (isCollected) {
                tv_collect_num.text = if (collectedNum == 0) "0" else (--collectedNum).toString()
                setStatus(tv_collect_num, R.drawable.ic_collect, R.color.behavior_normal)
                isCollected = false
            } else {
                tv_collect_num.text = (++collectedNum).toString()
                setStatus(tv_collect_num, R.drawable.ic_collect_enable, R.color.behavior_enable)
                isCollected = true
            }
            mDelegate?.onCollectClick(isCollected, collectedNum)
        }
        tv_praise_num.clickWithTrigger {
            if (isPraised) {
                tv_praise_num.text = if (praisedNum == 0) "0" else (--praisedNum).toString()
                setStatus(tv_praise_num, R.drawable.ic_praise, R.color.behavior_normal)
                isPraised = false
            } else {
                tv_praise_num.text = (++praisedNum).toString()
                setStatus(tv_praise_num, R.drawable.ic_praise_enable, R.color.behavior_enable)
                isPraised = true
            }
            mDelegate?.onPraiseClick(isPraised, praisedNum)
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
        view.setTextColor(context.resources.getColor(color))
    }

    /**
     * 重新设置图标
     * @param id 资源id
     */
    @SuppressLint("ResourceType")
    private fun getDrawable(@IdRes id: Int): Drawable {
        val drawable = context.resources.getDrawable(id)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
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
    fun setLikedStatus(status: Boolean, count: Int) {
        tv_like_num.text = count.toString()
        isLiked = status
        likedNum = count
        if (status) {
            setStatus(tv_like_num, R.drawable.ic_like_enable, R.color.behavior_enable)
        } else {
            setStatus(tv_like_num, R.drawable.ic_like, R.color.behavior_normal)
        }
    }

    /**
     * 设置收藏初始状态
     */
    @SuppressLint("ResourceType")
    fun setCollectedStatus(status: Boolean, count: Int) {
        tv_collect_num.text = count.toString()
        isCollected = status
        collectedNum = count
        if (status) {
            setStatus(tv_collect_num, R.drawable.ic_collect_enable, R.color.behavior_enable)
        } else {
            setStatus(tv_collect_num, R.drawable.ic_collect, R.color.behavior_normal)
        }
    }

    /**
     * 设置点赞初始状态
     * @param status 状态
     * @param count 数量
     */
    @SuppressLint("ResourceType")
    fun setPraiseStatus(status: Boolean, count: Int) {
        tv_praise_num.text = count.toString()
        isPraised = status
        praisedNum = count
        if (status) {
            setStatus(tv_praise_num, R.drawable.ic_praise_enable, R.color.behavior_enable)
        } else {
            setStatus(tv_praise_num, R.drawable.ic_praise, R.color.behavior_normal)
        }
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
        fun onLikeClick(isLiked: Boolean, likedNum: Int)
        fun onCollectClick(isCollected: Boolean, collectedNum: Int)
        fun onPraiseClick(isPraise: Boolean, praiseNum: Int)
    }

    class ClickDelegate : Delegate {

        var onLikeClick: ((Boolean, Int) -> Unit)? = null
        var onCollectClick: ((Boolean, Int) -> Unit)? = null
        var onPraiseClick: ((Boolean, Int) -> Unit)? = null

        override fun onLikeClick(isLiked: Boolean, likedNum: Int) {
            onLikeClick?.let { it(isLiked, likedNum) }
        }

        override fun onCollectClick(isCollected: Boolean, collectedNum: Int) {
            onCollectClick?.let { it(isCollected, collectedNum) }
        }

        override fun onPraiseClick(isPraise: Boolean, praiseNum: Int) {
            onPraiseClick?.let { it(isPraise, praiseNum) }
        }

    }

}
