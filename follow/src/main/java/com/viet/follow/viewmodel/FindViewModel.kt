package com.viet.follow.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import cn.magicwindow.channelwidget.entity.ChannelBean
import com.viet.follow.repository.FindRepository
import com.viet.news.core.domain.response.NewsResponse
import com.viet.news.core.utils.FileUtils
import com.viet.news.core.viewmodel.BaseViewModel

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

    fun getNewsArticles(): LiveData<NewsResponse> {
        return FileUtils.handleVirtualData(NewsResponse::class.java)
    }

    fun getChannelList(owner: LifecycleOwner, function: () -> Unit) {
        repository.getChannelList().observe(owner, Observer {
            if (it?.data != null) {
                it.data?.data?.forEach {
                    normalList.add(ChannelBean(it.channelName, it.channelKey))
                    function()
                }
            }
        })
    }

    fun getChannelAllList(owner: LifecycleOwner, function: () -> Unit) {
        repository.getChannelAllList().observe(owner, Observer {
            if (it?.data != null) {
                unFollowList.clear()
                followList.clear()
                it.data?.data?.unFollowChannelList?.forEach { unFollowList.add(ChannelBean(it.channelName, it.channelKey)) }
                it.data?.data?.followChannelList?.forEachIndexed { index, list ->
                    if (index == 0) {
                        followList.add(ChannelBean(list.channelName, list.id, 0))
                    } else {
                        followList.add(ChannelBean(list.channelName, list.id, 2))
                    }
                }
                function()
            }
        })
    }

    fun channelAdd(owner: LifecycleOwner, id: String?, function: () -> Unit) {
        repository.channelAdd(id).observe(owner, Observer {
            if (it?.data != null) function()
        })
    }

    fun channelRemove(owner: LifecycleOwner, id: String?, function: () -> Unit) {
        repository.channelRemove(id).observe(owner, Observer {
            if (it?.data != null) function()
        })
    }
}