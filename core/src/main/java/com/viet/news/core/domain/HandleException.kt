package com.viet.news.core.domain

import android.net.ParseException
import com.google.gson.JsonParseException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.cert.CertPathValidatorException
import javax.net.ssl.SSLHandshakeException


/**
 * @Description 服务器异常,http异常处理
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 26/09/2018 3:32 PM
 * @Version
 */
class HandleException {

    companion object {

        private const val ACCESS_DENIED = 302
        const val UNAUTHORIZED = 401    //未认证
        const val FORBIDDEN = 403   //被禁止
        private const val NOT_FOUND = 404
        private const val REQUEST_TIMEOUT = 408
        private const val INTERNAL_SERVER_ERROR = 500
        private const val BAD_GATEWAY = 502
        private const val SERVICE_UNAVAILABLE = 503
        private const val GATEWAY_TIMEOUT = 504

        //服务器异常状态码
        private const val SERVICE_EXCEPTION = 1000

        fun handle(e: Throwable): Throwable {
            return when (e) {
                is HttpException -> when (e.code()) {
                    UNAUTHORIZED -> Throwable("token过期", e)
                    FORBIDDEN -> Throwable("请先登录", e)
                    NOT_FOUND -> Throwable("HTTP NOT FOUND", e)
                    REQUEST_TIMEOUT -> Throwable("请求超时", e)
                    GATEWAY_TIMEOUT -> Throwable("网关超时")
                    INTERNAL_SERVER_ERROR -> Throwable("内部服务器错误", e)
                    BAD_GATEWAY -> Throwable("无效网关", e)
                    SERVICE_UNAVAILABLE -> Throwable("找不到服务器", e)
                    ACCESS_DENIED -> Throwable("拒绝访问", e)
                    else -> Throwable("网络错误", e)
                }
                is ServiceException -> when (e.code) {
                    SERVICE_EXCEPTION -> Throwable(e.message, e)
                    else -> Throwable("服务器异常", e)
                }
                is JsonParseException, is JSONException, is ParseException -> Throwable("解析错误", e)
                is ConnectException -> Throwable("连接失败", e)
                is UnknownHostException -> Throwable("网络连接失败", e)
                is SSLHandshakeException -> Throwable("证书验证失败", e)
                is CertPathValidatorException -> Throwable("证书路径没找到", e)
                is ConnectTimeoutException, is SocketTimeoutException -> Throwable("连接超时", e)
                is ClassCastException -> Throwable("类型转换出错", e)
                is NullPointerException -> Throwable("数据有空", e)
                else -> Throwable(e.message, e)
            }
        }
    }
}

