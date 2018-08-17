package com.viet.news.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.viet.news.db.SourceEntity
import com.viet.news.rac.ui.model.ArticlesResponse
import com.viet.news.repository.NewsRepository
import com.viet.news.core.vo.Resource

/**
 * Created by abhinav.sharma on 01/11/17.
 */
class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val newsRepo: NewsRepository = NewsRepository()

    fun getNewsSource(language: String?, category: String?, country: String?): LiveData<Resource<List<SourceEntity>>> {
        return newsRepo.fetchNewsSource(getApplication(), language, category, country)
    }

    fun getNewsArticles(source: String, sortBy: String?): LiveData<Resource<ArticlesResponse>> {
        return newsRepo.getNewsArticles(source, sortBy)
    }
}