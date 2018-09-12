package com.viet.mine.repository

import android.arch.lifecycle.LiveData
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.config.LoginEnum
import com.viet.news.core.config.VerifyCodeTypeEnum
import com.viet.news.core.domain.request.LoginParams
import com.viet.news.core.domain.request.RegisterParams
import com.viet.news.core.domain.request.VerifyCodeParams
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

    /**
     * 发送 验证码
     */
    fun sendSMS(phoneNumber: String?, zone_code: String?, enum: VerifyCodeTypeEnum): LiveData<Resource<Any>> {
        val params = VerifyCodeParams()
        params.phone_number = phoneNumber
        params.zone_code = zone_code
        params.setType(enum)
        return object : NetworkOnlyResource<Any>() {
            override fun createCall(): LiveData<ApiResponse<Any>> = apiInterface.sendSMS(params)
        }.asLiveData()
    }

    /**
     * 校验 验证码
     */
    fun checkVerifyCode(phoneNumber: String?, verifyCode: String?, zone_code: String?, type: VerifyCodeTypeEnum): LiveData<Resource<Any>> {
        val params = VerifyCodeParams()
        params.phone_number = phoneNumber
        params.validation_code = verifyCode
        params.zone_code = zone_code
        params.setType(type)
        return object : NetworkOnlyResource<Any>() {
            override fun createCall(): LiveData<ApiResponse<Any>> = apiInterface.checkVerifyCode(params)
        }.asLiveData()
    }

    /**
     * 注册
     */
    fun signIn(params:RegisterParams): LiveData<Resource<LoginRegisterResponse>> {
//        val params = RegisterParams()
//        params.phone_number = phoneNumber
//        params.zone_code = zone_code
//        params.setType(type)
        return object : NetworkOnlyResource<LoginRegisterResponse>() {
            override fun createCall(): LiveData<ApiResponse<LoginRegisterResponse>> = apiInterface.register(params)
        }.asLiveData()
    }
}