package com.lcorekit.channeldemo.callback

import android.view.LayoutInflater
import android.view.ViewGroup
import com.lcorekit.channeldemo.adapter.ChannelAdapter

import com.lcorekit.channeldemo.bean.ChannelBean


/**
 * @author null
 */

interface IChannelType {

    fun createViewHolder(mInflater: LayoutInflater, parent: ViewGroup): ChannelAdapter.ChannelViewHolder
    fun bindViewHolder(holder: ChannelAdapter.ChannelViewHolder, position: Int, data: ChannelBean?)

    companion object {
        //我的频道头部部分
        const val TYPE_MY_CHANNEL_HEADER = 0
        //我的频道部分
        const val TYPE_MY_CHANNEL = 1
        //推荐头部部分
        const val TYPE_REC_CHANNEL_HEADER = 2
        //推荐部分
        const val TYPE_REC_CHANNEL = 3
    }
}
