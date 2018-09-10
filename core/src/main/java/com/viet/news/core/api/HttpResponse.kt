package com.viet.news.core.api

import java.io.Serializable

data class HttpResponse<T>(
        var code: Int = -1, //0: 成功1: M-Token错误或过期2: 业务逻辑错误 500:系统内部错误  998表示sdkOwnerToken无效
        var message: String? = null,
        var data: T? = null
) : Serializable {
    val isOkStatus: Boolean
        get() = code == 0
}