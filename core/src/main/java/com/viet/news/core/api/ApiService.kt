package com.viet.news.core.api

import android.arch.lifecycle.LiveData
import com.viet.news.core.domain.request.*
import com.viet.news.core.domain.response.*
import io.reactivex.Maybe
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


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
    fun getlist4Channel(@Body param: ListParams): LiveData<ApiResponse<NewsListResponse>>

    //查询当前关注用户发表的文章列表
    @POST("v1/content/list4follow")
    fun getlist4Follow(@Body param: ListParams): LiveData<ApiResponse<NewsListResponse>>

    //查询收藏列表
    @POST("v1/content/collectionList")
    fun getCollectionList(@Body param: ListParams): LiveData<ApiResponse<CollectionListResponse>>

    //查询指定用户发布的文章列表
    @POST("v1/content/list4user")
    fun getlist4User(@Body param: ListParams): LiveData<ApiResponse<NewsListResponse>>

    //获取用户信息
    @GET("v1/user/info/{userId}")
    fun getUserInfo(@Path("userId") userId: String?): LiveData<ApiResponse<UserInfoResponse>>

    /**
     * 登录注册相关
     */
    @POST("v1/login/login")
    fun login(@Body param: LoginParams): LiveData<ApiResponse<LoginRegisterResponse>>

    @POST("v1/login/login")
    fun logins(@Body param: LoginParams): Call<HttpResponse<LoginRegisterResponse.LoginRegister>>

    @POST("v1/login/register")
    fun register(@Body param: SignInParams): LiveData<ApiResponse<LoginRegisterResponse>>

    @POST("v1/login/sendSms")
    fun sendSMS(@Body param: VerifyCodeParams): LiveData<ApiResponse<HttpResponse<Any>>>

    @POST("v1/login/checkValidationCode")
    fun checkVerifyCode(@Body param: VerifyCodeParams):LiveData<ApiResponse<HttpResponse<Any>>>

    @POST("v1/login/resetPassword")
    fun setPassword(@Body param: ResetPwdParams):LiveData<ApiResponse<LoginRegisterResponse>>

    @POST("v1/feedback/add")
    fun feedback(@Body param: FeedBackParams): LiveData<ApiResponse<Any>>

    @GET("v1/content/collection/{contentId}")
    fun collection(@Path("contentId") contentId: String): LiveData<ApiResponse<Any>>

    @GET("v1/user/updateNickName/{nick_name}")
    fun updateNickName(@Path("nick_name") nick_name: String): LiveData<ApiResponse<Any>>

    @GET("v1/content/like/{contentId}")
    fun like(@Path("contentId") contentId: String): LiveData<ApiResponse<Any>>

    @GET("v1/user/follow/{followUserId}")
    fun follow(@Path("followUserId") followUserId: String?): LiveData<ApiResponse<Any>>

    @POST("v1/user/followList")
    fun followList(@Body param: ListParams): LiveData<ApiResponse<UserInfoListResponse>>

    @POST("v1/user/fansList")
    fun fansList(@Body param: ListParams): LiveData<ApiResponse<UserInfoListResponse>>

    //多参数上传+多图片上传
    @Multipart
    @POST("userFeedback/uploadFeedback")
    fun uploadFile(@Part("email") email: String?,
                   @Part("subject") subject: String?,
                   @Part("content") content: String?,
                   @Part part: List<MultipartBody.Part>?): Maybe<HttpResponse<Any>>
    @Multipart
    @POST("v1/user/updateAvatar")
    fun uploadFile(@Part part: MultipartBody.Part): LiveData<ApiResponse<Any>>

    //用户反馈,未登录
    @Multipart
    @POST("userFeedback/uploadFeedbackWithoutLogin")
    fun uploadFileVisitor(@Part("email") email: String?,
                   @Part("subject") subject: String?,
                   @Part("content") content: String?,
                   @Part part: List<MultipartBody.Part>?): Maybe<HttpResponse<Any>>

}
