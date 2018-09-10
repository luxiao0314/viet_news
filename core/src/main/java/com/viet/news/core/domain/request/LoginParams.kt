package com.viet.news.core.domain.request

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
    var password: String? = ""
    var login_type: String? = ""
}