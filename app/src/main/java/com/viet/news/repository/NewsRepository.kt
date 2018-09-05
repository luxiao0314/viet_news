package com.viet.news.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.google.gson.Gson
import com.viet.news.BuildConfig
import com.viet.news.RateLimiter
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.api.ApiService
import com.viet.news.core.api.RetrofitManager
import com.viet.news.core.domain.response.ArticlesResponse
import com.viet.news.core.domain.response.NewsResponse
import com.viet.news.core.repository.NetworkBoundResource
import com.viet.news.core.repository.NetworkOnlyResource
import com.viet.news.core.ui.App
import com.viet.news.core.vo.Resource
import com.viet.news.db.NewsDBHelper
import com.viet.news.db.SourceEntity
import com.viet.news.rac.ui.model.SourceResponse
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit


/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Description
 */
public class NewsRepository constructor(private val apiInterface: ApiService = RetrofitManager.get().apiService()) {

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

    fun getMockNewsArticles(): LiveData<NewsResponse> {
        val live = MutableLiveData<NewsResponse>()
        val filepath = "virtualdata" + "/" + NewsResponse::class.java.simpleName
        val response = getJson(App.instance, filepath)
        val data = Gson().fromJson(response, NewsResponse::class.java)
        live.postValue(data)
        return live
    }

    fun getJson(context: Context, fileName: String): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(InputStreamReader(assetManager.open("$fileName.json"), "UTF-8"))
            var read: String? = null
            while ({ read = bf.readLine();read }() != null) {
                stringBuilder.append(read)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}