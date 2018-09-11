package com.viet.news.repository

import android.arch.lifecycle.LiveData
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.repository.NetworkOnlyResource
import com.viet.news.core.vo.Resource

/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Description
 */
class AppRepository : ApiRepository() {

    fun collection(contentId: String): LiveData<Resource<Any>> {
        return object : NetworkOnlyResource<Any>() {
            override fun createCall(): LiveData<ApiResponse<Any>> = apiInterface.collection(contentId)
        }.asLiveData()
    }

    fun like(contentId: String): LiveData<Resource<Any>> {
        return object : NetworkOnlyResource<Any>() {
            override fun createCall(): LiveData<ApiResponse<Any>> = apiInterface.like(contentId)
        }.asLiveData()
    }
}