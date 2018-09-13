package com.viet.mine.viewmodel

import android.arch.lifecycle.LiveData
import com.viet.mine.repository.MineRepository
import com.viet.news.core.api.HttpResponse
import com.viet.news.core.domain.response.CollectionListBean
import com.viet.news.core.domain.response.CollectionListResponse
import com.viet.news.core.viewmodel.BaseViewModel
import com.viet.news.core.vo.Resource

class CollectionViewModel(var repository: MineRepository = MineRepository()) : BaseViewModel() {
    var page_number = 0
    var collectionList = arrayListOf<CollectionListBean>()

    fun getCollectionList(userId:String): LiveData<Resource<HttpResponse<CollectionListResponse>>> {
        return repository.getCollectionList(page_number, userId)
    }

}