package com.lcorekit.channeldemo.widget


import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.lcorekit.channeldemo.callback.EditModeHandler
import com.lcorekit.channeldemo.callback.IChannelType
import com.lcorekit.channeldemo.bean.ChannelBean

import cn.magicwindow.commonui.R
import com.lcorekit.channeldemo.adapter.ChannelAdapter


/**
 * @author null
 */

class RecChannelWidget(private val editModeHandler: EditModeHandler?) : IChannelType {
    private var mRecyclerView: RecyclerView? = null
    override fun createViewHolder(mInflater: LayoutInflater, parent: ViewGroup): ChannelAdapter.ChannelViewHolder {
        this.mRecyclerView = parent as RecyclerView
        return RecChannelHeaderViewHolder(mInflater.inflate(R.layout.layout_channel_rec, parent, false))
    }

    override fun bindViewHolder(holder: ChannelAdapter.ChannelViewHolder, position: Int, data: ChannelBean?) {
        val recHolder = holder as RecChannelHeaderViewHolder
        recHolder.mChannelTitleTv.text = data!!.tabName
        val textSize = if (data.tabName!!.length >= 4) 14 else 16
        recHolder.mChannelTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        recHolder.mChannelTitleTv.setOnClickListener {
            editModeHandler?.clickRecChannel(mRecyclerView!!, holder)
        }
    }

    private inner class RecChannelHeaderViewHolder (itemView: View) : ChannelAdapter.ChannelViewHolder(itemView) {
        val mChannelTitleTv: TextView = itemView.findViewById<View>(R.id.id_channel_title) as TextView

    }
}
