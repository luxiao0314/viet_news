package com.viet.follow.viewmodel

import android.arch.lifecycle.LiveData
import com.viet.news.core.domain.response.PersonalPageResponse
import com.viet.news.core.utils.FileUtils
import com.viet.news.core.viewmodel.BaseViewModel

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 1:37 PM
 * @Version
 */
class PersonalPageModel : BaseViewModel() {

    fun getNewsArticles(): LiveData<PersonalPageResponse> {
        return FileUtils.handleVirtualData(PersonalPageResponse::class.java)
    }
}