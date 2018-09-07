package com.viet.news.core.domain.response

import com.google.gson.annotations.SerializedName

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 07/09/2018 2:28 PM
 * @Version
 */
class FunsAndFollowResponse(var data: List<DataBean>? = null)

class DataBean(@SerializedName("title") var title: String?,
               @SerializedName("funsNum") var funsNum: Int?,
               @SerializedName("cover") var cover: String?,
               @SerializedName("follow") var follow: Boolean?)