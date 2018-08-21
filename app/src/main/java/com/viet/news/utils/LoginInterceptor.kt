package com.viet.news.utils

import com.viet.news.R
import com.viet.news.core.ext.Interceptor

/**
 * @Author Aaron
 * @Date 2018/8/21
 * @Email aaron@magicwindow.cn
 * @Description
 */
class LoginInterceptor : Interceptor() {
    override fun getResId(): Int = R.id.action_page1

    override fun skip(): Boolean = false

}