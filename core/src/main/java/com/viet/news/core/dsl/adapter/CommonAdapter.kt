package com.viet.news.core.dsl.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.viet.news.core.ext.event

/**
 *@version:
 *@FileDescription:
 *@Author:jing
 *@Since:2018/6/12
 *@ChangeList:
 */
class CommonAdapter<D>(create: CommonMgr<D>.() -> Unit) : RecyclerView.Adapter<CommonAdapter.AcroViewHolder<D>>() {

    private val commonMgr by lazy { CommonMgr<D>() }

    init {
        commonMgr.create()
    }

    var emptyEvent: (() -> Unit)? = null

    var itemCompare: ((D, D) -> Boolean)? = null

    private var bind: AcroViewHolder<D>.() -> Unit = {}

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcroViewHolder<D> {
        val acrobatItem = commonMgr.items[viewType]
        val view = LayoutInflater.from(parent.context).inflate(acrobatItem.getResId(), parent, false)
        acrobatItem.onViewCreate(parent, view)
        val viewHolder = AcroViewHolder(view, acrobatItem)
        if (acrobatItem.hasEvent()) {
            view.event({ acrobatItem.click?.apply { this(commonMgr.data[viewHolder.adapterPosition], viewHolder.adapterPosition) } },
                    { acrobatItem.doubleTap?.apply { this(commonMgr.data[viewHolder.adapterPosition], viewHolder.adapterPosition) } },
                    { acrobatItem.longPress?.apply { this(commonMgr.data[viewHolder.adapterPosition], viewHolder.adapterPosition) } })
        }
        viewHolder.bind()
        viewHolder.doBindEvent()
        context = viewHolder.itemView.context
        return viewHolder
    }

    override fun getItemCount(): Int = commonMgr.data.size

    override fun onBindViewHolder(holder: AcroViewHolder<D>, position: Int) {
        holder.acrobatItem.showItem(commonMgr.data[position], position, holder.itemView)
    }

    override fun onBindViewHolder(holder: AcroViewHolder<D>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.acrobatItem.showItem(commonMgr.data[position], position, holder.itemView, payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return commonMgr.getItemConfig(position)
    }

    fun setData(dataList: ArrayList<D>): CommonAdapter<D> {
        if (dataList.isEmpty()) {
            emptyEvent?.apply {
                this()
            }
        }
        val diffCallBack = DiffCallback(commonMgr.data, dataList)
        val calculateDiff = DiffUtil.calculateDiff(diffCallBack)
        calculateDiff.dispatchUpdatesTo(this)
        commonMgr.setData(dataList)
        notifyDataSetChanged()
        return this
    }

    fun addData(dataList: ArrayList<D>): CommonAdapter<D> {
        if (dataList.isEmpty()) {
            emptyEvent?.apply {
                this()
            }
        }
        val diffCallBack = DiffCallback(commonMgr.data, dataList)
        val calculateDiff = DiffUtil.calculateDiff(diffCallBack)
        calculateDiff.dispatchUpdatesTo(this)
        commonMgr.addData(dataList)
        notifyDataSetChanged()
        return this
    }

    fun getData() = commonMgr.data.clone() as ArrayList<D>

    fun bindEvent(click: AcroViewHolder<D>.() -> Unit): CommonAdapter<D> {
        this.bind = click
        return this
    }

    fun itemCompareRule(rule: (D, D) -> Boolean): CommonAdapter<D> {
        this.itemCompare = rule
        return this
    }

    fun notifyItemRemove(pos: Int) {
        val data = getData()
        if (data.isNotEmpty()) {
            if (pos < 0 || pos > data.lastIndex) {
                return
            }
            data.removeAt(pos)
            setData(data)
        }
    }

    private inner class DiffCallback(private var mOldData: List<D>, private var mNewData: List<D>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return mOldData.size
        }

        override fun getNewListSize(): Int {
            return mNewData.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = mOldData.get(oldItemPosition)
            val new = mNewData.get(newItemPosition)
            itemCompare?.apply {
                return invoke(old, new)
            }
            return old.toString().equals(new.toString())
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mOldData.get(oldItemPosition).toString().equals(mNewData.get(newItemPosition).toString())
        }
    }

    class AcroViewHolder<D>(view: View, val acrobatItem: CommonItem<D>) : RecyclerView.ViewHolder(view) {
        private var click: ((Int) -> Unit)? = null
        private var doubleTap: ((Int) -> Unit)? = null
        private var longPress: ((Int) -> Unit)? = null

        fun onClick(c: (Int) -> Unit) {
            if (acrobatItem.hasEvent()) {
                throw IllegalStateException("item has inner event!!!")
            }
            click = c
        }

        fun onDoubleTap(d: (Int) -> Unit) {
            if (acrobatItem.hasEvent()) {
                throw IllegalStateException("item has inner event!!!")
            }
            doubleTap = d
        }

        fun longPress(l: (Int) -> Unit) {
            if (acrobatItem.hasEvent()) {
                throw IllegalStateException("item has inner event!!!")
            }
            longPress = l
        }

        internal fun doBindEvent() {
            if (click != null || doubleTap != null || longPress != null) {
                itemView.event({ click?.apply { this((adapterPosition)) } },
                        { doubleTap?.apply { this(adapterPosition) } },
                        { longPress?.apply { this(adapterPosition) } })
            }
        }
    }
}