package com.viet.news.core.domain.response

import java.io.Serializable

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 12/09/2018 12:02 PM
 * @Version
 */
class UserInfoResponse : Serializable {
    var id: String? = ""
    var zone_code: Int = 0
    var phone_number: String? = ""
    var nick_name: String? = ""
    var avatar: String? = ""
    var invite_code: String? = ""
    var follow_flag: Boolean = false
    var self_flag: Boolean = false
    var fans_count: Int = 0
    var follow_count: Int = 0
    var is_bind: Boolean = false
    var is_set_password: Boolean = false
}