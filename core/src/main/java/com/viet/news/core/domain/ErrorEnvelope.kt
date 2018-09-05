package com.viet.news.core.domain

import com.viet.news.core.config.Config

class ErrorEnvelope @JvmOverloads constructor(val code: Int, val message: String?, private val throwable: Throwable? = null) {

    constructor(message: String?) : this(Config.ErrorCode.UNKNOWN, message) {}
}
