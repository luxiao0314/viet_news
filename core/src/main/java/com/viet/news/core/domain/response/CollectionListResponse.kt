package com.viet.news.core.domain.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CollectionListResponse(var total_count: String?,
                             var total_pages: String?,
                             var list: List<CollectionListBean>) : Serializable

data class CollectionListBean(var content: CollectionBean,var author: UserInfoResponse, var image_array: List<CollectionImageEntity>)

data class CollectionBean(var id: Int,
                          var createDateTime: Long,
                          var updateDateTime: Long,
                          var version: String?,
                          @SerializedName("content_title")
                          var contentTitle: String?,
                          @SerializedName("content_image")
                          var contentImage: String?,
                          @SerializedName("content_detail")
                          var contentDetail: String?,
                          @SerializedName("content_type")
                          var contentType: Int,
                          @SerializedName("user_id")
                          var userId: String?,
                          @SerializedName("like_number")
                          var likeNumber: Int,
                          @SerializedName("view_number")
                          var viewNumber: Int,
                          @SerializedName("collection_number")
                          var collectionNumber: Int,
                          @SerializedName("content_profit")
                          var contentProfit: Int)


data class CollectionImageEntity(var cover: String? = "")