package com.viet.mine.viewmodel

import com.luck.picture.lib.entity.LocalMedia
import com.safframework.utils.RxJavaUtils
import com.viet.news.core.domain.response.HttpResponse
import com.viet.news.core.viewmodel.BaseViewModel
import io.reactivex.Maybe

class AccountInfoViewModel : BaseViewModel() {
    var selectList = ArrayList<LocalMedia>()

//    fun uploadFile(): Maybe<HttpResponse<Any>> =
//            apiService.uploadFile(ContentType.getPart(selectList[0].compressPath))
//                    .compose(RxJavaUtils.maybeToMain())
}