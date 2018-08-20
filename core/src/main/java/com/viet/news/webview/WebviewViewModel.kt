package com.viet.news.webview

import android.arch.lifecycle.MutableLiveData
import android.support.v4.app.FragmentActivity
import com.viet.news.core.viewmodel.BaseViewModel
import io.reactivex.Maybe

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 19/07/2018 2:59 PM
 * @Version
 */
class WebviewViewModel : BaseViewModel() {

    var shareType = 0
    var ticketCode = ""
    var oldTime: Long = 0L
    val holderTime: Long = 1000
    var injectedName: MutableLiveData<String> = MutableLiveData()
    var injectedToken: String? = ""

}