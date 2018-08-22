package com.lcorekit.channeldemo.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.magicwindow.commonui.R

import com.lcorekit.channeldemo.adapter.ChannelAdapter
import com.lcorekit.channeldemo.bean.ChannelBean
import com.lcorekit.channeldemo.callback.IChannelType


/**
 * @author null
 */

class RecChannelHeaderWidget : IChannelType {
    override fun createViewHolder(mInflater: LayoutInflater, parent: ViewGroup): ChannelAdapter.ChannelViewHolder {
        return MyChannelHeaderViewHolder(mInflater.inflate(R.layout.layout_channel_rec_header, parent, false))
    }

    override fun bindViewHolder(holder: ChannelAdapter.ChannelViewHolder, position: Int, data: ChannelBean?) {

    }

    inner class MyChannelHeaderViewHolder(itemView: View) : ChannelAdapter.ChannelViewHolder(itemView)
}
