package cn.magicwindow.core.utils

import android.app.Activity
import android.graphics.Rect
import android.support.v4.app.Fragment
import android.view.View

/**
 * @author shuqing.li@magicwindow.cn
 * @description 用于监听软键盘是否打开，不一定准确。
 */
class SoftKeyInputVisibleUtils {
    companion object {
        //用200像素来判断键盘的高度，若大于200像素，则认为键盘已经出现了
        private const val SOFT_KEY_BOARD_MIN_HEIGHT = 200
    }

    /*
     * 软键盘是否可见的监听
     */
    private lateinit var callback: (Boolean) -> Unit

    fun registerFragment(fragment: Fragment, callback: (Boolean) -> Unit): SoftKeyInputVisibleUtils {
        return registerView(fragment.view, callback)
    }

    fun registerActivity(activity: Activity, callback: (Boolean) -> Unit): SoftKeyInputVisibleUtils {
        return registerView(activity.window.decorView.findViewById(android.R.id.content), callback)
    }

    fun registerView(view: View?, callback: (Boolean) -> Unit): SoftKeyInputVisibleUtils {
        this.callback = callback
        view?.let {
            it.viewTreeObserver.addOnGlobalLayoutListener {
                //测量当前用户界面
                val r = Rect()
                view.getWindowVisibleDisplayFrame(r)
                //计算手机屏幕底部与用户界面底部的距离 如果键盘出来了，一般来说就是键盘的高度
                val heightDiff = view.rootView.height - (r.bottom - r.top)
                //如果大于200像素，则应该是键盘出现了
                if (heightDiff > SOFT_KEY_BOARD_MIN_HEIGHT) {
                    //键盘出来了
                    callback(true)
                } else {
                    //键盘收起了
                    callback(false)
                }
            }
        }
        return this
    }
}