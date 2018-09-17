package com.viet.mine.repository

import android.arch.lifecycle.LiveData
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.config.Config
import com.viet.news.core.config.ContentType
import com.viet.news.core.config.VerifyCodeTypeEnum
import com.viet.news.core.domain.request.*
import com.viet.news.core.domain.response.NewsListResponse
import com.viet.news.core.domain.response.UserInfoResponse
import com.viet.news.core.repository.NetworkOnlyResource
import com.viet.news.core.vo.Resource

/**
 * @Description
 * @author null
 * @date 2018/9/10
 * @Email zongjia.long@merculet.io
 * @Version
 */
class MineRepository : ApiRepository() {
    fun feedBack(feedback: String): LiveData<Resource<Any>> {
        val params = FeedBackParams()
        params.feedback = feedback
        return object : NetworkOnlyResource<Any>() {
            override fun createCall(): LiveData<ApiResponse<Any>> = apiInterface.feedback(params)
        }.asLiveData()
    }

    fun updateNickName(nickname: String): LiveData<Resource<Any>> {
        return object : NetworkOnlyResource<Any>() {
            override fun createCall(): LiveData<ApiResponse<Any>> = apiInterface.updateNickName(nickname)
        }.asLiveData()
    }

    fun getCollectionList(page: Int, id: String?): LiveData<Resource<NewsListResponse>> {
        val params = ListParams()
        params.page_number = page
        params.page_size = Config.page_size
        params.user_id = id
        return object : NetworkOnlyResource<NewsListResponse>() {
            override fun createCall(): LiveData<ApiResponse<NewsListResponse>> = apiInterface.getCollectionList(params)
        }.asLiveData()
    }

    fun uploadFile(path: String): LiveData<Resource<Any>> {
        return object : NetworkOnlyResource<Any>() {
            override fun createCall(): LiveData<ApiResponse<Any>> {
                return apiInterface.uploadFile(ContentType.getPart(path))
            }
        }.asLiveData()
    }

    fun getUserInfo(userId: String?): LiveData<Resource<UserInfoResponse>> {
        return object : NetworkOnlyResource<UserInfoResponse>() {
            override fun createCall(): LiveData<ApiResponse<UserInfoResponse>> {
                return apiInterface.getUserInfo(userId)
            }
        }.asLiveData()
    }

    /**
     * 【发送】 验证码
     */
    fun sendSMS(phoneNumber: String?, zone_code: String?, type: VerifyCodeTypeEnum): LiveData<Resource<Any>> {
        val params = VerifyCodeParams()
        params.phone_number = phoneNumber
        params.zone_code = zone_code
        params.setType(type)
        return object : NetworkOnlyResource<Any>() {
            override fun createCall(): LiveData<ApiResponse<Any>> = apiInterface.sendSMS(params)
        }.asLiveData()
    }

    /**
     * 【校验】 验证码
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
     * 修改密码
     */
    fun resetPwdWithOldPwd(oldPwd: String, newPwd: String): LiveData<Resource<Any>> {
        val params = ResetPwdWithOldpwdParams()
        params.new_password = newPwd
        params.old_password = oldPwd
        return object : NetworkOnlyResource<Any>() {
            override fun createCall(): LiveData<ApiResponse<Any>> {
                return apiInterface.resetPwdWithOldPwd(params)
            }
        }.asLiveData()
    }

    /**
     * 修改手机号
     */
    fun resetPhoneNum(newPhoneNum:String,oldPhoneNum:String,newVrifyCode:String,oldVerifyCode:String): LiveData<Resource<Any>>{
        val params = ResetPhoneNumParams()
        params.new_phone_number = newPhoneNum
        params.old_phone_number  = oldPhoneNum
        params.new_validation_code = newVrifyCode
        params.old_validation_code = oldVerifyCode
        return object : NetworkOnlyResource<Any>() {
            override fun createCall(): LiveData<ApiResponse<Any>> {
                return apiInterface.resetPwdWithOldPwd(params)
            }
        }.asLiveData()
    }

}