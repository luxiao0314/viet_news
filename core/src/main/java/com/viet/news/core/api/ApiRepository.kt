package com.viet.news.core.api

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 2018/9/10 上午10:58
 * @Version
 */
open class ApiRepository {
    val apiInterface: ApiService = RetrofitManager.get().apiService()
}