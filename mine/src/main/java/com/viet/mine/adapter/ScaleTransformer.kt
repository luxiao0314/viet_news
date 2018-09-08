package com.viet.mine.adapter

import android.support.v4.view.ViewPager
import android.view.View

/**
 * Created by longzongjia on 2018/3/17.
 */

class ScaleTransformer : ViewPager.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        if (position < -1 || position > 1) {
            page.scaleX = MIN_SCALE
            page.scaleY = MIN_SCALE
        } else if (position <= 1) { // [-1,1]
            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
            if (position < 0) {
                val scaleX = 1 + 0.2f * position
                page.scaleX = scaleX
                page.scaleY = scaleX
            } else {
                val scaleX = 1 - 0.2f * position
                page.scaleX = scaleX
                page.scaleY = scaleX
            }
        }
    }

    companion object {
        private const val MIN_SCALE = 0.80f
    }
}
