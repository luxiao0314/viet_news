package com.viet.mine.repository

import android.arch.lifecycle.LiveData
import com.safframework.utils.RxJavaUtils
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.api.HttpResponse
import com.viet.news.core.domain.request.FeedBackParams
import com.viet.news.core.domain.response.LoginRegisterResponse
import com.viet.news.core.repository.NetworkOnlyResource
import com.viet.news.core.vo.Resource
import io.reactivex.Maybe

/**
 * @Description
 * @author null
 * @date 2018/9/10
 * @Email zongjia.long@merculet.io
 * @Version
 */
class SettingRepository : ApiRepository() {
    fun feedBack(feedback: String): LiveData<Resource<Any>>{
        val params = FeedBackParams()
        params.feedback = feedback
        return object : NetworkOnlyResource<Any>(){
            override fun createCall(): LiveData<ApiResponse<Any>> = apiInterface.feedback(params)
        }.asLiveData()
    }

    fun updateNickName(nickname:String): LiveData<Resource<Any>>{
        return object : NetworkOnlyResource<Any>(){
            override fun createCall(): LiveData<ApiResponse<Any>> = apiInterface.updateNickName(nickname)
        }.asLiveData()
    }
}