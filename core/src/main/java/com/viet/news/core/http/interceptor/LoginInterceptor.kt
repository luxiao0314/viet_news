package com.viet.news.core.http.interceptor

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.chenenyu.router.RouteInterceptor
import com.chenenyu.router.RouteResponse
import com.chenenyu.router.annotation.Interceptor
import com.viet.news.core.config.Config
import com.viet.news.core.domain.User
import com.viet.news.dialog.LoginDialog

@Interceptor(Config.LOGIN_INTERCEPTOR)
class LoginInterceptor : RouteInterceptor {

    override fun intercept(chain: RouteInterceptor.Chain?): RouteResponse =
            if (User.currentUser.isLogin()) {
                chain!!.process()
            } else {
                val context = chain?.context
                if (context is FragmentActivity) {
                    LoginDialog.create(context)
                } else if (context is Fragment) {
                    LoginDialog.create(context.activity!!)
                }
                chain!!.intercept()    //true表示拦截
            }

}