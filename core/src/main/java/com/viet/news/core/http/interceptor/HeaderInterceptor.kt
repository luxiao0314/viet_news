package com.viet.news.core.http.interceptor

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

        requestBuilder.addHeader("os_type", "0")
        requestBuilder.addHeader("Content-Type", "application/json")


        return chain.proceed(requestBuilder.build())
    }
}