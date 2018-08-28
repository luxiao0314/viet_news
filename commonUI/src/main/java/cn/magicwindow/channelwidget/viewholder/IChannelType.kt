package cn.magicwindow.channelwidget.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import cn.magicwindow.channelwidget.adapter.ChannelAdapter
import cn.magicwindow.channelwidget.entity.ChannelBean


/**
 * @author null
 * RecyclerView中类别
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
