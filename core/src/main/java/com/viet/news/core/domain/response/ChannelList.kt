package com.viet.news.core.domain.response

import com.google.gson.annotations.SerializedName

class ChannelList(
        var id: String?,
        var createDateTime: Long?,
        var updateDateTime: Long?,
        var version: String?,
        @SerializedName("channel_key")
        var channelKey: String?,
        @SerializedName("channel_name")
        var channelName: String?,
        @SerializedName("follow_Status")
        var followStatus: Boolean,
        var sort: Int?,
        @SerializedName("can_delete")
        var canDelete: Boolean,
        @SerializedName("default_channel")
        var defaultChannel: Boolean)