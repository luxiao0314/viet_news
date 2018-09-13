package com.viet.mine.repository

import android.arch.lifecycle.LiveData
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.api.HttpResponse
import com.viet.news.core.config.Config
import com.viet.news.core.config.ContentType
import com.viet.news.core.domain.request.FeedBackParams
import com.viet.news.core.domain.request.ListParams
import com.viet.news.core.domain.response.CollectionListResponse
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
    fun feedBack(feedback: String): LiveData<Resource<HttpResponse<Any>>> {
        val params = FeedBackParams()
        params.feedback = feedback
        return object : NetworkOnlyResource<HttpResponse<Any>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<Any>>> = apiInterface.feedback(params)
        }.asLiveData()
    }

    fun updateNickName(nickname: String): LiveData<Resource<HttpResponse<Any>>> {
        return object : NetworkOnlyResource<HttpResponse<Any>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<Any>>> = apiInterface.updateNickName(nickname)
        }.asLiveData()
    }

    fun getCollectionList(page: Int, id: String?): LiveData<Resource<HttpResponse<CollectionListResponse>>> {
        val params = ListParams()
        params.page_number = page
        params.page_size = Config.page_size
        params.user_id = id
        return object : NetworkOnlyResource<HttpResponse<CollectionListResponse>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<CollectionListResponse>>> = apiInterface.getCollectionList(params)
        }.asLiveData()
    }

    fun uploadFile(path: String): LiveData<Resource<HttpResponse<Any>>> {
        return object : NetworkOnlyResource<HttpResponse<Any>>() {
            override fun createCall(): LiveData<ApiResponse<HttpResponse<Any>>> {
                return apiInterface.uploadFile(ContentType.getPart(path))
            }
        }.asLiveData()
    }
}