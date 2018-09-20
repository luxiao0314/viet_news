package com.viet.news.core.ext

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.safframework.log.L
import com.viet.news.core.R

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 20/09/2018 11:38 AM
 * @Version
 */
fun RecyclerView.linearHor() {
    this.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
}

fun RecyclerView.linear() {
    this.layoutManager = LinearLayoutManager(this.context)
}

fun RecyclerView.grid(spanCount: Int) {
    this.layoutManager = GridLayoutManager(this.context, spanCount)
}

fun RecyclerView.itemDecoration() {
    val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.shape_list_divider_gray_05dp)!!)
    addItemDecoration(dividerItemDecoration)
}

fun View.event(click: ((View) -> Unit)? = null, doubleTap: (() -> Unit)? = null,
               longPress: (() -> Unit)? = null) {
    this.isLongClickable = true
    val gestureDetector = GestureDetector(this.context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            L.d("event", "onSingleTapConfirmed ")
            click?.apply { this(this@event) }
            return false
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            L.d("event", "onDoubleTap ")
            doubleTap?.apply { this() }
            return false
        }

        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
            L.d("event", "onLongPres ")
            longPress?.apply { this() }
        }
    })

    this.setOnTouchListener { v, event ->
        gestureDetector.onTouchEvent(event)
    }
}