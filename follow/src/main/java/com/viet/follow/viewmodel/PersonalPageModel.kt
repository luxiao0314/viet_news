package com.viet.follow.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.viet.follow.R
import com.viet.follow.repository.PersonalPageRepository
import com.viet.news.core.domain.response.NewsListResponse
import com.viet.news.core.domain.response.UserInfo
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.App
import com.viet.news.core.viewmodel.BaseViewModel
import com.viet.news.core.vo.Resource
import com.viet.news.core.vo.Status

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 1:37 PM
 * @Version
 */
class PersonalPageModel(var repository: PersonalPageRepository = PersonalPageRepository()) : BaseViewModel() {

    var page_number = 0
    var userId: String? = "1"

    fun getlist4User(): LiveData<Resource<NewsListResponse>> {
        return repository.getlist4User(page_number, userId)
    }

    fun getUserInfo(owner: LifecycleOwner, function: (user: UserInfo?) -> Unit) {
        repository.getUserInfo(userId).observe(owner, Observer {
            if (it?.status == Status.SUCCESS) {
                function(it.data?.data)
            } else if (it?.status == Status.ERROR) {
                toast(App.instance.resources.getString(R.string.error_msg)).show()
            }
        })
    }


}