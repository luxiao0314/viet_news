package com.viet.mine.repository

import android.arch.lifecycle.LiveData
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.config.Config
import com.viet.news.core.config.ContentType
import com.viet.news.core.config.VerifyCodeTypeEnum
import com.viet.news.core.domain.request.FeedBackParams
import com.viet.news.core.domain.request.ListParams
import com.viet.news.core.domain.request.VerifyCodeParams
import com.viet.news.core.domain.response.CollectionListResponse
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

    fun getCollectionList(page: Int, id: String?): LiveData<Resource<CollectionListResponse>> {
        val params = ListParams()
        params.page_number = page
        params.page_size = Config.page_size
        params.user_id = id
        return object : NetworkOnlyResource<CollectionListResponse>() {
            override fun createCall(): LiveData<ApiResponse<CollectionListResponse>> = apiInterface.getCollectionList(params)
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
    fun checkVerifyCode(phoneNumber: String?, verifyCode: String?, zone_code: String?, type: VerifyCodeTypeEnum): LiveData<Resource< Any>> {
        val params = VerifyCodeParams()
        params.phone_number = phoneNumber
        params.validation_code = verifyCode
        params.zone_code = zone_code
        params.setType(type)
        return object : NetworkOnlyResource< Any>() {
            override fun createCall(): LiveData<ApiResponse< Any>> = apiInterface.checkVerifyCode(params)
        }.asLiveData()
    }

}