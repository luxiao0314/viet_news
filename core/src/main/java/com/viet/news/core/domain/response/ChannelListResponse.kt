package com.viet.news.core.domain.response

import com.google.gson.annotations.SerializedName
import com.viet.news.core.api.HttpResponse
import java.io.Serializable

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 2018/9/10 上午10:49
 * @Version
 */
class ChannelListResponse : HttpResponse<List<ChannelListResponse.ChannelList>>() {

    data class ChannelList(
            var id: String?,
            var createDateTime: Long?,
            var updateDateTime: Long?,
            var version: String?,
            var channel_key: String?,
            var channel_name: String?,
            var sort: Int?,
            var can_delete: Boolean,
            var default_channel: Boolean
    )
}