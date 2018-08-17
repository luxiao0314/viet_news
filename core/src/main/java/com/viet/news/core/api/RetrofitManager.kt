package com.viet.news.core.api

import com.viet.news.core.BuildConfig
import com.viet.news.core.api.interceptor.HeaderInterceptor
import com.viet.news.core.api.interceptor.LoggingInterceptor
import com.viet.news.core.utils.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitManager private constructor() {

    private val apiService: ApiService

    private val okHttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()
        builder.writeTimeout((10 * 1000).toLong(), TimeUnit.MILLISECONDS)
        builder.readTimeout((10 * 1000).toLong(), TimeUnit.MILLISECONDS)
        builder.connectTimeout((10 * 1000).toLong(), TimeUnit.MILLISECONDS)

        val loggingInterceptor = LoggingInterceptor.Builder()
                .loggable(true).request()
                .requestTag("Request")
                .response()
                .responseTag("Response")
        if (BuildConfig.DEBUG)
            loggingInterceptor.loggable(true) // TODO: 发布到生产环境需要改成false

        //设置拦截器
        builder.addInterceptor(HeaderInterceptor())
        builder.addInterceptor(loggingInterceptor.build())

        okHttpClient = builder.build()

        mRetrofit = Retrofit.Builder()
                .baseUrl(ApiService.NEWSAPI_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .client(okHttpClient)
                .build()

        apiService = mRetrofit.create(ApiService::class.java)
    }

    fun retrofit(): Retrofit = mRetrofit

    fun apiService(): ApiService = apiService

    fun okHttpClient(): OkHttpClient = okHttpClient

    private object Holder {
        val MANAGER = RetrofitManager()
    }

    companion object {

        private lateinit var mRetrofit: Retrofit

        @JvmStatic
        fun get(): RetrofitManager {

            return Holder.MANAGER
        }
    }
}