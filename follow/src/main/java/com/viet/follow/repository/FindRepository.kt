package com.viet.follow.repository

import android.arch.lifecycle.LiveData
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.api.HttpResponse
import com.viet.news.core.config.Config
import com.viet.news.core.domain.request.ListParams
import com.viet.news.core.domain.response.ChannelAllListResponse
import com.viet.news.core.domain.response.ChannelListResponse
import com.viet.news.core.domain.response.NewsListResponse
import com.viet.news.core.repository.NetworkOnlyResource
import com.viet.news.core.vo.Resource

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 2018/9/10 上午10:56
 * @Version
 */
class FindRepository : ApiRepository() {

    fun getlist4Channel(page_number: Int, id: String?): LiveData<Resource<NewsListResponse>> {
        val params = ListParams()
        params.page_number = page_number
        params.page_size = Config.page_size
        params.channel_id = id
        return object : NetworkOnlyResource<NewsListResponse>() {
            override fun createCall(): LiveData<ApiResponse<NewsListResponse>> = apiInterface.getlist4Channel(params)
        }.asLiveData()
    }

    fun getlist4Follow(page_number: Int, id: String?): LiveData<Resource<NewsListResponse>> {
        val params = ListParams()
        params.page_number = page_number
        params.page_size = Config.page_size
        params.user_id = id
        return object : NetworkOnlyResource<NewsListResponse>() {
            override fun createCall(): LiveData<ApiResponse<NewsListResponse>> = apiInterface.getlist4Follow(params)
        }.asLiveData()
    }

    fun getChannelAllList(): LiveData<Resource<ChannelAllListResponse>> {
        return object : NetworkOnlyResource<ChannelAllListResponse>() {
            override fun createCall(): LiveData<ApiResponse<ChannelAllListResponse>> = apiInterface.getChannelAllList()
        }.asLiveData()
    }

    fun getChannelList(): LiveData<Resource<ChannelListResponse>> {
        return object : NetworkOnlyResource<ChannelListResponse>() {
            override fun createCall(): LiveData<ApiResponse<ChannelListResponse>> = apiInterface.getChannelList()
        }.asLiveData()
    }

    fun channelAdd(id: String?): LiveData<Resource<HttpResponse<Any>>> {
        return object : NetworkOnlyResource<HttpResponse<Any>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<Any>>> = apiInterface.channelAdd(id)
        }.asLiveData()
    }

    fun channelRemove(id: String?): LiveData<Resource<HttpResponse<Any>>> {
        return object : NetworkOnlyResource<HttpResponse<Any>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<Any>>> = apiInterface.channelRemove(id)
        }.asLiveData()
    }

    fun favorite(contentId: String): LiveData<Resource<HttpResponse<Any>>> {
        return object : NetworkOnlyResource<HttpResponse<Any>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<Any>>> = apiInterface.collection(contentId)
        }.asLiveData()
    }

    fun like(contentId: String): LiveData<Resource<HttpResponse<Any>>> {
        return object : NetworkOnlyResource<HttpResponse<Any>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<Any>>> = apiInterface.like(contentId)
        }.asLiveData()
    }

}