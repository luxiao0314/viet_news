package com.viet.mine.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.viet.mine.repository.SettingRepository
import com.viet.news.core.viewmodel.BaseViewModel
import com.viet.news.core.vo.Status

class SettingViewModel(var repository: SettingRepository = SettingRepository()) : BaseViewModel() {
    var feedback: MutableLiveData<String> = MutableLiveData()    //提交反馈内容
    var submitEnable: MutableLiveData<Boolean> = MutableLiveData()   //提交按钮是否可用
    var count: MutableLiveData<Int> = MutableLiveData()   //剩余字数

    var nickName: MutableLiveData<String> = MutableLiveData()    //提交昵称

    fun checkFeedBackSubmitBtnEnable() {
        submitEnable.value = feedback.value != null && feedback.value!!.isNotEmpty()
    }

    fun checkNickNameSubmitBtnEnable() {
        submitEnable.value = nickName.value != null && nickName.value!!.isNotEmpty()
    }

    fun feedBackSubmitEnable(): Boolean = when {
        feedback.value == null || feedback.value.isNullOrBlank() -> {
            false
        }
        else -> true
    }
    fun nickNameSubmitEnable(): Boolean = when {
        nickName.value == null || nickName.value.isNullOrBlank() -> {
            false
        }
        else -> true
    }


    fun feedBack(owner: LifecycleOwner, feedback: String, finish: (isSuccess: Boolean) -> Unit) {
        return repository.feedBack(feedback).observe(owner, Observer {
            when (it!!.status) {
                Status.SUCCESS -> finish(true)
                Status.ERROR -> finish(false)
                else -> {
                    // TODO: 正在加载
                }
            }
        })
    }

    fun updateNickName(owner: LifecycleOwner, nickname: String, finish: (isSuccess: Boolean) -> Unit) {
        return repository.updateNickName(nickname).observe(owner, Observer {
            when (it!!.status) {
                Status.SUCCESS -> finish(true)
                Status.ERROR -> finish(false)
                else -> {
                    // TODO: 正在加载
                }
            }
        })
    }

}