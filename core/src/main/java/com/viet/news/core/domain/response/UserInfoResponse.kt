package com.viet.news.core.domain.response

import com.viet.news.core.api.HttpResponse

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 12/09/2018 12:02 PM
 * @Version
 */
class UserInfoResponse : HttpResponse<UserInfo>()

class UserInfo {
    var id: Int = 0
    var zone_code: Int = 0
    var phone_number: String? = ""
    var nick_name: String? = ""
    var avatar: String? = ""
    var invite_code: String? = ""
    var follow_flag: Boolean = false
    var self_flag: String? = ""
    var fans_count: Int = 0
    var follow_count: Int = 0
}