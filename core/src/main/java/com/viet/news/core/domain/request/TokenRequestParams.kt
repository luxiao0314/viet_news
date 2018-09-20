package com.viet.news.core.domain.request

import java.io.Serializable

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 13/08/2018 3:05 PM
 * @Version
 */
class TokenRequestParams(var activityId: String? = "",
                         var accountKey: String? = "",
                         var appKey: String? = "",
                         var externalUserId: String? = "",
                         var uatId: Long? = 0) : Serializable