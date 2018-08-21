package com.viet.news.core.ext

import androidx.navigation.NavController

/**
 * @Author Aaron
 * @Date 2018/8/21
 * @Email aaron@magicwindow.cn
 * @Description
 */

fun NavController.interceptor(interceptor: Interceptor): NavController {
    if (interceptor.skip()){
        navigate(interceptor.getResId())
    }
    return this
}