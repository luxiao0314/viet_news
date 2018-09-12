package com.viet.news.core.domain.request

import com.viet.news.core.config.VerifyCodeTypeEnum
import java.io.Serializable

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 11/09/2018 10:21 AM
 * @Version
 */
class VerifyCodeParams : Serializable {

    private var validation_code_type: String = ""
    var validation_code: String? = null
    // 国家代码 中国 86
    var zone_code: String? = ""
    var phone_number: String? = ""

    fun setType(enum: VerifyCodeTypeEnum) {
        validation_code_type = enum.toString()
    }
}