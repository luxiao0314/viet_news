package com.viet.news.core.config

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.*

/**
 * @Description activity管理类，主要用来获取当前的运行实例,对于跳转实用
 * @Author Sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 07/05/2018 1:52 PM
 * @Version
 */
class IActivityManager private constructor() {

    private var mCurrentActivityWeakRef: WeakReference<Activity>? = null

    fun currentActivity(): Activity? {
        var currentActivity: Activity? = null
        if (null != mCurrentActivityWeakRef) {
            currentActivity = mCurrentActivityWeakRef!!.get()
        }
        return currentActivity
    }

    /**
     * 添加活动到应用活动集合中
     *
     * @param activity 活动对象
     */
    fun registerActivity(activity: Activity) {
        mCurrentActivityWeakRef = WeakReference(activity)
        activitys.add(activity)
    }

    /**
     * 结束活动集合中的一个对象
     *
     * @param activity
     */
    fun unregisterActivity(activity: Activity?) {
        if (null != mCurrentActivityWeakRef) {
            mCurrentActivityWeakRef!!.clear()
        }
        if (null != activitys && null != activity) {
            val position = activitys.indexOf(activity)
            if (position >= 0) {
                activitys.removeAt(position)
            }
            activity.finish()
        }
    }

    /**
     * 结束活动的Activity
     */
    fun exitApp() {
        if (null != activitys) {
            synchronized(activitys) {
                for (activity in activitys) {
                    activity.finish()
                }
            }
            activitys.clear()
        }
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }

    companion object {
        /**
         * 应用创建activity集合 在创建activity时调用addActivity方法将新创建的活动添加到集合中
         */
        private val activitys = LinkedList<Activity>()

        var instance = IActivityManager()

        /**
         * 结束最后一个活动之前的Activity
         */
        fun killBeforeActivitys() {
            if (null != activitys) {
                val size = activitys.size - 1
                for (i in 0 until size) {
                    val activity = activitys[i]
                    activity.finish()
                }
                println(activitys.size)
            }
        }

        /**
         * 获取队列中最后一个activity
         *
         * @return ACTIVITY
         */
        fun lastActivity(): Activity? {
            var activity: Activity? = null
            if (null != activitys) {
                activity = activitys.last
            }
            return activity
        }
    }
}
