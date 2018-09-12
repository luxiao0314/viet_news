package com.viet.news.core.domain

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

open class GlobalNetworkException(val code :Int ,val bodyString :String)

open class RefreshUserInfoEvent