package com.viet.news.core.api

import android.arch.lifecycle.LiveData
import com.viet.news.core.domain.request.FeedBackParams
import com.viet.news.core.domain.request.List4ChannelParams
import com.viet.news.core.domain.request.LoginParams
import com.viet.news.core.domain.request.RegisterParams
import com.viet.news.core.domain.response.ChannelAllListResponse
import com.viet.news.core.domain.response.ChannelListResponse
import com.viet.news.core.domain.response.LoginRegisterResponse
import com.viet.news.core.domain.response.NewsListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


/**
 * Created by Tony Shen on 2017/8/15.
 */
interface ApiService {

    companion object {
        const val MAGICBOX_API = "http://magicbox.liaoyantech.cn/magicbox/api/"
    }

    /**
     * 频道文章列表相关
     */
    @GET("v1/channel/add/{channelId}")
    fun channelAdd(@Path("channelId") channelId: String?): LiveData<ApiResponse<Any>>

    @GET("v1/channel/remove/{channelId}")
    fun channelRemove(@Path("channelId") channelId: String?): LiveData<ApiResponse<Any>>

    @POST("v1/channel/allList")
    fun getChannelAllList(): LiveData<ApiResponse<ChannelAllListResponse>>

    @POST("v1/channel/list")
    fun getChannelList(): LiveData<ApiResponse<ChannelListResponse>>

    //根据频道查询文章列表
    @POST("v1/content/list4Channel")
    fun getlist4Channel(@Body param: List4ChannelParams): LiveData<ApiResponse<NewsListResponse>>

    //查询当前关注用户发表的文章列表
    @POST("v1/content/list4follow")
    fun getlist4Follow(@Body param: List4ChannelParams): LiveData<ApiResponse<NewsListResponse>>

    /**
     * 登录注册相关
     */
    @POST("v1/login/login")
    fun login(@Body param: LoginParams): LiveData<ApiResponse<LoginRegisterResponse>>

    @POST("v1/login/register")
    fun register(@Body param: RegisterParams): LiveData<ApiResponse<LoginRegisterResponse>>

    @POST("v1/feedback/add")
    fun feedback(@Body param: FeedBackParams): LiveData<ApiResponse<Any>>

    @GET("v1/content/collection/{contentId}")
    fun collection(@Path("contentId") contentId: String): LiveData<ApiResponse<Any>>

    @GET("v1/user/updateNickName/{nick_name}")
    fun updateNickName(@Path("nick_name") nick_name: String): LiveData<ApiResponse<Any>>

    @GET("v1/content/like/{contentId}")
    fun like(@Path("contentId") contentId: String): LiveData<ApiResponse<Any>>

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
