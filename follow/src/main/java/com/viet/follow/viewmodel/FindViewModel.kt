package com.viet.follow.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import cn.magicwindow.channelwidget.entity.ChannelBean
import com.viet.follow.repository.FindRepository
import com.viet.news.core.domain.response.NewsListBean
import com.viet.news.core.domain.response.NewsListResponse
import com.viet.news.core.viewmodel.BaseViewModel
import com.viet.news.core.vo.Resource

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 1:37 PM
 * @Version
 */
class FindViewModel(var repository: FindRepository = FindRepository()) : BaseViewModel() {
    var normalList = arrayListOf<ChannelBean>()
    var followList = arrayListOf<ChannelBean>()
    var unFollowList = arrayListOf<ChannelBean>()
    var newsList = arrayListOf<NewsListBean>()
    var id: String? = "1"

    fun getlist4Channel(id: String?, page_number: Int): LiveData<Resource<NewsListResponse>> {
        return repository.getlist4Channel(page_number, id)
    }

    fun getlist4Follow(page_number: Int): LiveData<Resource<NewsListResponse>> {
        return repository.getlist4Follow(page_number, id)
    }

    fun getChannelList(owner: LifecycleOwner, function: () -> Unit) {
        repository.getChannelList().observe(owner, Observer { it ->
            it?.work {
                it.data?.forEach { normalList.add(ChannelBean(it.channelName, it.id, 2)) }
                function()
            }
        })
    }

    fun getChannelAllList(owner: LifecycleOwner) {
        repository.getChannelAllList().observe(owner, Observer { it ->
            it?.work {
                unFollowList.clear()
                followList.clear()
                it.data?.unFollowChannelList?.forEach { unFollowList.add(ChannelBean(it.channelName, it.id, 2)) }
                it.data?.followChannelList?.forEachIndexed { index, list ->
                    if (index == 0) {
                        followList.add(ChannelBean(list.channelName, list.id, 0))
                    } else {
                        followList.add(ChannelBean(list.channelName, list.id, 2))
                    }
                }
            }
        })
    }

    fun channelAdd(owner: LifecycleOwner, list: List<ChannelBean>, position: Int, function: () -> Unit) {
        repository.channelAdd(list[position].id).observe(owner, Observer {
            it?.work { function() }
        })
    }

    fun channelRemove(owner: LifecycleOwner, list: List<ChannelBean>, position: Int, function: () -> Unit) {
        repository.channelRemove(list[position].id).observe(owner, Observer {
            it?.work { function() }
        })
    }

    /**
     * 点赞
     */
    fun like(owner: LifecycleOwner, contentId: String, function: () -> Unit) {
        repository.like(contentId).observe(owner, Observer {
            it?.work { function() }
        })
    }

    fun collection(owner: LifecycleOwner, contentId: String, function: () -> Unit) {
        repository.collection(contentId).observe(owner, Observer {
            it?.work {  function()}
        })
    }
}