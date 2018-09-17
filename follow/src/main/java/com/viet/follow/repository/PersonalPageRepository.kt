package com.viet.follow.repository

import android.arch.lifecycle.LiveData
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.config.Config
import com.viet.news.core.domain.request.ListParams
import com.viet.news.core.domain.response.NewsListResponse
import com.viet.news.core.domain.response.UserInfoResponse
import com.viet.news.core.repository.NetworkOnlyResource
import com.viet.news.core.vo.Resource

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 12/09/2018 11:51 AM
 * @Version
 */
class PersonalPageRepository : ApiRepository() {

    fun getlist4User(page_number: Int, id: String?): LiveData<Resource<NewsListResponse>> {
        val params = ListParams()
        params.page_number = page_number
        params.page_size = Config.page_size
        params.user_id = id
        return object : NetworkOnlyResource<NewsListResponse>() {
            override fun createCall(): LiveData<ApiResponse<NewsListResponse>> = apiInterface.getlist4User(params)
        }.asLiveData()
    }

    fun getUserInfo(userId: String?): LiveData<Resource<UserInfoResponse>> {
        return object : NetworkOnlyResource<UserInfoResponse>() {
            override fun createCall(): LiveData<ApiResponse<UserInfoResponse>> = apiInterface.getUserInfo(userId)
        }.asLiveData()
    }

    fun follow(followUserId: String?): LiveData<Resource<Any>> {
        return object : NetworkOnlyResource<Any>() {
            override fun createCall(): LiveData<ApiResponse<Any>> = apiInterface.follow(followUserId)
        }.asLiveData()
    }

    fun cancelfollow(followUserId: String?): LiveData<Resource<Any>> {
        return object : NetworkOnlyResource<Any>() {
            override fun createCall(): LiveData<ApiResponse<Any>> = apiInterface.cancelfollow(followUserId)
        }.asLiveData()
    }

    fun collection(contentId: String): LiveData<Resource<Int>> {
        return object : NetworkOnlyResource<Int>() {
            override fun createCall(): LiveData<ApiResponse<Int>> = apiInterface.collection(contentId)
        }.asLiveData()
    }

    fun like(contentId: String): LiveData<Resource<Int>> {
        return object : NetworkOnlyResource<Int>() {
            override fun createCall(): LiveData<ApiResponse<Int>> = apiInterface.like(contentId)
        }.asLiveData()
    }
}