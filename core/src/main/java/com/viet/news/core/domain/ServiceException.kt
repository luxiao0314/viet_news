package com.viet.news.core.domain

import java.lang.RuntimeException

/**
 * @Description 服务器异常
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 25/09/2018 1:51 PM
 * @Version 1.0.0
 */
class ServiceException(var code: Int, override var message: String?) : RuntimeException()
