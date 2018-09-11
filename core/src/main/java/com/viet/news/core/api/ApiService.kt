package com.viet.news.core.api

import android.arch.lifecycle.LiveData
import com.viet.news.core.domain.request.*
import com.viet.news.core.domain.response.ChannelAllListResponse
import com.viet.news.core.domain.response.ChannelListResponse
import com.viet.news.core.domain.response.LoginRegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST


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
    @POST("v1/channel/add")
    fun channelAdd(@Body param: ChannelParams): LiveData<ApiResponse<Any>>

    @POST("v1/channel/remove")
    fun channelRemove(@Body param: ChannelParams): LiveData<ApiResponse<Any>>

    @POST("v1/channel/allList")
    fun getChannelAllList(): LiveData<ApiResponse<ChannelAllListResponse>>

    @POST("v1/channel/list")
    fun getChannelList(): LiveData<ApiResponse<ChannelListResponse>>

    //根据频道查询文章列表
    @POST("v1/content/list4Channel")
    fun getlist4Channel(): LiveData<ApiResponse<ChannelListResponse>>

    //查询当前关注用户发表的文章列表
    @POST("v1/content/list4follow")
    fun list4follow(): LiveData<ApiResponse<ChannelListResponse>>

    /**
     * 登录注册相关
     */
    @POST("v1/login/login")
    fun login(@Body param: LoginParams): LiveData<ApiResponse<LoginRegisterResponse>>

    //POST /v1/login/register
    @POST("v1/login/login")
    fun register(@Body param: RegisterParams): LiveData<ApiResponse<LoginRegisterResponse>>

    @POST("v1/feedback/add")
    fun feedback(@Body param: FeedBackParams): LiveData<ApiResponse<Any>>

    @POST("/v1/content/collection")
    fun collection(@Body param: CollectionParams): LiveData<ApiResponse<Any>>

    @POST("/v1/content/like")
    fun like(@Body param: LikeParams): LiveData<ApiResponse<Any>>


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
