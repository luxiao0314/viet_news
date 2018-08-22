package com.lcorekit.channeldemo.utlis

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager


/**
 * @author null
 */
object ResolutionUtils {
    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService(Activity.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    fun dp2px(value: Int, context: Context): Int {
        return (context.applicationContext.resources.displayMetrics.density * value).toInt()
    }

    fun sp2px(value: Int, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value.toFloat(), context.applicationContext.resources.displayMetrics).toInt()
    }
}
