package com.viet.mine.repository

import android.arch.lifecycle.LiveData
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.config.Config
import com.viet.news.core.domain.request.FeedBackParams
import com.viet.news.core.domain.request.ListParams
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

    fun getUserInfo(userId: String?): LiveData<Resource<UserInfoResponse>> {
        return object : NetworkOnlyResource<UserInfoResponse>() {
            override fun createCall(): LiveData<ApiResponse<UserInfoResponse>> {
                return apiInterface.getUserInfo(userId)
            }
        }.asLiveData()
    }
}