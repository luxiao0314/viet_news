package com.viet.follow.repository

import android.arch.lifecycle.LiveData
import com.safframework.utils.RxJavaUtils
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.api.HttpResponse
import com.viet.news.core.domain.response.ChannelListResponse
import com.viet.news.core.repository.NetworkOnlyResource
import com.viet.news.core.vo.Resource
import io.reactivex.Maybe

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 2018/9/10 上午10:56
 * @Version
 */
class FindRepository : ApiRepository() {

    fun getChannelAllList(): LiveData<Resource<ChannelListResponse>> {
        return object : NetworkOnlyResource<ChannelListResponse>() {
            override fun createCall(): LiveData<ApiResponse<ChannelListResponse>> {
                return apiInterface.getChannelAllList()
            }
        }.asLiveData()
    }

    fun getChannelList(): LiveData<Resource<ChannelListResponse>> {
        return object : NetworkOnlyResource<ChannelListResponse>() {
            override fun createCall(): LiveData<ApiResponse<ChannelListResponse>> {
                return apiInterface.getChannelList()
            }
        }.asLiveData()
    }

}