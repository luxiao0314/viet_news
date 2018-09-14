package com.viet.news.core.domain

import com.viet.news.core.api.HttpResponse

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 24/04/2018 2:49 PM
 * @Version
 */
open class LogoutEvent

open class LoginEvent

open class RefreshNewsEvent

open class GlobalNetworkException(val code :Int ,val httpResponse: HttpResponse<*>?)

open class RefreshUserInfoEvent