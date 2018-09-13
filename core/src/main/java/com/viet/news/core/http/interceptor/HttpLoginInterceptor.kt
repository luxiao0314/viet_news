package com.viet.news.core.http.interceptor

import android.annotation.SuppressLint
import com.viet.news.core.R
import com.viet.news.core.config.Config
import com.viet.news.core.config.IActivityManager
import com.viet.news.core.ext.routerWithAnim
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

/**
 * @Description 登录权限
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 07/05/2018 11:22 AM
 * @Version
 */
class HttpLoginInterceptor : Interceptor {

    var isLoginInvalidate: Boolean = false

    @SuppressLint("CheckResult")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val response = chain.proceed(chain.request())
        return when {
            response.code() == Config.ErrorCode.NETWORK_RESPONSE_LOGIN_FORBIDDEN -> {
                if (!isLoginInvalidate) {
                    isLoginInvalidate = true
                    routerWithAnim(Config.ROUTER_LOGIN_ACTIVITY).anim(R.anim.dialog_push_bottom_in, R.anim.dialog_push_bottom_out).go(IActivityManager.lastActivity())
                }
                isLoginInvalidate = false
                response
            }
            else -> {
                isLoginInvalidate = false
                response.newBuilder()
                        .body(ResponseBody.create(response.body()?.contentType(), response.body()?.string()))
                        .build()
            }
        }
    }

}