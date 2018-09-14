package com.viet.follow.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Context
import cn.magicwindow.channelwidget.entity.ChannelBean
import com.viet.follow.repository.FindRepository
import com.viet.news.core.domain.response.NewsListBean
import com.viet.news.core.domain.response.NewsListResponse
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.BaseFragment
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
                it.data?.followChannelList?.forEach { followList.add(ChannelBean(it.channelName, it.id, if (it.canDelete) 2 else 0)) }
            }
        })
    }

    /**
     * 频道排序:添加,删除,拖拽
     */
    fun updateSort(owner: LifecycleOwner, list: List<ChannelBean>, function: () -> Unit) {
        repository.updateSort(list).observe(owner, Observer {
            it?.work { function() }
        })
    }

    /**
     * 点赞
     */
    private lateinit var owner: LifecycleOwner

    fun like(context: Context, contentId: String, function: (num: Int?) -> Unit) {
        if (context is BaseFragment) {
            owner = context
        } else if (context is BaseActivity) {
            owner = context
        }
        repository.like(contentId).observe(owner, Observer {
            it?.work { function(it.data) }
        })
    }

    /**
     * 收藏
     */
    fun collection(context: Context, contentId: String, function: (num: Int?) -> Unit) {
        if (context is BaseFragment) {
            owner = context
        } else if (context is BaseActivity) {
            owner = context
        }
        repository.collection(contentId).observe(owner, Observer {
            it?.work { function(it.data) }
        })
    }
}