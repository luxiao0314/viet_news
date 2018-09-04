package com.viet.news.core.ui

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NoScrollViewPager : ViewPager {
    private var scroll = false

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context) : super(context)

    fun setScrollEnable(scroll: Boolean) {
        this.scroll = scroll
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        return scroll && super.onTouchEvent(arg0)
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return scroll && super.onInterceptTouchEvent(arg0)
    }

}
