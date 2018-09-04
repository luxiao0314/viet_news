package cn.magicwindow.channelwidget.viewholder


import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.magicwindow.channelwidget.adapter.ChannelAdapter
import cn.magicwindow.channelwidget.callback.EditModeHandler
import cn.magicwindow.channelwidget.entity.ChannelBean
import cn.magicwindow.commonui.R


/**
 * @author null
 * 推荐频道 ViewHolder
 */

class RecChannelViewHolder(private val editModeHandler: EditModeHandler?) : IChannelType {
    private var mRecyclerView: RecyclerView? = null
    override fun createViewHolder(mInflater: LayoutInflater, parent: ViewGroup): ChannelAdapter.ChannelViewHolder {
        this.mRecyclerView = parent as RecyclerView
        return RecChannelHeaderViewHolder(mInflater.inflate(R.layout.layout_channel_rec, parent, false))
    }

    override fun bindViewHolder(holder: ChannelAdapter.ChannelViewHolder, position: Int, data: ChannelBean?) {
        val recHolder = holder as RecChannelHeaderViewHolder
        val name = data!!.tabName
        recHolder.mChannelTitleTv.text = "+  $name"
        val textSize = if (name!!.length >= 4) 14 else 16
        recHolder.mChannelTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        recHolder.mChannelTitleTv.setOnClickListener {
            editModeHandler?.clickRecChannel(mRecyclerView!!, holder)
        }
    }

    private inner class RecChannelHeaderViewHolder(itemView: View) : ChannelAdapter.ChannelViewHolder(itemView) {
        val mChannelTitleTv: TextView = itemView.findViewById<View>(R.id.id_channel_title) as TextView

    }
}
