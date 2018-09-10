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

//    fun getChannelList(): LiveData<Resource<List<ChannelListResponse>>> {
//        return object : NetworkOnlyResource<List<ChannelListResponse>>() {
//            override fun createCall(): LiveData<ApiResponse<List<ChannelListResponse>>> {
//                return apiInterface.getChannelList()
//            }
//        }.asLiveData()
//    }

    fun getChannelList(): Maybe<HttpResponse<List<ChannelListResponse>>> {
        return apiInterface.getChannelList().compose(RxJavaUtils.maybeToMain())
    }

}