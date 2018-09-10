package com.viet.news.core.domain.response

import java.io.Serializable

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 2018/9/10 上午10:49
 * @Version
 */
class ChannelListResponse(var id: String?,
                          var createDateTime: Long?,
                          var updateDateTime: Long?,
                          var version: String?,
                          var channel_key: String?,
                          var channel_name: String?,
                          var sort: Int?,
                          var can_delete: Boolean,
                          var default_channel: Boolean):Serializable