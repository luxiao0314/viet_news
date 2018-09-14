package com.viet.follow.repository

import android.arch.lifecycle.LiveData
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.api.HttpResponse
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
class PersonalPageRepository: ApiRepository() {

    fun getlist4User(page_number: Int, id: String?): LiveData<Resource<HttpResponse<NewsListResponse>>> {
        val params = ListParams()
        params.page_number = page_number
        params.page_size = Config.page_size
        params.user_id = id
        return object : NetworkOnlyResource<HttpResponse<NewsListResponse>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<NewsListResponse>>> {
                return apiInterface.getlist4User(params)
            }
        }.asLiveData()
    }

    fun getUserInfo(userId: String?): LiveData<Resource<HttpResponse<UserInfoResponse>>> {
        return object : NetworkOnlyResource<HttpResponse<UserInfoResponse>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<UserInfoResponse>>> {
                return apiInterface.getUserInfo(userId)
            }
        }.asLiveData()
    }

    fun follow(followUserId: String?): LiveData<Resource<HttpResponse<Any>>> {
        return object : NetworkOnlyResource<HttpResponse<Any>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<Any>>> {
                return apiInterface.follow(followUserId)
            }
        }.asLiveData()
    }

    fun collection(contentId: String): LiveData<Resource<HttpResponse<Int>>> {
        return object : NetworkOnlyResource<HttpResponse<Int>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<Int>>> = apiInterface.collection(contentId)
        }.asLiveData()
    }

    fun like(contentId: String): LiveData<Resource<HttpResponse<Int>>> {
        return object : NetworkOnlyResource<HttpResponse<Int>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<Int>>> = apiInterface.like(contentId)
        }.asLiveData()
    }

}