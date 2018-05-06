package com.library.aaron.module.repository

import android.arch.lifecycle.LiveData
import android.content.Context
import com.library.aaron.module.db.NewsDBHelper
import com.library.aaron.module.db.SourceEntity
import com.library.aaron.module.ui.model.ArticlesResponse
import com.library.aaron.module.ui.model.SourceResponse
import com.library.aaron.core.api.ApiResponse
import com.library.aaron.core.api.ApiService
import com.library.aaron.core.api.RetrofitManager
import com.library.aaron.core.repository.NetworkBoundResource
import com.library.aaron.core.repository.NetworkOnlyResource
import com.library.aaron.core.vo.Resource
import com.library.aaron.module.BuildConfig
import com.library.aaron.module.RateLimiter
import java.util.concurrent.TimeUnit

/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Description
 */
class NewsRepository(private val apiInterface: ApiService = RetrofitManager.get().apiService()) {

    val repoRateLimiter = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun fetchNewsSource(context: Context, language: String?, category: String?, country: String?): LiveData<Resource<List<SourceEntity>>> {
        return object : NetworkBoundResource<List<SourceEntity>, SourceResponse>() {
            override fun onFetchFailed() {
                repoRateLimiter.reset("all")
            }

            override fun saveCallResult(item: SourceResponse) {
//                To avoid this make API response pojo class as entity
                val sourceList = ArrayList<SourceEntity>()
                item.sources.forEach {
                    val sourceEntity = SourceEntity()
                    sourceEntity.id = it.id
                    sourceEntity.category = it.category
                    sourceEntity.country = it.country
                    sourceEntity.description = it.description
                    sourceEntity.language = it.language
                    sourceEntity.name = it.name
                    sourceEntity.url = it.url
                    sourceList.add(sourceEntity)
                }
                NewsDBHelper.getInstance(context).getSourceDao().insertSources(sourceList)
            }

            override fun shouldFetch(data: List<SourceEntity>?): Boolean = repoRateLimiter.shouldFetch("all")

            override fun loadFromDb(): LiveData<List<SourceEntity>> {
                return NewsDBHelper.getInstance(context).getSourceDao().getAllNewsSource()
            }

            override fun createCall(): LiveData<ApiResponse<SourceResponse>> {
                return apiInterface.getSources(language, category, country)
            }
        }.asLiveData()
    }

    fun getNewsArticles(source: String, sortBy: String?): LiveData<Resource<ArticlesResponse>> {
        return object : NetworkOnlyResource<ArticlesResponse>() {
            override fun createCall(): LiveData<ApiResponse<ArticlesResponse>> {
                return apiInterface.getArticles(source, sortBy, BuildConfig.API_KEY)
            }

        }.asLiveData()
    }
}