package com.viet.news.core.domain.response

class ChannelList(
        var id: String?,
        var createDateTime: Long?,
        var updateDateTime: Long?,
        var version: String?,
        var channel_key: String?,
        var channel_name: String?,
        var sort: Int?,
        var can_delete: Boolean,
        var default_channel: Boolean)