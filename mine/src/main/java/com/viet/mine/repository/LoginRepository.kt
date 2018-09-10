package com.viet.mine.repository

import android.arch.lifecycle.LiveData
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.config.LoginEnum
import com.viet.news.core.domain.request.LoginParams
import com.viet.news.core.domain.response.LoginRegisterResponse
import com.viet.news.core.repository.NetworkOnlyResource
import com.viet.news.core.vo.Resource

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 2018/9/10 下午1:30
 * @Version
 */
class LoginRepository : ApiRepository() {

    /**
     * login_type:登录类型 1 手机号密码登录 2 手机号验证码登录 3 facebook登录
     */
    fun login(phoneNumber: String?, password: String?): LiveData<Resource<LoginRegisterResponse>> {
        val params = LoginParams()
        params.phone_number = phoneNumber
        params.password = password
        params.login_type = LoginEnum.PASSWORD.toString()
        return object : NetworkOnlyResource<LoginRegisterResponse>() {
            override fun createCall(): LiveData<ApiResponse<LoginRegisterResponse>> = apiInterface.login(params)
        }.asLiveData()
    }
}