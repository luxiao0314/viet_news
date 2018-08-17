package com.viet.news.core.domain

class ErrorEnvelope @JvmOverloads constructor(val code: Int, val message: String?, private val throwable: Throwable? = null) {

    constructor(message: String?) : this(Config.ErrorCode.UNKNOWN, message) {}
}
