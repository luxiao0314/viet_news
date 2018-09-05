package com.viet.mine.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.viet.news.core.domain.response.NewsResponse
import com.viet.news.core.utils.FileUtils
import com.viet.news.core.viewmodel.BaseViewModel

class CollectionViewModel() : BaseViewModel() {
    fun getNewsArticles(): LiveData<NewsResponse> {
        return FileUtils.handleVirtualData(NewsResponse::class.java)
    }
}