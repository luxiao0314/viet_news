package com.viet.news.core.domain.request

import com.viet.news.core.config.LoginEnum
import java.io.Serializable

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 2018/9/10 下午1:41
 * @Version
 */
class LoginParams : Serializable {
    var phone_number: String? = ""
    var validation_code: String? = ""
    var password: String? = ""
    private var login_type: String? = ""
    fun setType(enum: LoginEnum) {
        login_type = enum.toString()
    }
}