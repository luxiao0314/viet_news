package com.viet.follow.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.viet.follow.R
import com.viet.follow.fragment.FansTabFragment
import com.viet.follow.fragment.FollowTabFragment
import com.viet.follow.repository.FansAndFollowRepository
import com.viet.news.core.domain.response.UserInfoListResponse
import com.viet.news.core.ui.App
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
class FansAndFollowViewModel(var repository: FansAndFollowRepository = FansAndFollowRepository()) : BaseViewModel() {

    val titles = arrayListOf(App.instance.resources.getString(R.string.funs), App.instance.resources.getString(R.string.follow))
    val fragments = arrayListOf<BaseFragment>(FansTabFragment(), FollowTabFragment())
    var currentTab = MutableLiveData<Int>()
    var userId = MutableLiveData<String>()
    var cancelPosition = MutableLiveData<Int>()

    fun followList(page_number: Int): LiveData<Resource<UserInfoListResponse>> = repository.followList(page_number, userId.value)

    fun fansList(page_number: Int): LiveData<Resource<UserInfoListResponse>> = repository.fansList(page_number, userId.value)

    fun follow(owner: LifecycleOwner, userId: String?, function: () -> Unit) {
        repository.follow(userId).observe(owner, Observer { it?.work { function() } })
    }

    fun cancelfollow(owner: LifecycleOwner, userId: String?, function: () -> Unit) {
        repository.cancelfollow(userId).observe(owner, Observer { it?.work { function() } })
    }
}