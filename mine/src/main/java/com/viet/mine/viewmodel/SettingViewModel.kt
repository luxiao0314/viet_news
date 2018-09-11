package com.viet.mine.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.viet.mine.repository.SettingRepository
import com.viet.news.core.viewmodel.BaseViewModel

class SettingViewModel(var repository: SettingRepository = SettingRepository()) : BaseViewModel() {

    fun feedBack(owner: LifecycleOwner, feedback: String) {
        return repository.feedBack(feedback).observe(owner, Observer {
                print(it!!.status)
        })
    }
}