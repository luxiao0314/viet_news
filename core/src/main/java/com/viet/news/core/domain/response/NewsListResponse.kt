package com.viet.news.core.domain.response

import com.google.gson.annotations.SerializedName

/**
 * @Description
 * @Author lucio
 * @Email xiao.lu@magicwindow.cn
 * @Date 11/09/2018 11:57 PM
 * @Version
 */
class NewsListResponse(var total_count: String?,
                       var total_pages: String?,
                       var list: List<NewsListBean>)

data class NewsListBean(var content: ContentBean, var author: UserInfoResponse, var image_array: ArrayList<ImageEntity>)

data class ContentBean(var id: Int, //文章id,点赞,收藏使用此id
                       var createDateTime: Long,
                       var updateDateTime: Long,
                       var version: String?,
                       @SerializedName("content_title")
                       var contentTitle: String?,
                       @SerializedName("like_flag")
                       var likeFlag: Boolean,
                       @SerializedName("collection_flag")
                       var collectionFlag: Boolean,
                       @SerializedName("content_image")
                       var contentImage: String?,
                       @SerializedName("content_detail")
                       var contentDetail: String?,
                       @SerializedName("content_type")
                       var contentType: Int,
                       @SerializedName("user_id")
                       var userId: String?, //个人主页采用此id
                       @SerializedName("like_number")
                       var likeNumber: Int,
                       @SerializedName("view_number")
                       var viewNumber: Int,
                       @SerializedName("collection_number")
                       var collectionNumber: Int,
                       @SerializedName("content_profit")
                       var contentProfit: Int)

data class ImageEntity(var cover: String? = "")