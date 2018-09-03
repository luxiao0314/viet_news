package com.viet.news.core.dsl

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.OnTabSelectListener

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 11:36 AM
 * @Version
 */
fun ViewPager.setOnPageChangeListener(init: PageChangeWrapper.() -> Unit) {
    val pageChangeDelegate = PageChangeWrapper()
    pageChangeDelegate.init()
    setOnPageChangeListener(pageChangeDelegate)
}

class PageChangeWrapper : ViewPager.OnPageChangeListener {

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

fun ViewPager.addOnPageChangeListener(init: AddOnPageChangeListenerWrapper.() -> Unit) {
    val callback = AddOnPageChangeListenerWrapper()
    callback.init()
    addOnPageChangeListener(callback)
}

class AddOnPageChangeListenerWrapper : ViewPager.OnPageChangeListener {

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

fun CommonTabLayout.setOnTabSelectListener(init: OnTabSelectListenerWrapper.() -> Unit) {
    val callback = OnTabSelectListenerWrapper()
    callback.init()
    setOnTabSelectListener(callback)
}

class OnTabSelectListenerWrapper: OnTabSelectListener{

    var onTabSelect: ((position: Int) -> Unit)? = null
    var onTabReselect: ((position: Int) -> Unit)? = null

    override fun onTabSelect(position: Int) {
        onTabSelect?.let { it(position) }
    }

    override fun onTabReselect(position: Int) {
        onTabReselect?.let { it(position) }
    }
}