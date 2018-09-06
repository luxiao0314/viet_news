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
fun Toast.addView(addView: View, position: Int): Toast {

    (view as LinearLayout).addView(addView, position)

    return this
}

fun Toast.addView(block: () -> View, position: Int): Toast {

    (view as LinearLayout).addView(block(), position)

    return this
}

fun Toast.setGravityCenter(): Toast {
    setGravity(Gravity.CENTER, 0, 0)
    return this
}

/**
 * 设置Toast字体及背景颜色
 * @param messageColor
 * @param backgroundColor
 * @return
 */
fun Toast.setToastColor(@ColorInt messageColor: Int, @ColorInt backgroundColor: Int) {
    val view = view
    if (view != null) {
        val message = view.findViewById(android.R.id.message) as TextView
        message.setBackgroundColor(backgroundColor)
        message.setTextColor(messageColor)
    }
}

/**
 * 设置Toast字体及背景
 * @param messageColor
 * @param background
 * @return
 */
fun Toast.setBackground(@ColorInt messageColor: Int = Color.WHITE, @DrawableRes background: Int = R.color.gray_tran): Toast {
    val view = view
    if (view != null) {
        val message = view.findViewById(android.R.id.message) as TextView
        view.setBackgroundResource(background)
        message.setTextColor(messageColor)
    }
    return this
}

@SuppressLint("ShowToast")
fun toast(text: CharSequence?): Toast = Toast.makeText(App.instance, text, Toast.LENGTH_SHORT)
        .setGravityCenter()
        .setBackground()
//需要的地方调用withErrorIcon，默认不要添加
//        .withErrorIcon()


@SuppressLint("ShowToast")
fun toast(@StringRes res: Int): Toast = Toast.makeText(App.instance, App.instance.resources.getString(res), Toast.LENGTH_SHORT)
        .setGravityCenter()
        .setBackground()
