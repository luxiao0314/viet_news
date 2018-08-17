package com.viet.news.core.api

import android.arch.lifecycle.LiveData
import com.viet.news.rac.ui.model.ArticlesResponse
import com.viet.news.rac.ui.model.SourceResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Tony Shen on 2017/8/15.
 */
interface ApiService {


    companion object {
        val NEWSAPI_URL = "https://newsapi.org/v1/"

    }

    @GET("sources")
    fun getSources(@Query("language") language: String?,
                   @Query("category") category: String?,
                   @Query("country") country: String?): LiveData<ApiResponse<SourceResponse>>

    @GET("articles")
    fun getArticles(@Query("source") source: String,
                    @Query("sortBy") sortBy: String?,
                    @Query("apiKey") apiKey: String):  LiveData<ApiResponse<ArticlesResponse>>
    /**
     * 检查token过期接口
     * @param param
     *
     * @return
     */
    /*@GET("register/checkTokenExpired")
    fun checkTokenExpired(): Observable<LoginResponse>


    //多参数上传+多图片上传
    @Multipart
    @POST("userFeedback/uploadFeedback")
    fun uploadFile(@Part("email") email: String?,
                   @Part("subject") subject: String?,
                   @Part("content") content: String?,
                   @Part part: List<MultipartBody.Part>?): Maybe<HttpResponse<Any>>

    //用户反馈,未登录
    @Multipart
    @POST("userFeedback/uploadFeedbackWithoutLogin")
    fun uploadFileVisitor(@Part("email") email: String?,
                   @Part("subject") subject: String?,
                   @Part("content") content: String?,
                   @Part part: List<MultipartBody.Part>?): Maybe<HttpResponse<Any>>*/

}
