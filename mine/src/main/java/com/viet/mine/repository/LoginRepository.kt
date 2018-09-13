package com.viet.mine.repository

import android.arch.lifecycle.LiveData
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.api.HttpResponse
import com.viet.news.core.config.LoginEnum
import com.viet.news.core.config.VerifyCodeTypeEnum
import com.viet.news.core.domain.request.LoginParams
import com.viet.news.core.domain.request.ResetPwdParams
import com.viet.news.core.domain.request.SignInParams
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
    fun loginByPwd(phoneNumber: String?, password: String?): LiveData<Resource<HttpResponse<LoginRegisterResponse>>> {
        val params = LoginParams()
        params.phone_number = phoneNumber
        params.password = password
        params.setType(LoginEnum.PASSWORD)
        return object : NetworkOnlyResource<HttpResponse<LoginRegisterResponse>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<LoginRegisterResponse>>> = apiInterface.login(params)
        }.asLiveData()
    }

    /**
     * login_type:登录类型 1 手机号密码登录 2 手机号验证码登录 3 facebook登录
     */
    fun loginBySMS(phoneNumber: String?, vCode: String?): LiveData<Resource<HttpResponse<LoginRegisterResponse>>> {
        val params = LoginParams()
        params.phone_number = phoneNumber
        params.validation_code = vCode
        params.setType(LoginEnum.VALIDATION_CODE)
        return object : NetworkOnlyResource<HttpResponse<LoginRegisterResponse>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<LoginRegisterResponse>>> = apiInterface.login(params)
        }.asLiveData()
    }

    /**
     * login_type:登录类型 1 手机号密码登录 2 手机号验证码登录 3 facebook登录
     */
    fun loginByFacebook(phoneNumber: String?, password: String?): LiveData<Resource<HttpResponse<LoginRegisterResponse>>> {
        val params = LoginParams()
        params.phone_number = phoneNumber
        //TODO tsing Facebook 传入登录相关信息
        params.setType(LoginEnum.FACEBOOK)
        return object : NetworkOnlyResource<HttpResponse<LoginRegisterResponse>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<LoginRegisterResponse>>> = apiInterface.login(params)
        }.asLiveData()
    }

    /**
     * 【发送】 验证码
     */
    fun sendSMS(phoneNumber: String?, zone_code: String?, type: VerifyCodeTypeEnum): LiveData<Resource<HttpResponse<Any>>> {
        val params = VerifyCodeParams()
        params.phone_number = phoneNumber
        params.zone_code = zone_code
        params.setType(type)
        return object : NetworkOnlyResource<HttpResponse<Any>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<Any>>> = apiInterface.sendSMS(params)
        }.asLiveData()
    }

    /**
     * 【校验】 验证码
     */
    fun checkVerifyCode(phoneNumber: String?, verifyCode: String?, zone_code: String?, type: VerifyCodeTypeEnum): LiveData<Resource<HttpResponse<Any>>> {
        val params = VerifyCodeParams()
        params.phone_number = phoneNumber
        params.validation_code = verifyCode
        params.zone_code = zone_code
        params.setType(type)
        return object : NetworkOnlyResource<HttpResponse<Any>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<Any>>> = apiInterface.checkVerifyCode(params)
        }.asLiveData()
    }

    /**
     * 注册
     */
    fun signIn(params: SignInParams): LiveData<Resource<HttpResponse<LoginRegisterResponse>>> =
            object : NetworkOnlyResource<HttpResponse<LoginRegisterResponse>>() {
                override fun createCall(): LiveData<ApiResponse<HttpResponse<LoginRegisterResponse>>> = apiInterface.register(params)
            }.asLiveData()
    /**
     * 设置密码
     */
    fun setPassword(phoneNumber: String?, verifyCode: String?, password: String?): LiveData<Resource<HttpResponse<LoginRegisterResponse>>> {
        val params = ResetPwdParams()
        params.phone_number = phoneNumber
        params.validation_code = verifyCode
        params.password = password
        return object : NetworkOnlyResource<HttpResponse<LoginRegisterResponse>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<LoginRegisterResponse>>> = apiInterface.setPassword(params)
        }.asLiveData()
    }
}