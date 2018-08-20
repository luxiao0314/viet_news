package com.viet.news.webview

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout

/**
 * found by Ricardo
 * Android5497bug,一个解决方案
 */
class AndroidBug5497Helper private constructor(activity: Activity) {

    private val mChildOfContent: View
    private var usableHeightPrevious: Int = 0
    private val frameLayoutParams: FrameLayout.LayoutParams
    private var layoutParamsHeight: Int = 0

    init {
        //找到activity的根View
        val content = activity.findViewById<View>(android.R.id.content) as FrameLayout
        mChildOfContent = content.getChildAt(0)
        //设置监听软键盘弹出
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener { possiblyResizeChildOfContent() }
        frameLayoutParams = mChildOfContent.layoutParams as FrameLayout.LayoutParams
        //frameLayout原始高度
        layoutParamsHeight = frameLayoutParams.height
    }

    /**
     * 重设高度
     */
    private fun possiblyResizeChildOfContent() {
        val usableHeightNow = computeUsableHeight()
//        L.i("usableHeightNow:$usableHeightNow, contentHeight:${mChildOfContent.rootView.height}")
        if (usableHeightNow != usableHeightPrevious) {
            //WebView可见高度
            val contentHeight = mChildOfContent.rootView.height
            //键盘高度
            val heightDifference = contentHeight - usableHeightNow
            if (heightDifference > contentHeight / 4) {
                // 键盘刚刚可见
                frameLayoutParams.height = contentHeight - heightDifference
            } else {
                // 键盘刚刚不可见
                frameLayoutParams.height = layoutParamsHeight
            }
            mChildOfContent.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    /**
     * 计算可用高低，即软键盘之上可用高度
     */
    private fun computeUsableHeight(): Int {
        val r = Rect()
        mChildOfContent.getWindowVisibleDisplayFrame(r)
        return r.bottom - r.top// 全屏模式下： return r.bottom
    }

    companion object {
        // For more information, see https://code.google.com/p/android/issues/detail?id=5497
        // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.
        fun assistActivity(activity: Activity) {
            AndroidBug5497Helper(activity)
        }
    }

}