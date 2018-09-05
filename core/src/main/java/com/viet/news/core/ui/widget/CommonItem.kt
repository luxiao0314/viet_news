package com.viet.news.core.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.support.annotation.IdRes
import android.util.AttributeSet
import android.view.View
import android.view.WindowId
import android.widget.RelativeLayout
import com.safframework.ext.clickWithTrigger
import com.viet.news.core.R
import kotlinx.android.synthetic.main.core_item.view.*

class CommonItem @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    private var mDelegate: CommonItem.Delegate? = null

    init {
        View.inflate(context, R.layout.core_item, this)
        initAttrs(context, attrs)
        setListener()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonItem)
        val count = typedArray.indexCount
        for (i in 0 until count) {
            initAttr(typedArray.getIndex(i), typedArray)
        }
        typedArray.recycle()
    }

    private fun initAttr(index: Int, typedArray: TypedArray) {
        when (index) {
            R.styleable.CommonItem_left_text -> {
                tv_item_left.text = typedArray.getString(index).toString()
            }
            R.styleable.CommonItem_right_text -> {
                tv_item_right.text = typedArray.getString(index).toString()
            }
            R.styleable.CommonItem_left_text_visibility -> {
                val isShowLeft = typedArray.getBoolean(index, true)
                setStatus(tv_item_left, isShowLeft)
            }
            R.styleable.CommonItem_right_text_visibility -> {
                val isShowRight = typedArray.getBoolean(index, true)
                setStatus(tv_item_right, isShowRight)
            }
            R.styleable.CommonItem_next_icon_visibility -> {
                val isShowNext = typedArray.getBoolean(index, true)
                setStatus(iv_next, isShowNext)
            }
            R.styleable.CommonItem_left_text_color -> {
                val color = typedArray.getColor(index, resources.getColor(R.color.text_gray))
                tv_item_left.setTextColor(color)
            }
            R.styleable.CommonItem_right_text_color -> {
                val color = typedArray.getColor(index, resources.getColor(R.color.text_gray))
                tv_item_right.setTextColor(color)
            }
        }
    }

    private fun setDelegate(delegate: ClickDelegate): CommonItem {
        mDelegate = delegate
        return this
    }

    private fun setListener() {
        item.clickWithTrigger {
            mDelegate?.onItemClick()
        }
    }

    private fun setStatus(view: View, status: Boolean) {
        view.visibility = if (status) View.VISIBLE else View.GONE
    }

    fun setLeftText(msg: String): CommonItem {
        tv_item_left.text = msg
        return this
    }

    fun setRightText(msg: String): CommonItem {
        tv_item_right.text = msg
        return this
    }

    fun setLeftTextVisibility(status: Boolean): CommonItem {
        setStatus(tv_item_left, status)
        return this
    }

    fun setRightTextVisibility(status: Boolean): CommonItem {
        setStatus(tv_item_right, status)
        return this
    }

    fun setNextIconVisibility(status: Boolean): CommonItem {
        setStatus(iv_next, status)
        return this
    }


    fun setLeftTextColor(@IdRes color: Int): CommonItem {
        tv_item_left.setTextColor(color)
        return this
    }

    fun setRightTextColor(@IdRes color: Int): CommonItem {
        tv_item_right.setTextColor(color)
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
        fun onItemClick()
    }

    class ClickDelegate : Delegate {
        var onItemClick: (() -> Unit)? = null

        override fun onItemClick() {
            onItemClick?.let { it() }
        }
    }

}