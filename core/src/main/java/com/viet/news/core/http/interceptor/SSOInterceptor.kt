package com.viet.news.core.http.interceptor

import com.google.gson.Gson
import com.viet.news.core.api.HttpResponse
import com.viet.news.core.api.RetrofitManager
import com.viet.news.core.config.Config
import com.viet.news.core.domain.User
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

/**
 * @Description 单点登录
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 07/05/2018 11:22 AM
 * @Version
 */
class SSOInterceptor : Interceptor {

    var isLoginInvalidate: Boolean = false

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val response = chain.proceed(chain.request())
        val responseBody = response.body()
        val bodyString = responseBody!!.string()
        val httpResponse = Gson().fromJson(bodyString, HttpResponse::class.java)
        return if (httpResponse?.code == Config.ErrorCode.NETWORK_RESPONSE_LOGIN_INVALIDATE) {
            if (!isLoginInvalidate) {
                isLoginInvalidate = true
                User.currentUser.logout()//首页会接受登出时间 然后刷新界面

//                IActivityManager.lastActivity()?.let { currentActivity ->
//                    val router = routerWithAnim(Config.ROUTER_MAIN_ACTIVITY)
//                    val descClassName = router.getIntent(currentActivity).component.className
//                    val currClassName: String = currentActivity.javaClass.name
//                    if (descClassName != currClassName) {
//                        router.anim(0, 0)
//                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                .go(currentActivity)
//                    }
//                }
//                Observable.timer(600, TimeUnit.MILLISECONDS)
//                        .subscribe {
//                            (IActivityManager.lastActivity() as FragmentActivity).showCusDialog(DeviceOfflineDialog())
//                        }
                RetrofitManager.get().okHttpClient().dispatcher().cancelAll()
            }
            response
        } else {
            isLoginInvalidate = false
            response.newBuilder()
                    .body(ResponseBody.create(responseBody.contentType(), bodyString))
                    .build()
        }
    }

}