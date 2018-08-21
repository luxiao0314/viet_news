package com.viet.news.core.ui.widget

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v7.widget.AppCompatCheckedTextView
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.widget.RelativeLayout
import com.safframework.ext.hideKeyboard
import com.viet.news.core.R
import kotlinx.android.synthetic.main.core_titlebar.view.*

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 18/03/2018 22:23
 * @description
 */
class TitleBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    lateinit var titleCtv: AppCompatCheckedTextView
        private set
    lateinit var leftCtv: AppCompatCheckedTextView
        private set
    lateinit var rightCtv: AppCompatCheckedTextView
        private set
    lateinit var rightSecondaryCtv: AppCompatCheckedTextView
        private set
    private var mDelegate: Delegate? = null

    init {
        View.inflate(context, R.layout.core_titlebar, this)
        initView()
        setListener()
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
        val count = typedArray.indexCount
        for (i in 0 until count) {
            initAttr(typedArray.getIndex(i), typedArray)
        }
        typedArray.recycle()
    }

    private fun initView() {
        leftCtv = getViewById(R.id.ctv_titlebar_left)
        rightCtv = getViewById(R.id.ctv_titlebar_right)
        rightSecondaryCtv = getViewById(R.id.ctv_titlebar_right_secondary)
        titleCtv = getViewById(R.id.ctv_titlebar_title)
    }

    private fun setListener() {
        leftCtv.setOnClickListener(object : OnNoDoubleClickListener() {
            override fun onNoDoubleClick(v: View) {
                if (mDelegate != null) {
                    mDelegate?.onClickLeft()
                } else {
                    sendKeyBackEvent()
                }
            }
        })
        iv_back.setOnClickListener(object : OnNoDoubleClickListener() {
            override fun onNoDoubleClick(v: View) {
                if (mDelegate != null) {
                    mDelegate?.onClickLeft()
                } else {
                    sendKeyBackEvent()
                }
            }
        })
        titleCtv.setOnClickListener(object : OnNoDoubleClickListener() {
            override fun onNoDoubleClick(v: View) {
                if (mDelegate != null) {
                    mDelegate?.onClickTitle()
                }
            }
        })
        rightCtv.setOnClickListener(object : OnNoDoubleClickListener() {
            override fun onNoDoubleClick(v: View) {
                if (mDelegate != null) {
                    mDelegate?.onClickRight()
                }
            }
        })
        rightSecondaryCtv.setOnClickListener(object : OnNoDoubleClickListener() {
            override fun onNoDoubleClick(v: View) {
                if (mDelegate != null) {
                    mDelegate?.onClickRightSecondary()
                }
            }
        })

    }

    private fun initAttr(attr: Int, typedArray: TypedArray) {
        when (attr) {
            R.styleable.TitleBar_titlebar_leftText -> setLeftText(typedArray.getText(attr))
            R.styleable.TitleBar_titlebar_leftTextColor -> leftCtv.setTextColor(typedArray.getColorStateList(attr))
            R.styleable.TitleBar_titlebar_titleText -> setTitleText(typedArray.getText(attr))
            R.styleable.TitleBar_titlebar_rightText -> setRightText(typedArray.getText(attr))
            R.styleable.TitleBar_titlebar_rightTextColor -> rightCtv.setTextColor(typedArray.getColorStateList(attr))
            R.styleable.TitleBar_titlebar_rightTextSize -> rightCtv.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(attr, sp2px(context, 16f)).toFloat())
            R.styleable.TitleBar_titlebar_rightSecondaryText -> setRightSecondaryText(typedArray.getText(attr))
            R.styleable.TitleBar_titlebar_leftDrawable -> setLeftDrawable(typedArray.getDrawable(attr))
            R.styleable.TitleBar_titlebar_titleDrawable -> setTitleDrawable(typedArray.getDrawable(attr))
            R.styleable.TitleBar_titlebar_titlebarDrawable -> setTitleBarDrawable(typedArray.getDrawable(attr))
            R.styleable.TitleBar_titlebar_rightDrawable -> setRightDrawable(typedArray.getDrawable(attr))
            R.styleable.TitleBar_titlebar_rightSecondaryDrawable -> setRightSecondaryDrawable(typedArray.getDrawable(attr))
            R.styleable.TitleBar_titlebar_leftAndRightTextSize -> {
                val textSize = typedArray.getDimensionPixelSize(attr, sp2px(context, 12f))
                leftCtv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
                rightCtv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
                rightSecondaryCtv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
            }
            R.styleable.TitleBar_titlebar_titleTextSize -> titleCtv.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(attr, sp2px(context, 16f)).toFloat())
            R.styleable.TitleBar_titlebar_leftAndRightTextColor -> {
                leftCtv.setTextColor(typedArray.getColorStateList(attr))
                rightCtv.setTextColor(typedArray.getColorStateList(attr))
                rightSecondaryCtv.setTextColor(typedArray.getColorStateList(attr))
            }
            R.styleable.TitleBar_titlebar_titleTextColor -> titleCtv.setTextColor(typedArray.getColorStateList(attr))
            R.styleable.TitleBar_titlebar_titleDrawablePadding -> titleCtv.compoundDrawablePadding = typedArray.getDimensionPixelSize(attr, dp2px(context, 3f))
            R.styleable.TitleBar_titlebar_leftDrawablePadding -> leftCtv.compoundDrawablePadding = typedArray.getDimensionPixelSize(attr, dp2px(context, 3f))
            R.styleable.TitleBar_titlebar_rightDrawablePadding -> {
                rightCtv.compoundDrawablePadding = typedArray.getDimensionPixelSize(attr, dp2px(context, 3f))
                rightSecondaryCtv.compoundDrawablePadding = typedArray.getDimensionPixelSize(attr, dp2px(context, 3f))
            }
            R.styleable.TitleBar_titlebar_leftAndRightPadding -> {
                val leftAndRightPadding = typedArray.getDimensionPixelSize(attr, dp2px(context, 10f))
                leftCtv.setPadding(leftAndRightPadding, 0, leftAndRightPadding, 0)
                rightCtv.setPadding(leftAndRightPadding / 2, 0, leftAndRightPadding, 0)
                rightSecondaryCtv.setPadding(leftAndRightPadding / 2, 0, leftAndRightPadding / 2, 0)
            }
            R.styleable.TitleBar_titlebar_leftMaxWidth -> setLeftCtvMaxWidth(typedArray.getDimensionPixelSize(attr, dp2px(context, 85f)))
            R.styleable.TitleBar_titlebar_rightMaxWidth -> setRightCtvMaxWidth(typedArray.getDimensionPixelSize(attr, dp2px(context, 85f)))
            R.styleable.TitleBar_titlebar_titleMaxWidth -> setTitleCtvMaxWidth(typedArray.getDimensionPixelSize(attr, dp2px(context, 144f)))
            R.styleable.TitleBar_titlebar_isTitleTextBold -> titleCtv.paint.typeface = getTypeface(typedArray.getBoolean(attr, true))
            R.styleable.TitleBar_titlebar_isLineVisiable -> line.visibility = if (typedArray.getBoolean(attr, true)) View.VISIBLE else View.GONE
            R.styleable.TitleBar_titlebar_isLeftTextBold -> leftCtv.paint.typeface = getTypeface(typedArray.getBoolean(attr, true))
            R.styleable.TitleBar_titlebar_isRightTextBold -> {
                rightCtv.paint.typeface = getTypeface(typedArray.getBoolean(attr, true))
                rightSecondaryCtv.paint.typeface = getTypeface(typedArray.getBoolean(attr, true))
            }
        }
    }

    private fun sendKeyBackEvent() {
        val context = context
        if (context is Activity) {
            if (hideKeyboard()) {
                val keyEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)
                context.onKeyDown(KeyEvent.KEYCODE_BACK, keyEvent)
            } else {
                val keyEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)
                context.onKeyDown(KeyEvent.KEYCODE_BACK, keyEvent)
            }
        }
    }

    private fun getTypeface(isBold: Boolean): Typeface {
        return if (isBold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
    }

    fun setLeftCtvMaxWidth(maxWidth: Int): TitleBar {
        leftCtv.maxWidth = maxWidth
        return this
    }

    fun setRightCtvMaxWidth(maxWidth: Int): TitleBar {
        rightCtv.maxWidth = maxWidth
        rightSecondaryCtv.maxWidth = maxWidth
        return this
    }

    fun setTitleCtvMaxWidth(maxWidth: Int): TitleBar {
        titleCtv.maxWidth = maxWidth
        return this
    }

    fun hiddenLeftCtv(): TitleBar {
        leftCtv.visibility = View.GONE
        return this
    }

    fun showLeftCtv(): TitleBar {
        leftCtv.visibility = View.VISIBLE
        return this
    }

    fun setLeftText(@StringRes resId: Int): TitleBar {
        setLeftText(resources.getString(resId))
        return this
    }

    fun setLeftText(text: CharSequence): TitleBar {
        if (TextUtils.isEmpty(text)) {
            leftCtv.text = ""
            if (leftCtv.compoundDrawables[0] == null) {
                hiddenLeftCtv()
            }
        } else {
            leftCtv.text = text
            showLeftCtv()
        }
        return this
    }

    fun setLeftDrawable(@DrawableRes resId: Int): TitleBar {
        setLeftDrawable(resources.getDrawable(resId))
        return this
    }

    fun setLeftDrawable(drawable: Drawable?): TitleBar {
        if (drawable == null) {
            iv_back.setImageDrawable(null)
            view_line.visibility = View.GONE
//            leftCtv.setCompoundDrawables(null, null, null, null)
            if (TextUtils.isEmpty(leftCtv.text)) {
                hiddenLeftCtv()
            }
        } else {
            view_line.visibility = View.VISIBLE
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            iv_back.setImageDrawable(drawable)
//            leftCtv.setCompoundDrawables(drawable, null, null, null)
            showLeftCtv()
        }
        return this
    }

    fun hiddenTitleCtv(): TitleBar {
        titleCtv.visibility = View.GONE
        return this
    }

    fun showTitleCtv(): TitleBar {
        titleCtv.visibility = View.VISIBLE
        return this
    }

    fun setTitleText(text: CharSequence): TitleBar {
        if (TextUtils.isEmpty(text)) {
            titleCtv.text = ""
            if (titleCtv.compoundDrawables[2] == null) {
                hiddenTitleCtv()
            }
        } else {
            titleCtv.text = text
            showTitleCtv()
        }
        return this
    }

    fun setTitleText(@StringRes resid: Int): TitleBar {
        setTitleText(resources.getString(resid))
        return this
    }

    fun setTitleDrawable(@DrawableRes resId: Int): TitleBar {
        setTitleDrawable(resources.getDrawable(resId))
        return this
    }

    fun setTitleDrawable(drawable: Drawable?): TitleBar {
        if (drawable == null) {
            titleCtv.setCompoundDrawables(null, null, null, null)
            if (TextUtils.isEmpty(titleCtv.text)) {
                hiddenTitleCtv()
            }
        } else {
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            titleCtv.setCompoundDrawables(null, null, drawable, null)
            showTitleCtv()
        }
        return this
    }

    fun setTitleBarDrawable(drawable: Drawable?): TitleBar {
        title_bar.setBackgroundDrawable(drawable)
        return this
    }

    fun hiddenRightSecondaryCtv(): TitleBar {
        rightSecondaryCtv.visibility = View.GONE
        return this
    }

    fun showRightSecondaryCtv(): TitleBar {
        rightSecondaryCtv.visibility = View.VISIBLE
        return this
    }

    fun setRightSecondaryText(text: CharSequence): TitleBar {
        if (TextUtils.isEmpty(text)) {
            rightSecondaryCtv.text = ""
            if (rightSecondaryCtv.compoundDrawables[2] == null) {
                hiddenRightSecondaryCtv()
            }
        } else {
            rightSecondaryCtv.text = text
            showRightSecondaryCtv()
        }
        return this
    }

    fun setRightSecondaryText(@StringRes resid: Int): TitleBar {
        setRightSecondaryText(resources.getString(resid))
        return this
    }

    fun setRightSecondaryDrawable(@DrawableRes resId: Int): TitleBar {
        setRightSecondaryDrawable(resources.getDrawable(resId))
        return this
    }

    fun setRightSecondaryDrawable(drawable: Drawable?): TitleBar {
        if (drawable == null) {
            rightSecondaryCtv.setCompoundDrawables(null, null, null, null)
            if (TextUtils.isEmpty(rightSecondaryCtv.text)) {
                hiddenRightSecondaryCtv()
            }
        } else {
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            rightSecondaryCtv.setCompoundDrawables(null, null, drawable, null)
            showRightSecondaryCtv()
        }
        return this
    }

    fun hiddenRightCtv(): TitleBar {
        rightCtv.visibility = View.GONE
        return this
    }

    fun showRightCtv(): TitleBar {
        rightCtv.visibility = View.VISIBLE
        return this
    }

    fun setRightText(text: CharSequence): TitleBar {
        if (TextUtils.isEmpty(text)) {
            rightCtv.text = ""
            if (rightCtv.compoundDrawables[2] == null) {
                hiddenRightCtv()
            }
        } else {
            rightCtv.text = text
            showRightCtv()
        }
        return this
    }

    fun setRightText(@StringRes resid: Int): TitleBar {
        setRightText(resources.getString(resid))
        return this
    }

    fun setRightDrawable(@DrawableRes resId: Int): TitleBar {
        setRightDrawable(resources.getDrawable(resId))
        return this
    }

    fun setRightDrawable(drawable: Drawable?): TitleBar {
        if (drawable == null) {
            rightCtv.setCompoundDrawables(null, null, null, null)
            if (TextUtils.isEmpty(rightCtv.text)) {
                hiddenRightCtv()
            }
        } else {
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            rightCtv.setCompoundDrawables(null, null, drawable, null)
            showRightCtv()
        }
        return this
    }

    fun setLeftCtvChecked(checked: Boolean): TitleBar {
        leftCtv.isChecked = checked
        return this
    }

    fun setTitleCtvChecked(checked: Boolean): TitleBar {
        titleCtv.isChecked = checked
        return this
    }

    fun setRightCtvChecked(checked: Boolean): TitleBar {
        rightCtv.isChecked = checked
        return this
    }

    fun setRightSecondaryCtvChecked(checked: Boolean): TitleBar {
        rightSecondaryCtv.isChecked = checked
        return this
    }

    fun setDelegate(delegate: Delegate): TitleBar {
        mDelegate = delegate
        return this
    }

    fun setDelegate(init: SimpleDelegate.() -> Unit) {
        val delegate = SimpleDelegate()
        delegate.init()
        setDelegate(delegate)
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

    interface Delegate {
        fun onClickLeft()
        fun onClickTitle()
        fun onClickRight()
        fun onClickRightSecondary()
    }

    class SimpleDelegate : Delegate {

         var onClickLeft: (() -> Unit)? = null
         var onClickTitle: (() -> Unit)? = null
         var onClickRight: (() -> Unit)? = null
         var onClickRightSecondary: (() -> Unit)? = null

        override fun onClickLeft() {
            onClickLeft?.let { it() }
        }

        override fun onClickTitle() {
            onClickTitle?.let { it() }
        }

        override fun onClickRight() {
            onClickRight?.let { it() }
        }

        override fun onClickRightSecondary() {
            onClickRightSecondary?.let { it() }
        }
    }

    private abstract class OnNoDoubleClickListener : View.OnClickListener {
        private var mThrottleFirstTime = 600
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

    companion object {

        fun dp2px(context: Context, dpValue: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.resources.displayMetrics).toInt()
        }

        fun sp2px(context: Context, spValue: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.resources.displayMetrics).toInt()
        }
    }
}