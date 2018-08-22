package com.lcorekit.channeldemo.widget


import android.support.v7.widget.RecyclerView
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
class MyChannelHeaderWidget(private val editModeHandler: EditModeHandler?) : IChannelType {
    private var mRecyclerView: RecyclerView? = null
    override fun createViewHolder(mInflater: LayoutInflater, parent: ViewGroup): ChannelAdapter.ChannelViewHolder {
        mRecyclerView = parent as RecyclerView
        return MyChannelHeaderViewHolder(mInflater.inflate(R.layout.layout_channel_my_header, parent, false))
    }

    override fun bindViewHolder(holder: ChannelAdapter.ChannelViewHolder, position: Int, data: ChannelBean?) {
        val viewHolder = holder as MyChannelHeaderViewHolder
        viewHolder.mEditModeTv.setOnClickListener {
            if (!viewHolder.mEditModeTv.isSelected) {
                editModeHandler?.startEditMode(mRecyclerView!!)
                viewHolder.mEditModeTv.text = "完成"
            } else {
                editModeHandler?.cancelEditMode(mRecyclerView!!)
                viewHolder.mEditModeTv.text = "编辑"
            }
            viewHolder.mEditModeTv.isSelected = !viewHolder.mEditModeTv.isSelected
        }
    }

    inner class MyChannelHeaderViewHolder(itemView: View) : ChannelAdapter.ChannelViewHolder(itemView) {
        val mEditModeTv: TextView = itemView.findViewById(R.id.id_edit_mode) as TextView
    }
}
