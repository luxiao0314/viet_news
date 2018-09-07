package com.viet.follow.viewmodel

import android.arch.lifecycle.LiveData
import com.viet.follow.R
import com.viet.follow.fragment.FunsAndFollowFragment
import com.viet.news.core.domain.response.FunsAndFollowResponse
import com.viet.news.core.ui.App
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.utils.FileUtils
import com.viet.news.core.viewmodel.BaseViewModel

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 1:37 PM
 * @Version
 */
class FunsAndFollowViewModel : BaseViewModel() {

    val titles = arrayListOf(App.instance.resources.getString(R.string.funs), App.instance.resources.getString(R.string.follow))
    val fragments = arrayListOf<BaseFragment>(FunsAndFollowFragment(), FunsAndFollowFragment())
    var currentTab = 0

    fun getData(): LiveData<FunsAndFollowResponse> {
        return FileUtils.handleVirtualData(FunsAndFollowResponse::class.java)
    }
}