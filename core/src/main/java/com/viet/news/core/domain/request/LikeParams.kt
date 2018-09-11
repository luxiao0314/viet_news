package com.viet.news.core.domain.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @Author Aaron
 * @Date 2018/9/11
 * @Email aaron@magicwindow.cn
 * @Description
 */
data class LikeParams(
        @SerializedName("can_delete") var can_delete: String = "",
        @SerializedName("channel_key") var channel_key: String = "",
        @SerializedName("channel_name") var channel_name: String = "",
        @SerializedName("createDateTime") var createDateTime: String = "",
        @SerializedName("default_channel") var default_channel: String = "",
        @SerializedName("id") var id: String = "",
        @SerializedName("sort") var sort: String = "",
        @SerializedName("updateDateTime") var updateDateTime: String = "",
        @SerializedName("version") var version: String = ""
) : Serializable