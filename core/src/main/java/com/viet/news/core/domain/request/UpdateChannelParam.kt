package com.viet.news.core.domain.request

import java.io.Serializable

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 14/09/2018 2:37 PM
 * @Version
 */
class UpdateChannelParam(var channel_list: List<UpdateChannel>? = null) : Serializable

data class UpdateChannel(var channel_id: String?) : Serializable