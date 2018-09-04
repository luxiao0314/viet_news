package com.viet.news.core.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

/**
 * Created by null on 2018/3/14.
 */

class SwipeLayout : FrameLayout {

    private var helper: ViewDragHelper? = null
    private var mContent: View? = null
    private var mBehind: View? = null
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mRange: Int = 0
    private var isOpen = false
    private var status = Status.CLOSE
    private var downX: Int = 0
    private var downY: Int = 0
    private lateinit var contentRect: Rect

    private val callback = object : ViewDragHelper.Callback() {
        /**
         * 是否能拖拽
         */
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return true
        }

        /**
         * 空间位置改变时的操作
         */
        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            if (changedView === mBehind) {
                mContent!!.offsetLeftAndRight(dx)
            } else if (changedView === mContent) {
                mBehind!!.offsetLeftAndRight(dx)
            }
            dispatchEvent()
            invalidate()
        }

        /**
         * 松开手指时是执行的方法
         *
         * @param xvel x方向加速度
         * @param yvel y方向加速度
         */
        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            if (xvel == 0f && mContent!!.left < -mRange * 0.5f) {
                openItem()
            } else if (xvel < 0) {
                openItem()
            } else {
                closeItem()
            }
        }

        /**
         * 获取拖拽的范围
         */
        override fun getViewHorizontalDragRange(child: View): Int {
            return mRange
        }

        /**
         * 限制拖拽的范围
         */
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            var leftRange = left
            if (child === mContent) {
                if (leftRange < -mRange) {
                    leftRange = -mRange
                } else if (leftRange > 0) {
                    leftRange = 0
                }
            }
            if (child === mBehind) {
                if (leftRange < mWidth - mRange) {
                    leftRange = mWidth - mRange
                } else if (leftRange > mWidth) {
                    leftRange = mWidth
                }
            }
            return leftRange
        }
    }

    var swipingListener: OnSwipingListener? = null

    /**
     * 打开，关闭，滑动
     */
    enum class Status {
        OPEN,
        CLOSE,
        SWIPING
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        helper = ViewDragHelper.create(this, callback)
    }

    /**
     * 关闭条目
     */
    private fun closeItem() {
        close(true)
    }

    fun close(isSmooth: Boolean) {
        if (isSmooth) {
            if (helper!!.smoothSlideViewTo(mContent!!, 0, 0)) {
                ViewCompat.postInvalidateOnAnimation(this)
            }
        } else {
            layoutContent(false)
        }
    }

    /**
     * 打开条目
     */
    private fun openItem() {
        open(true)
    }

    private fun open(isSmooth: Boolean) {
        if (isSmooth) {
            if (helper!!.smoothSlideViewTo(mContent!!, -mRange, 0)) {
                ViewCompat.postInvalidateOnAnimation(this)
            }
        } else {
            layoutContent(true)
        }
    }

    override fun computeScroll() {
        if (helper!!.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    /**
     * 分发滑动事件
     */
    private fun dispatchEvent() {
        val lastStatus = status
        status = updateStatus()
        if (status != lastStatus && swipingListener != null) {
            when (status) {
                Status.OPEN -> {
                    println("打开状态")
                    swipingListener!!.onOpened(this)
                }
                Status.CLOSE -> {
                    println("关闭状态")
                    swipingListener!!.onClosed(this)
                }
                Status.SWIPING -> {
                    if (lastStatus == Status.CLOSE) {
                        println("正在打开状态")
                        swipingListener!!.onSwiping(this)
                    }
                }
            }
        }
    }

    private fun updateStatus(): Status {
        val left = mContent!!.left
        if (left == -mRange) {
            return Status.OPEN
        } else if (left == 0) {
            return Status.CLOSE
        }
        return Status.SWIPING
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        //获取主体布局
        mContent = getChildAt(1)
        //获取隐藏布局
        mBehind = getChildAt(0)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = measuredWidth
        mHeight = measuredHeight
        mRange = mBehind!!.measuredWidth
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        layoutContent(isOpen)
    }

    /**
     * 动态更新布局
     */
    private fun layoutContent(isOpen: Boolean) {
        contentRect = computeContentRect(isOpen)
        mContent!!.layout(contentRect.left, contentRect.top, contentRect.right, contentRect.bottom)
        val behindRect = computeBehindRect(contentRect)
        mBehind!!.layout(behindRect.left, behindRect.top, behindRect.right, behindRect.bottom)
    }

    /**
     * 计算布局边界
     */
    private fun computeBehindRect(contentRect: Rect): Rect {
        return Rect(contentRect.right, 0, contentRect.right + mRange, mHeight)
    }

    private fun computeContentRect(isOpen: Boolean): Rect {
        val left = if (isOpen) {
            -mRange
        } else {
            0
        }
        return Rect(left, 0, left + mWidth, mHeight)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x.toInt()
                downY = ev.y.toInt()
                //按下时请求父控件不要拦截事件
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = ev.x.toInt()
                val moveY = ev.y.toInt()

                val diffX = moveX - downX
                val diffY = moveY - downY
                //当上下滑时请求父控件拦截
                if (Math.abs(diffX) < Math.abs(diffY)) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    //其它情况不拦截
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return helper!!.shouldInterceptTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        try {
            helper!!.processTouchEvent(event)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    fun setOnSwipingListener(swipingListener: OnSwipingListener) {
        this.swipingListener = swipingListener
    }

    /**
     * 相关状态监听
     */
    interface OnSwipingListener {
        fun onOpened(layout: SwipeLayout)

        fun onClosed(layout: SwipeLayout)

        fun onSwiping(layout: SwipeLayout)
    }

}
