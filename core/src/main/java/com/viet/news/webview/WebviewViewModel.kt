package com.viet.news.webview

import android.arch.lifecycle.MutableLiveData
import com.viet.news.core.domain.request.TokenRequestParams
import com.viet.news.core.viewmodel.BaseViewModel

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 19/07/2018 2:59 PM
 * @Version
 */
class WebviewViewModel : BaseViewModel() {

    var oldTime: Long = 0L
    val holderTime: Long = 1000
    var injectedName: MutableLiveData<String> = MutableLiveData()
    var injectedToken: String? = ""
    var tokenParams: MutableLiveData<TokenRequestParams> = MutableLiveData()

}