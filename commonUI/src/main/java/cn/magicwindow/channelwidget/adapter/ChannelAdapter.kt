package cn.magicwindow.channelwidget.adapter


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator

import android.widget.ImageView
import android.widget.TextView
import cn.magicwindow.commonui.R


import cn.magicwindow.channelwidget.callback.EditModeHandler
import cn.magicwindow.channelwidget.viewholder.IChannelType
import cn.magicwindow.channelwidget.callback.ItemDragHelperCallback
import cn.magicwindow.channelwidget.callback.ItemDragListener
import cn.magicwindow.channelwidget.callback.ItemDragVHListener
import cn.magicwindow.channelwidget.entity.ChannelBean
import cn.magicwindow.channelwidget.viewholder.MyChannelHeaderViewHolder
import cn.magicwindow.channelwidget.viewholder.MyChannelViewHolder
import cn.magicwindow.channelwidget.viewholder.RecChannelHeaderViewHolder
import cn.magicwindow.channelwidget.viewholder.RecChannelViewHolder


@Suppress("DEPRECATION")
/**
 * @author null
 * 频道管理适配器
 */
class ChannelAdapter(context: Context, recyclerView: RecyclerView, private val mMyChannelItems: MutableList<ChannelBean>, private val mOtherChannelItems: MutableList<ChannelBean>,
                     private val mMyHeaderCount: Int, private val mRecHeaderCount: Int) : RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder>(), ItemDragListener {
    private val mInflater: LayoutInflater
    private val mTypeMap: SparseArray<IChannelType> = SparseArray()
    // 是否是编辑状态
    private var isEditMode: Boolean = false
    private val mItemTouchHelper: ItemTouchHelper = ItemTouchHelper(ItemDragHelperCallback(this))
    // touch 点击开始时间
    private var startTime: Long = 0
    private var channelItemClickListener: ChannelItemClickListener? = null

    init {
        mItemTouchHelper.attachToRecyclerView(recyclerView)
        mInflater = LayoutInflater.from(context)
        mTypeMap.put(IChannelType.TYPE_MY_CHANNEL_HEADER, MyChannelHeaderViewHolder(EditHandler()))
        mTypeMap.put(IChannelType.TYPE_MY_CHANNEL, MyChannelViewHolder(EditHandler()))
        mTypeMap.put(IChannelType.TYPE_REC_CHANNEL_HEADER, RecChannelHeaderViewHolder())
        mTypeMap.put(IChannelType.TYPE_REC_CHANNEL, RecChannelViewHolder(EditHandler()))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        return mTypeMap.get(viewType).createViewHolder(mInflater, parent)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        if (getItemViewType(position) == IChannelType.TYPE_MY_CHANNEL) {
            var myPosition = position - mMyHeaderCount
            myPosition = if (myPosition < 0 || myPosition >= mMyChannelItems.size) 0 else myPosition
            mTypeMap.get(getItemViewType(position)).bindViewHolder(holder, position, mMyChannelItems[myPosition])
            return
        }
        if (getItemViewType(position) == IChannelType.TYPE_REC_CHANNEL) {
            var otherPosition = position - mMyChannelItems.size - mMyHeaderCount - mRecHeaderCount
            otherPosition = if (otherPosition < 0 || otherPosition >= mOtherChannelItems.size) 0 else otherPosition
            mTypeMap.get(getItemViewType(position)).bindViewHolder(holder, position, mOtherChannelItems[otherPosition])
            return
        }
        mTypeMap.get(getItemViewType(position)).bindViewHolder(holder, position, null)
    }

    override fun getItemViewType(position: Int): Int {
        if (position < mMyHeaderCount)
            return IChannelType.TYPE_MY_CHANNEL_HEADER
        if (position >= mMyHeaderCount && position < mMyChannelItems.size + mMyHeaderCount)
            return IChannelType.TYPE_MY_CHANNEL
        return if (position >= mMyChannelItems.size + mMyHeaderCount && position < mMyChannelItems.size + mMyHeaderCount + mRecHeaderCount) IChannelType.TYPE_REC_CHANNEL_HEADER else IChannelType.TYPE_REC_CHANNEL
    }

    override fun getItemCount(): Int {
        return mMyChannelItems.size + mOtherChannelItems.size + mMyHeaderCount + mRecHeaderCount
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (toPosition > 2) {
            val item = mMyChannelItems[fromPosition - mMyHeaderCount]
            mMyChannelItems.removeAt(fromPosition - mMyHeaderCount)
            mMyChannelItems.add(toPosition - mMyHeaderCount, item)
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    override fun onItemSwiped(position: Int) {}

    /**
     * 频道条目
     */
    open class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ItemDragVHListener {

        override fun onItemSelected() {
            scaleItem(1.0f, 1.2f, 0.5f)
        }

        override fun onItemFinished() {
            scaleItem(1.2f, 1.0f, 1.0f)
        }

        private fun scaleItem(start: Float, end: Float, alpha: Float) {
            val anim1 = ObjectAnimator.ofFloat(itemView, "scaleX",
                    start, end)
            val anim2 = ObjectAnimator.ofFloat(itemView, "scaleY",
                    start, end)
            val anim3 = ObjectAnimator.ofFloat(itemView, "alpha",
                    alpha)

            val animSet = AnimatorSet()
            animSet.duration = 200
            animSet.interpolator = LinearInterpolator()
            animSet.playTogether(anim1, anim2, anim3)
            animSet.start()
        }
    }

    /**
     * 编辑状态捕获
     */
    private inner class EditHandler : EditModeHandler() {
        override fun startEditMode(mRecyclerView: RecyclerView) {
            doStartEditMode(mRecyclerView)
        }

        override fun cancelEditMode(mRecyclerView: RecyclerView) {
            doCancelEditMode(mRecyclerView)
        }

        override fun clickMyChannel(mRecyclerView: RecyclerView, holder: ChannelViewHolder) {
            val position = holder.adapterPosition
            if (isEditMode) {
                moveMyToOther(position)
            } else {
                if (channelItemClickListener != null) {
                    channelItemClickListener!!.onChannelItemClick(mMyChannelItems, position - mMyHeaderCount)
                }
            }
        }

        override fun touchMyChannel(motionEvent: MotionEvent, holder: ChannelViewHolder) {
            if (!isEditMode) {
                return
            }
            when (MotionEventCompat.getActionMasked(motionEvent)) {
                MotionEvent.ACTION_DOWN -> startTime = System.currentTimeMillis()
                MotionEvent.ACTION_MOVE -> if (System.currentTimeMillis() - startTime > SPACE_TIME) {
                    mItemTouchHelper.startDrag(holder)
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> startTime = 0
            }
        }

        override fun clickLongMyChannel(mRecyclerView: RecyclerView, holder: ChannelViewHolder) {
            if (!isEditMode) {
                doStartEditMode(mRecyclerView)
                val view = mRecyclerView.getChildAt(0)
                if (view === mRecyclerView.layoutManager!!.findViewByPosition(0)) {
                    val dragTip = view.findViewById<View>(R.id.id_my_header_tip_tv) as TextView
                    dragTip.text = "拖拽可以排序"

                    val tvBtnEdit = view.findViewById<View>(R.id.id_edit_mode) as TextView
                    tvBtnEdit.text = "完成"
                    tvBtnEdit.isSelected = true
                }
                mItemTouchHelper.startDrag(holder)
            }
        }

        override fun clickRecChannel(mRecyclerView: RecyclerView, holder: ChannelViewHolder) {
            val position = holder.adapterPosition
            moveOtherToMy(position)
        }
    }

    /**
     * 开启编辑模式
     */
    private fun doStartEditMode(parent: RecyclerView) {
        isEditMode = true
        val visibleChildCount = parent.childCount
        for (i in 0 until visibleChildCount) {
            val view = parent.getChildAt(i)
            val imgEdit = view.findViewById<ImageView>(R.id.id_delete_icon)
            if (imgEdit != null) {
                val item = mMyChannelItems[i - mMyHeaderCount]
                if (item.tabType == 2) {
                    imgEdit.visibility = View.VISIBLE
                } else {
                    imgEdit.visibility = View.INVISIBLE
                }
            }
        }
    }

    /**
     * 关闭编辑模式
     */
    private fun doCancelEditMode(parent: RecyclerView) {
        isEditMode = false
        val visibleChildCount = parent.childCount
        for (i in 0 until visibleChildCount) {
            val view = parent.getChildAt(i)
            val imgEdit = view.findViewById<ImageView>(R.id.id_delete_icon)
            if (imgEdit != null) {
                imgEdit.visibility = View.INVISIBLE
            }
        }
    }

    /**
     * 我的频道 -> 推荐频道
     */
    private fun moveMyToOther(position: Int) {
        val myPosition = position - mMyHeaderCount
        val item = mMyChannelItems[myPosition]
        mMyChannelItems.removeAt(myPosition)
        mOtherChannelItems.add(item)
        notifyItemMoved(position, mMyChannelItems.size + mMyHeaderCount + mRecHeaderCount + mOtherChannelItems.size - 1)
    }

    /**
     * 推荐频道-> 我的频道
     */
    private fun moveOtherToMy(position: Int) {
        val recPosition = processItemRemoveAdd(position)
        if (recPosition == -1) {
            return
        }
        notifyItemMoved(position, mMyChannelItems.size + mMyHeaderCount - 1)
    }


    private fun processItemRemoveAdd(position: Int): Int {
        val startPosition = position - mMyChannelItems.size - mRecHeaderCount - mMyHeaderCount
        if (startPosition > mOtherChannelItems.size - 1) {
            return -1
        }
        val item = mOtherChannelItems[startPosition]
        item.editStatus = if (isEditMode) 1 else 0
        mOtherChannelItems.removeAt(startPosition)
        mMyChannelItems.add(item)
        return position
    }


    fun setChannelItemClickListener(channelItemClickListener: ChannelItemClickListener) {
        this.channelItemClickListener = channelItemClickListener
    }

    interface ChannelItemClickListener {
        fun onChannelItemClick(list: List<ChannelBean>, position: Int)
    }

    companion object {
        // touch 间隔时间  用于分辨是否是 "点击"
        private const val SPACE_TIME: Long = 100
    }
}
