package com.viet.follow.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Context
import cn.magicwindow.channelwidget.entity.ChannelBean
import com.safframework.ext.then
import com.viet.follow.R
import com.viet.follow.repository.FindRepository
import com.viet.news.core.api.HttpResponse
import com.viet.news.core.domain.response.NewsListBean
import com.viet.news.core.domain.response.NewsListResponse
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.App
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

    fun getlist4Channel(id: String?, page_number: Int): LiveData<Resource<HttpResponse<NewsListResponse>>> {
        return repository.getlist4Channel(page_number, id)
    }

    fun getlist4Follow(page_number: Int): LiveData<Resource<HttpResponse<NewsListResponse>>> {
        return repository.getlist4Follow(page_number, id)
    }

    fun getChannelList(owner: LifecycleOwner, function: () -> Unit) {
        repository.getChannelList().observe(owner, Observer { it ->
            it?.data?.isOkStatus?.then({
                it.data?.data?.forEach { normalList.add(ChannelBean(it.channelName, it.id, 2)) }
                function()
            }, {
                toast(App.instance.resources.getString(R.string.error_msg)).show()
            })
        })
    }

    fun getChannelAllList(owner: LifecycleOwner) {
        repository.getChannelAllList().observe(owner, Observer { it ->
            it?.data?.isOkStatus?.then({
                unFollowList.clear()
                followList.clear()
                it.data?.data?.unFollowChannelList?.forEach { unFollowList.add(ChannelBean(it.channelName, it.id, 2)) }
                it.data?.data?.followChannelList?.forEachIndexed { index, list ->
                    if (index == 0) {
                        followList.add(ChannelBean(list.channelName, list.id, 0))
                    } else {
                        followList.add(ChannelBean(list.channelName, list.id, 2))
                    }
                }
            }, {
                toast(App.instance.resources.getString(R.string.error_msg)).show()
            })
        })
    }

    /**
     * 频道排序:添加,删除,拖拽
     */
    fun updateSort(owner: LifecycleOwner, list: List<ChannelBean>, function: () -> Unit) {
        repository.updateSort(list).observe(owner, Observer {
            it?.data?.isOkStatus?.then({
                function()
            }, {
                toast(App.instance.resources.getString(R.string.error_msg)).show()
            })
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
            it?.data?.isOkStatus?.then({
                function(it.data?.data)
            }, {
                toast(App.instance.resources.getString(R.string.error_msg)).show()
            })
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
            it?.data?.isOkStatus?.then({
                function(it.data?.data)
            }, {
                toast(App.instance.resources.getString(R.string.error_msg)).show()
            })
        })
    }
}