package com.viet.mine.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.viet.follow.R
import com.viet.mine.repository.MineRepository
import com.viet.news.core.domain.response.UserInfo
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.App
import com.viet.news.core.viewmodel.BaseViewModel
import com.viet.news.core.vo.Status

class MineViewModel(var repository: MineRepository = MineRepository()) : BaseViewModel() {

    fun getUserInfo(userId: Int, owner: LifecycleOwner, function: (user: UserInfo?) -> Unit) {
        repository.getUserInfo(userId).observe(owner, Observer {
            if (it?.status == Status.SUCCESS) {
                function(it.data?.data)
            } else if (it?.status == Status.ERROR) {
                toast(App.instance.resources.getString(R.string.error_msg)).show()
            }
        })
    }

}