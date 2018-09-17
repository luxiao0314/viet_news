package com.viet.follow.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Context
import com.viet.follow.repository.PersonalPageRepository
import com.viet.news.core.domain.response.NewsListResponse
import com.viet.news.core.domain.response.UserInfoResponse
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.viewmodel.BaseViewModel
import com.viet.news.core.vo.Resource

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 1:37 PM
 * @Version
 */
class PersonalPageModel(var repository: PersonalPageRepository = PersonalPageRepository()) : BaseViewModel() {

    var page_number = 0
    var userId: String? = ""

    fun getlist4User(): LiveData<Resource<NewsListResponse>> {
        return repository.getlist4User(page_number, userId)
    }

    fun getUserInfo(owner: LifecycleOwner, function: (user: UserInfoResponse?) -> Unit) {
        repository.getUserInfo(userId).observe(owner, Observer { it?.work { function(it.data) } })
    }

    fun follow(owner: LifecycleOwner, function: () -> Unit) {
        repository.follow(userId).observe(owner, Observer { it?.work { function() } })
    }

    fun cancelfollow(owner: LifecycleOwner, function: () -> Unit) {
        repository.cancelfollow(userId).observe(owner, Observer { it?.work { function() } })
    }

    /**
     * 点赞
     */
    fun like(context: Context, contentId: String) {
        repository.like(contentId).observe(context as BaseActivity, Observer { it?.work { } })
    }

    /**
     * 收藏
     */
    fun collection(context: Context, contentId: String) {
        repository.collection(contentId).observe(context as BaseActivity, Observer { it?.work { } })
    }
}