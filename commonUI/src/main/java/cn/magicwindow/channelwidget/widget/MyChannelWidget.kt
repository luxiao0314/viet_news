package com.lcorekit.channeldemo.widget

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.lcorekit.channeldemo.callback.EditModeHandler
import com.lcorekit.channeldemo.callback.IChannelType
import com.lcorekit.channeldemo.bean.ChannelBean

import cn.magicwindow.commonui.R
import com.lcorekit.channeldemo.adapter.ChannelAdapter


/**
 * @author null
 */
class MyChannelWidget(private val editModeHandler: EditModeHandler?) : IChannelType {
    private var mRecyclerView: RecyclerView? = null
    override fun createViewHolder(mInflater: LayoutInflater, parent: ViewGroup): ChannelAdapter.ChannelViewHolder {
        mRecyclerView = parent as RecyclerView
        return MyChannelHeaderViewHolder(mInflater.inflate(R.layout.layout_channel_my, parent, false))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun bindViewHolder(holder: ChannelAdapter.ChannelViewHolder, position: Int, data: ChannelBean?) {
        val myHolder = holder as MyChannelHeaderViewHolder
        data?.let {
            myHolder.mChannelTitleTv.text = data.tabName
            val textSize = if (data.tabName!!.length >= 4) 14 else 16
            myHolder.mChannelTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
            myHolder.mChannelTitleTv.setBackgroundResource(if (data.tabType == 0 || data.tabType == 1)
                R.drawable.channel_fixed_bg_shape
            else
                R.drawable.channel_my_bg_shape)
            myHolder.mChannelTitleTv.setTextColor(if (data.tabType == 0)
                Color.RED
            else if (data.tabType == 1) Color.parseColor("#666666") else Color.parseColor("#333333"))
            myHolder.mDeleteIv.visibility = if (data.editStatus == 1) View.VISIBLE else View.INVISIBLE
            myHolder.mChannelTitleTv.setOnClickListener {
                if (editModeHandler != null && data.tabType == 2) {
                    editModeHandler.clickMyChannel(mRecyclerView!!, holder)
                }
            }
            myHolder.mChannelTitleTv.setOnTouchListener { _v, motionEvent ->
                if (editModeHandler != null && data.tabType == 2) {
                    editModeHandler.touchMyChannel(motionEvent, holder)
                }
                false
            }
            myHolder.mChannelTitleTv.setOnLongClickListener {
                if (editModeHandler != null && data.tabType == 2) {
                    editModeHandler.clickLongMyChannel(mRecyclerView!!, holder)
                }
                true
            }
        }
    }

    inner class MyChannelHeaderViewHolder(itemView: View) : ChannelAdapter.ChannelViewHolder(itemView) {
        val mChannelTitleTv: TextView = itemView.findViewById(R.id.id_channel_title)
        val mDeleteIv: ImageView = itemView.findViewById(R.id.id_delete_icon)

    }
}
