package cn.magicwindow.channelwidget.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.magicwindow.commonui.R

import cn.magicwindow.channelwidget.adapter.ChannelAdapter
import cn.magicwindow.channelwidget.entity.ChannelBean


/**
 * @author null
 * 推荐频道标题栏 ViewHolder
 */

class RecChannelHeaderViewHolder : IChannelType {
    override fun createViewHolder(mInflater: LayoutInflater, parent: ViewGroup): ChannelAdapter.ChannelViewHolder {
        return MyChannelHeaderViewHolder(mInflater.inflate(R.layout.layout_channel_rec_header, parent, false))
    }

    override fun bindViewHolder(holder: ChannelAdapter.ChannelViewHolder, position: Int, data: ChannelBean?) {

    }

    inner class MyChannelHeaderViewHolder(itemView: View) : ChannelAdapter.ChannelViewHolder(itemView)
}
