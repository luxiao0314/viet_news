package com.viet.news.core.config

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.safframework.http.interceptor.LoggingInterceptor
import com.viet.news.core.BuildConfig
import com.viet.news.core.http.CertifyUtils
import okhttp3.OkHttpClient
import java.io.InputStream

@GlideModule
class CustomGlideModule : AppGlideModule() {

    override fun isManifestParsingEnabled(): Boolean = false

    override fun applyOptions(context: Context, builder: GlideBuilder) {
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888) //部分手机显示不清晰,换成高质量
        val maxMemory = Runtime.getRuntime().maxMemory().toInt()//获取系统分配给应用的总内存大小
        val memoryCacheSize = maxMemory / 8//设置图片内存缓存占用八分之一applyOptions
        //设置内存缓存大小
        builder.setMemoryCache(LruResourceCache(memoryCacheSize.toLong()))
        builder.setBitmapPool(LruBitmapPool(memoryCacheSize.toLong()))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        // 配置glide使用okhttp加载数据
        val builder = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .sslSocketFactory(CertifyUtils.getTrustAllSSLClient()!!.socketFactory)
                .hostnameVerifier { hostname, session -> true }
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(builder.build()))
    }

    private val loggingInterceptor = LoggingInterceptor.Builder()
            .loggable(BuildConfig.DEBUG) // TODO: 发布到生产环境需要改成false
            .request()
            .requestTag("Request")
            .response()
            .responseTag("Response")
            .build()
}