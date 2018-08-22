package cn.magicwindow.channelwidget.callback

import android.support.v7.widget.RecyclerView
import android.view.MotionEvent

import cn.magicwindow.channelwidget.adapter.ChannelAdapter

/**
 * @author null
 * 编辑状态的回调
 */
abstract class EditModeHandler {
    open fun startEditMode(mRecyclerView: RecyclerView) {}
    open fun cancelEditMode(mRecyclerView: RecyclerView) {}
    open fun clickMyChannel(mRecyclerView: RecyclerView, holder: ChannelAdapter.ChannelViewHolder) {}
    open fun clickLongMyChannel(mRecyclerView: RecyclerView, holder: ChannelAdapter.ChannelViewHolder) {}
    open fun touchMyChannel(motionEvent: MotionEvent, holder: ChannelAdapter.ChannelViewHolder) {}
    open fun clickRecChannel(mRecyclerView: RecyclerView, holder: ChannelAdapter.ChannelViewHolder) {}
}
