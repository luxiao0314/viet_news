package cn.magicwindow.channelwidget.viewholder


import android.support.v7.widget.RecyclerView
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
 * 我的频道标题栏 ViewHolder
 */
class MyChannelHeaderViewHolder(private val editModeHandler: EditModeHandler?) : IChannelType {
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
                viewHolder.mEditModeTv.text = mRecyclerView?.context?.getString(R.string.complete)
            } else {
                editModeHandler?.cancelEditMode(mRecyclerView!!)
                viewHolder.mEditModeTv.text =  mRecyclerView?.context?.getString(R.string.sort_or_delete)
            }
            viewHolder.mEditModeTv.isSelected = !viewHolder.mEditModeTv.isSelected
        }
    }

    inner class MyChannelHeaderViewHolder(itemView: View) : ChannelAdapter.ChannelViewHolder(itemView) {
        val mEditModeTv: TextView = itemView.findViewById(R.id.id_edit_mode) as TextView
    }
}
