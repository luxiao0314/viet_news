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
import com.viet.news.core.R

class BehaviorBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    lateinit var likeCtv: AppCompatCheckedTextView
        private set
    lateinit var collectCtv: AppCompatCheckedTextView
        private set
    lateinit var praiseCtv: AppCompatCheckedTextView
        private set
    private var isLiked: Boolean = false
    private var isCollected: Boolean = false
    private var isPraised: Boolean = false
    private var likedNum: Int = 0
    private var collectedNum: Int = 0
    private var praisedNum: Int = 0

    private var mDelegate: Delegate? = null

    init {
        View.inflate(context, R.layout.behaviorbar, this)
        initView()
        setListener()
        initAttrs(context, attrs)
    }

    private fun initView() {
        likeCtv = getViewById(R.id.tv_like_num)
        collectCtv = getViewById(R.id.tv_collect_num)
        praiseCtv = getViewById(R.id.tv_praise_num)
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
                likeCtv.text = likedNum.toString()
            }
            R.styleable.BehaviorBar_collect_number -> {
                collectedNum = typedArray.getInt(index, 0)
                collectCtv.text = collectedNum.toString()
            }
            R.styleable.BehaviorBar_praise_number -> {
                praisedNum = typedArray.getInt(index, 0)
                praiseCtv.text = praisedNum.toString()
            }
            R.styleable.BehaviorBar_is_liked -> {
                isLiked = typedArray.getBoolean(index, false)
                if (isLiked) {
                    setStatus(likeCtv, R.drawable.ic_like_enable, R.color.behavior_enable)
                } else {
                    setStatus(likeCtv, R.drawable.ic_like, R.color.behavior_normal)
                }
            }
            R.styleable.BehaviorBar_is_collected -> {
                isCollected = typedArray.getBoolean(index, false)
                if (isCollected) {
                    setStatus(collectCtv, R.drawable.ic_collect_enable, R.color.behavior_enable)
                } else {
                    setStatus(collectCtv, R.drawable.ic_collect, R.color.behavior_normal)
                }
            }
            R.styleable.BehaviorBar_is_praised -> {
                isPraised = typedArray.getBoolean(index, false)
                if (isPraised) {
                    setStatus(praiseCtv, R.drawable.ic_praise_enable, R.color.behavior_enable)
                } else {
                    setStatus(praiseCtv, R.drawable.ic_praise, R.color.behavior_normal)
                }
            }
        }
    }


    /**
     * 设置监听
     */
    private fun setListener() {
        likeCtv.setOnClickListener(object : OnNoDoubleClickListener() {
            @SuppressLint("ResourceType")
            override fun onNoDoubleClick(v: View) {
                if (isLiked) {
                    likeCtv.text = if (likedNum == 0) "0" else (--likedNum).toString()
                    setStatus(likeCtv, R.drawable.ic_like, R.color.behavior_normal)
                    isLiked = false
                } else {
                    likeCtv.text = (++likedNum).toString()
                    setStatus(likeCtv, R.drawable.ic_like_enable, R.color.behavior_enable)
                    isLiked = true
                }
                mDelegate?.onLikeClick(isLiked, likedNum)
            }
        })

        collectCtv.setOnClickListener(object : OnNoDoubleClickListener() {
            @SuppressLint("ResourceType")
            override fun onNoDoubleClick(v: View) {
                if (isCollected) {
                    collectCtv.text = if (collectedNum == 0) "0" else (--collectedNum).toString()
                    setStatus(collectCtv, R.drawable.ic_collect, R.color.behavior_normal)
                    isCollected = false
                } else {
                    collectCtv.text = (++collectedNum).toString()
                    setStatus(collectCtv, R.drawable.ic_collect_enable, R.color.behavior_enable)
                    isCollected = true
                }
                mDelegate?.onCollectClick(isCollected, collectedNum)
            }
        })


        praiseCtv.setOnClickListener(object : OnNoDoubleClickListener() {
            @SuppressLint("ResourceType")
            override fun onNoDoubleClick(v: View) {
                if (isPraised) {
                    praiseCtv.text = if (praisedNum == 0) "0" else (--praisedNum).toString()
                    setStatus(praiseCtv, R.drawable.ic_praise, R.color.behavior_normal)
                    isPraised = false
                } else {
                    praiseCtv.text = (++praisedNum).toString()
                    setStatus(praiseCtv, R.drawable.ic_praise_enable, R.color.behavior_enable)
                    isPraised = true
                }
                mDelegate?.onPraiseClick(isPraised, praisedNum)
            }
        })
    }


    @SuppressLint("ResourceType")
    fun setLikedStatus(status: Boolean, count: Int) {
        likeCtv.text = count.toString()
        isLiked = status
        likedNum = count
        if (status) {
            setStatus(likeCtv, R.drawable.ic_like_enable, R.color.behavior_enable)
        } else {
            setStatus(likeCtv, R.drawable.ic_like, R.color.behavior_normal)
        }
    }

    @SuppressLint("ResourceType")
    fun setCollectedStatus(status: Boolean, count: Int) {
        collectCtv.text = count.toString()
        isCollected = status
        collectedNum = count
        if (status) {
            setStatus(collectCtv, R.drawable.ic_collect_enable, R.color.behavior_enable)
        } else {
            setStatus(collectCtv, R.drawable.ic_collect, R.color.behavior_normal)
        }
    }

    @SuppressLint("ResourceType")
    fun setPraiseStatus(status: Boolean, count: Int) {
        praiseCtv.text = count.toString()
        isPraised = status
        praisedNum = count
        if (status) {
            setStatus(praiseCtv, R.drawable.ic_praise_enable, R.color.behavior_enable)
        } else {
            setStatus(praiseCtv, R.drawable.ic_praise, R.color.behavior_normal)
        }
    }

    @SuppressLint("ResourceType")
    private fun setStatus(view: AppCompatCheckedTextView, @IdRes id: Int, @IdRes color: Int) {
        view.setCompoundDrawables(getDrawable(id), null, null, null)
        view.setTextColor(context.resources.getColor(color))
    }

    /**
     * 重新设置图标
     */
    @SuppressLint("ResourceType")
    private fun getDrawable(@IdRes id: Int): Drawable {
        val drawable = context.resources.getDrawable(id)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        return drawable
    }


    fun setClickDelegate(init: ClickDelegate.() -> Unit) {
        val delegate = ClickDelegate()
        delegate.init()
        setDelegate(delegate)
    }

    private fun setDelegate(delegate: ClickDelegate): BehaviorBar {
        mDelegate = delegate
        return this
    }

    /**
     * 点击事件监听代理
     */
    interface Delegate {
        fun onLikeClick(isliked: Boolean, likedNum: Int)
        fun onCollectClick(isCollected: Boolean, collectedNum: Int)
        fun onPraiseClick(isPraise: Boolean, praiseNum: Int)
    }

    class ClickDelegate : Delegate {

        var onLikeClick: ((Boolean, Int) -> Unit)? = null
        var onCollectClick: ((Boolean, Int) -> Unit)? = null
        var onPraiseClick: ((Boolean, Int) -> Unit)? = null

        override fun onLikeClick(isliked: Boolean, likedNum: Int) {
            onLikeClick?.let { it(isliked, likedNum) }
        }

        override fun onCollectClick(isCollected: Boolean, collectedNum: Int) {
            onCollectClick?.let { it(isCollected, collectedNum) }
        }

        override fun onPraiseClick(isPraise: Boolean, praiseNum: Int) {
            onPraiseClick?.let { it(isPraise, praiseNum) }
        }

    }


    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
    </VT> */
    private fun <VT : View> getViewById(@IdRes id: Int): VT {
        return findViewById<View>(id) as VT
    }


    private abstract class OnNoDoubleClickListener : View.OnClickListener {
        private var mThrottleFirstTime = 10
        private var mLastClickTime: Long = 0

        constructor()

        constructor(throttleFirstTime: Int) {
            mThrottleFirstTime = throttleFirstTime
        }

        override fun onClick(v: View) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - mLastClickTime > mThrottleFirstTime) {
                mLastClickTime = currentTime
                onNoDoubleClick(v)
            }
        }

        abstract fun onNoDoubleClick(v: View)
    }


}
