package com.viet.news.core.ext

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.viet.news.core.R
import com.viet.news.core.ui.App


/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 2018/4/10 12:50
 * @description
 */


/**
 * 向Toast中添加自定义view
 * @param view
 * @param postion
 * @return
 */
//fun Toast.addView(addView: View, position: Int): Toast {
//
//    (view as LinearLayout).addView(addView, position)
//
//    return this
//}
//
//fun Toast.addView(block: () -> View, position: Int): Toast {
//
//    (view as LinearLayout).addView(block(), position)
//
//    return this
//}

private fun Toast.setGravityCenter(): Toast {
    setGravity(Gravity.CENTER, 0, 0)
    return this
}

/**
 * 设置Toast字体及背景
 * @param messageColor
 * @param background
 * @return
 */
private fun Toast.setBackground(@ColorInt messageColor: Int, @DrawableRes background: Int): Toast {
    val view = view
    if (view != null) {
        val message = view.findViewById(android.R.id.message) as TextView
        view.setBackgroundResource(background)
        message.setTextColor(messageColor)
    }
    return this
}

@SuppressLint("ShowToast")
fun toast(text: CharSequence?, @ColorInt messageColor: Int = Color.WHITE, @DrawableRes background: Int = R.color.color_bg_trans, vararg views: Pair<View, Int> = arrayOf()) =
        runOnUIThread {
            Toast.makeText(App.instance, text, Toast.LENGTH_SHORT)
                    .setGravityCenter()
                    .setBackground(messageColor, background)
                    .also {
                        if (views.isNotEmpty()) {
                            for (addView in views) {
                                (it.view as LinearLayout).addView(addView.first, addView.second)
                            }
                        }
                    }
                    .show()
        }

@SuppressLint("ShowToast")
fun toast(@StringRes res: Int, @ColorInt messageColor: Int = Color.WHITE, @DrawableRes background: Int = R.color.color_bg_trans, vararg views: Pair<View, Int> = arrayOf()) =
        toast(App.instance.resources.getString(res), messageColor, background, *views)
