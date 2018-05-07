package com.library.aaron.rac.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.content.Context
import com.library.aaron.rac.db.SourceEntity
import com.library.aaron.rac.ui.model.ArticlesResponse
import com.library.aaron.rac.repository.NewsRepository
import com.library.aaron.core.vo.Resource

/**
 * Created by abhinav.sharma on 01/11/17.
 */
class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val newsRepo: NewsRepository = NewsRepository()
    val context: Context = application.applicationContext

    fun getNewsSource(language: String?, category: String?, country: String?): LiveData<Resource<List<SourceEntity>>> {
        return newsRepo.fetchNewsSource(context, language, category, country)
    }

    fun getNewsArticles(source: String, sortBy: String?): LiveData<Resource<ArticlesResponse>> {
        return newsRepo.getNewsArticles(source, sortBy)
    }
}