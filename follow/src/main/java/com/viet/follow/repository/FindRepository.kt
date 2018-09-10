package com.viet.follow.repository

import com.safframework.utils.RxJavaUtils
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.HttpResponse
import com.viet.news.core.domain.response.ChannelListResponse
import io.reactivex.Maybe

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 2018/9/10 上午10:56
 * @Version
 */
class FindRepository : ApiRepository() {

//    fun getChannelAllList(): LiveData<Resource<List<ChannelListResponse>>> {
//        return object : NetworkOnlyResource<List<ChannelListResponse>>() {
//            override fun createCall(): LiveData<ApiResponse<List<ChannelListResponse>>> {
//                return apiInterface.getChannelAllList()
//            }
//        }.asLiveData()
//    }

    fun getChannelAllList(): Maybe<HttpResponse<List<ChannelListResponse>>> {
        return apiInterface.getChannelAllList().compose(RxJavaUtils.maybeToMain())
    }

}