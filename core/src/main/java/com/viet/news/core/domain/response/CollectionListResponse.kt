package com.viet.news.core.domain.response

import com.google.gson.annotations.SerializedName
import com.viet.news.core.api.HttpResponse

class CollectionListResponse : HttpResponse<CollectionArticleList>()

data class CollectionArticleList(var total_count: String?,
                          var total_pages: String?,
                          var list: List<CollectionListBean>)

data class CollectionListBean(var id: String?,
                              var createDateTime: String?,
                              var updateDateTime: String?,
                              var version: String?,
                              @SerializedName("like_number")
                              val likeNumber: Int = 0,
                              @SerializedName("collection_number")
                              val collectionNumber: Int = 0,
                              @SerializedName("content_title")
                              val contentTitle: String = "",
                              @SerializedName("content_type")
                              val contentType: Int = 0,
                               @SerializedName("user_id")
                              val userId: Int = 0,
                              @SerializedName("content_profit")
                              val contentProfit: Int = 0,
                              @SerializedName("content_detail")
                              val contentDetail: String = "",
                              @SerializedName("content_image")
                              val contentImage: String = "",
                              @SerializedName("view_number")
                              val viewNumber: Int = 0)