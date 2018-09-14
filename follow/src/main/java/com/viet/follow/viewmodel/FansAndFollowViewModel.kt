package com.viet.follow.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.safframework.ext.then
import com.viet.follow.R
import com.viet.follow.fragment.FansTabFragment
import com.viet.follow.fragment.FollowTabFragment
import com.viet.follow.repository.FansAndFollowRepository
import com.viet.news.core.domain.response.UserInfoListResponse
import com.viet.news.core.ext.toast
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
    var userId: String? = ""

    fun followList(page_number: Int): LiveData<Resource< UserInfoListResponse>> {
        return repository.followList(page_number, userId)
    }

    fun fansList(page_number: Int): LiveData<Resource< UserInfoListResponse>> {
        return repository.fansList(page_number, userId)
    }

    fun follow(owner: LifecycleOwner, userId: String?,function: () -> Unit) {
        repository.follow(userId).observe(owner, Observer {
//            it?.data?.isOkStatus?.then({
                function()
//            }, {
//                toast(App.instance.resources.getString(R.string.error_msg)).show()
//            })
        })
    }
}