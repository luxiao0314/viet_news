package com.viet.news.core.http.interceptor

import com.viet.news.core.config.Config
import com.viet.news.core.domain.GlobalNetworkException
import com.viet.news.core.utils.RxBus
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject

/***
 * 监听异常事件
 *
 */
class NetworkExceptionInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.code() == Config.NETWORK_OK) {

            val responseBody = response.body()
            val contentType = responseBody!!.contentType()

            val bodyString = responseBody.string()
            if (bodyString.startsWith("{")) {
                val json = JSONObject(bodyString)
                val code = json.optInt("code")
                if (code != Config.NETWORK_RESPONSE_OK) {
                    RxBus.get().post(GlobalNetworkException(code, bodyString))
                }
            }
            //深坑！ responseBody.string()后原ResponseBody会被清空，需要重新设置body
            val body = ResponseBody.create(contentType, bodyString)
            return response.newBuilder().body(body).build()
        }

        return response
    }
}