package com.viet.news.core.ext

import android.app.Activity
import android.support.v4.app.Fragment
import androidx.navigation.NavController
import com.chenenyu.router.Router
import com.viet.news.core.config.Config

/**
 * @Author Aaron
 * @Date 2018/8/21
 * @Email aaron@magicwindow.cn
 * @Description
 */

fun NavController.interceptor(interceptor: Interceptor): NavController {
    if (interceptor.skip()) {
        navigate(interceptor.getResId())
    }
    return this
}

/**
 * 跳转到MainActivity
 * 由于MainActivity是SingleTask的 因此会关闭上层其他界面
 */
fun Fragment.routerToMain() = Router.build(Config.ROUTER_MAIN_ACTIVITY)
        .anim(0, 0)//不使用跳转动画
        .go(activity)

/**
 * 跳转到MainActivity
 * 由于MainActivity是SingleTask的 因此会关闭上层其他界面
 */
fun Activity.routerToMain() = Router.build(Config.ROUTER_MAIN_ACTIVITY)
        .anim(0, 0)//不使用跳转动画
        .go(this)