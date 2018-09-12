package com.viet.news.core.http.interceptor

import com.viet.news.core.api.RetrofitManager
import com.viet.news.core.config.LoginEnum
import com.viet.news.core.domain.User
import com.viet.news.core.domain.request.LoginParams
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException

class RefreshTokenAuthenticator : Authenticator {
    @Throws(IOException::class)
    override fun authenticate(route: Route, response: Response): Request? {
        val param = LoginParams()
        param.setType(LoginEnum.HARDWARE)
        val data = RetrofitManager.get().apiService()
                .logins(param)
                .execute()
                .body()
        val token = data?.data?.data?.token.toString()
        User.currentUser.accessToken = token
//        val credential = Credentials.basic("jesse", "password1")
        return response.request().newBuilder()
                .addHeader("Authorization", token)
                .build()
    }
}
