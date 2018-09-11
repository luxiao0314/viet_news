package com.viet.news.core.http.interceptor

import com.viet.news.core.domain.User
import com.viet.news.core.utils.LanguageUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 2017/7/13 11:44 AM
 * @description
 */

class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val requestBuilder = request.newBuilder()
       requestBuilder.addHeader("Accept-Language", LanguageUtil.getHttpLanguageHeader())   // tsing 如果需要传递header指定接口返回语言类型，从这里下手
        requestBuilder.addHeader("os_type", "0")
        requestBuilder.addHeader("Authorization", User.currentUser.accessToken)

        return chain.proceed(requestBuilder.build())
    }
}