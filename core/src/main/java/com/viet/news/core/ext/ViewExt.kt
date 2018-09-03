package com.viet.news.core.ext

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View

/**
 * View扩展类
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Description
 */

/***
 * 设置延迟时间的View扩展
 * @param delay Long 延迟时间，默认600毫秒
 * @return T
 */
fun <T : View> T.withTrigger(delay: Long = 600): T {
    triggerDelay = delay
    return this
}

/***
 * 点击事件的View扩展
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {

    if (clickEnable()) {
        block(it as T)
    }
}

/***
 * 带延迟过滤的点击事件View扩展
 * @param delay Long 延迟时间，默认600毫秒
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.clickWithTrigger(time: Long = 600, block: (T) -> Unit) {
    triggerDelay = time
    setOnClickListener {
        if (clickEnable()) {
            block(it as T)
        }
    }
}

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
    set(value) {
        setTag(1123460103, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else -1
    set(value) {
        setTag(1123461123, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

fun ViewPager.setOnPageChangeListener(init: PageChangeDelegate.() -> Unit) {
    val pageChangeDelegate = PageChangeDelegate()
    pageChangeDelegate.init()
    setOnPageChangeListener(pageChangeDelegate)
}

class PageChangeDelegate : ViewPager.OnPageChangeListener {

    var onPageScrollStateChanged: ((state: Int) -> Unit)? = null
    var onPageScrolled: ((position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit)? = null
    var onPageSelected: ((position: Int) -> Unit)? = null

    override fun onPageScrollStateChanged(state: Int) {
        onPageScrollStateChanged?.let { it(state) }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        onPageScrolled?.let { it(position, positionOffset, positionOffsetPixels) }
    }

    override fun onPageSelected(position: Int) {
        onPageSelected?.let { it(position) }
    }
}

fun Application.registerActivityLifecycleCallbacks(init: RegisterActivityLifecycleCallbacks.() -> Unit) {
    val callback = RegisterActivityLifecycleCallbacks()
    callback.init()
    registerActivityLifecycleCallbacks(callback)
}

class RegisterActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    var onActivityPaused: ((p0: Activity?) -> Unit)? = null
    var onActivityResumed: ((p0: Activity?) -> Unit)? = null
    var onActivityStarted: ((p0: Activity?) -> Unit)? = null
    var onActivityDestroyed: ((p0: Activity?) -> Unit)? = null
    var onActivitySaveInstanceState: ((p0: Activity?, p1: Bundle?) -> Unit)? = null
    var onActivityStopped: ((p0: Activity?) -> Unit)? = null
    var onActivityCreated: ((p0: Activity?, p1: Bundle?) -> Unit)? = null

    override fun onActivityPaused(p0: Activity?) {
        onActivityPaused?.let { it(p0) }
    }

    override fun onActivityResumed(p0: Activity?) {
        onActivityResumed?.let { it(p0) }
    }

    override fun onActivityStarted(p0: Activity?) {
        onActivityStarted?.let { it(p0) }
    }

    override fun onActivityDestroyed(p0: Activity?) {
        onActivityDestroyed?.let { it(p0) }
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
        onActivitySaveInstanceState?.let { it(p0, p1) }
    }

    override fun onActivityStopped(p0: Activity?) {
        onActivityStopped?.let { it(p0) }
    }

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
        onActivityCreated?.let { it(p0, p1) }
    }

}