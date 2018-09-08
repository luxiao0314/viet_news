package com.viet.mine.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * @Description 横向滑动帮助类
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
class CustomSnapHelper : LinearSnapHelper() {
    private var mHorizontalHelper: OrientationHelper? = null

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? {
        val out = IntArray(2)
        //判断支持水平滚动，修改水平方向的位置，是修改的out[0]的值
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager))
        } else {
            out[0] = 0
        }
        return out
    }

    private fun distanceToStart(targetView: View, helper: OrientationHelper): Int {
        return helper.getDecoratedStart(targetView) - helper.startAfterPadding
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        return findStartView(layoutManager, getHorizontalHelper(layoutManager))
    }

    private fun findStartView(layoutManager: RecyclerView.LayoutManager,
                              helper: OrientationHelper): View? {

        if (layoutManager is LinearLayoutManager) {
            val firstChild = layoutManager.findFirstVisibleItemPosition()
            val lastChild = layoutManager.findLastVisibleItemPosition()
            if (firstChild == RecyclerView.NO_POSITION) {
                return null
            }

            val child = layoutManager.findViewByPosition(firstChild)
            //获取偏左显示的Item
            return if (helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2 && helper.getDecoratedEnd(child) > 0) {
                child
            } else {
                layoutManager.findViewByPosition(firstChild + 1)
            }
        }

        return super.findSnapView(layoutManager)
    }


    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return mHorizontalHelper!!
    }
}
