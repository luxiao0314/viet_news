package com.viet.news.core.domain.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @Author Aaron
 * @Date 2018/9/11
 * @Email aaron@magicwindow.cn
 * @Description
 */
data class CollectionParams(
        @SerializedName("collection_number") var collectionNumber: String = "",
        @SerializedName("content_detail") var contentDetail: String = "",
        @SerializedName("content_image") var contentImage: String = "",
        @SerializedName("content_profit") var contentProfit: String = "",
        @SerializedName("content_title") var contentTitle: String = "",
        @SerializedName("content_type") var contentType: String = "",
        @SerializedName("createDateTime") var createDateTime: String = "",
        @SerializedName("updateDateTime") var updateDateTime: String = "",
        @SerializedName("id") var id: String = "",
        @SerializedName("like_number") var likeNumber: String = "",
        @SerializedName("user_id") var userId: String = "",
        @SerializedName("view_number") var viewNumber: String = "",
        var imageUrl: String = ""
) : Serializable