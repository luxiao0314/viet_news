package com.viet.news.core.http.interceptor

import com.chenenyu.router.RouteInterceptor
import com.chenenyu.router.RouteResponse
import com.chenenyu.router.annotation.Interceptor
import com.viet.news.core.R
import com.viet.news.core.config.Config
import com.viet.news.core.config.IActivityManager
import com.viet.news.core.domain.User
import com.viet.news.core.ext.routerWithAnim

@Interceptor(Config.LOGIN_INTERCEPTOR)
class RouterLoginInterceptor : RouteInterceptor {

    override fun intercept(chain: RouteInterceptor.Chain?): RouteResponse =
            if (User.currentUser.isLogin()) {
                chain!!.process()
            } else {
                routerWithAnim(Config.ROUTER_LOGIN_ACTIVITY).anim(R.anim.activity_open,android.R.anim.fade_out).go(IActivityManager.lastActivity())
                chain!!.intercept()    //true表示拦截
            }
}