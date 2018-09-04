package com.viet.news.viewmodel

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.viet.news.core.ui.App
import com.viet.news.core.vo.Resource
import com.viet.news.db.SourceEntity
import com.viet.news.di.NewsApp
import com.viet.news.rac.ui.model.ArticlesResponse
import com.viet.news.repository.NewsRepository

/**
 * Created by abhinav.sharma on 01/11/17.
 */
class NewsViewModel constructor(var newsRepo: NewsRepository=NewsRepository()) : AndroidViewModel(App.instance) {

    fun getNewsSource(language: String?, category: String?, country: String?): LiveData<Resource<List<SourceEntity>>> {
        return newsRepo.fetchNewsSource(getApplication(), language, category, country)
    }

    fun getNewsArticles(source: String, sortBy: String?): LiveData<Resource<ArticlesResponse>> {
        return newsRepo.getNewsArticles(source, sortBy)
    }
}