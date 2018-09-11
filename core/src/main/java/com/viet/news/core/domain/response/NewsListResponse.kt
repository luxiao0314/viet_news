package com.viet.news.core.domain.response

import com.google.gson.annotations.SerializedName
import com.viet.news.core.api.HttpResponse

/**
 * @Description
 * @Author lucio
 * @Email xiao.lu@magicwindow.cn
 * @Date 11/09/2018 11:57 PM
 * @Version
 */
class NewsListResponse : HttpResponse<NewsList>()

data class NewsList(var total_count: String?,
               var total_pages: String?,
               var list:List<NewsListBean>)

data class NewsListBean(var id:String?,
                        var createDateTime:String?,
                        var updateDateTime:String?,
                        var version:String?,
                        @SerializedName("content_title")
                        var contentTitle:String?,
                        @SerializedName("content_image")
                        var contentImage:String?,
                        @SerializedName("content_detail")
                        var contentDetail:String?,
                        @SerializedName("content_type")
                        var contentType:Int,
                        @SerializedName("user_id")
                        var userId:String?,
                        @SerializedName("like_number")
                        var likeNumber:Int,
                        @SerializedName("view_number")
                        var viewNumber:Int,
                        @SerializedName("collection_number")
                        var collectionNumber:Int,
                        @SerializedName("content_profit")
                        var contentProfit:Int)