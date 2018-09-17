package cn.magicwindow.channelwidget.viewholder

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.magicwindow.channelwidget.adapter.ChannelAdapter
import cn.magicwindow.channelwidget.callback.EditModeHandler
import cn.magicwindow.channelwidget.entity.ChannelBean
import cn.magicwindow.commonui.R


/**
 * @author null
 * 我的频道 ViewHolder
 */
class MyChannelViewHolder(private val editModeHandler: EditModeHandler?) : IChannelType {
    private var mRecyclerView: RecyclerView? = null
    override fun createViewHolder(mInflater: LayoutInflater, parent: ViewGroup): ChannelAdapter.ChannelViewHolder {
        mRecyclerView = parent as RecyclerView
        return MyChannelHeaderViewHolder(mInflater.inflate(R.layout.layout_channel_my, parent, false))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun bindViewHolder(holder: ChannelAdapter.ChannelViewHolder, position: Int, data: ChannelBean?) {
        val myHolder = holder as MyChannelHeaderViewHolder
        data?.let {
            myHolder.mChannelTitleTv.text = it.channelName
            val textSize = if (it.channelName!!.length >= 4) 14 else 16
            myHolder.mChannelTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
            myHolder.mChannelTitleTv.setBackgroundResource(R.drawable.channel_my_bg_shape)
            myHolder.mChannelTitleTv.setTextColor(Color.parseColor("#333333"))
            myHolder.mDeleteIv.visibility = if (it.editStatus == 1) View.VISIBLE else View.INVISIBLE
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
