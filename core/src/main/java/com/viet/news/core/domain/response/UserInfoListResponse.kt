package com.viet.news.core.domain.response

import com.viet.news.core.api.HttpResponse

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 12/09/2018 12:02 PM
 * @Version
 */
class UserInfoListResponse : HttpResponse<UserInfoList>()

data class UserInfoList(var list: List<UserInfo>)