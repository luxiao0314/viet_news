package com.viet.news.repository

import android.arch.lifecycle.LiveData
import android.content.Context
import com.viet.news.BuildConfig
import com.viet.news.RateLimiter
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.domain.response.ArticlesResponse
import com.viet.news.core.repository.NetworkBoundResource
import com.viet.news.core.repository.NetworkOnlyResource
import com.viet.news.core.vo.Resource
import com.viet.news.db.NewsDBHelper
import com.viet.news.db.SourceEntity
import com.viet.news.rac.ui.model.SourceResponse
import java.util.concurrent.TimeUnit


/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Description
 */
public class NewsRepository : ApiRepository() {

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