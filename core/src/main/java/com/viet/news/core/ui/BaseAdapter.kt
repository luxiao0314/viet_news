package com.viet.news.core.ui

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.IntRange
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 22/12/2017 2:59 PM
 * @description
 */
abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    private var checkBoxVisibility = false

    protected var list: MutableList<T> = mutableListOf()

    lateinit var context: Context

    fun getData(): MutableList<T> = list

    //增加一列数据
    fun addData(newData: List<T>?) {
        if (newData == null || newData.isEmpty()) return
        list.addAll(newData)
        notifyItemRangeInserted(list.size - newData.size, newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    //根据position插入集合
    fun addData(@IntRange(from = 0) position: Int, newData: Collection<T>) {
        if (newData == null || newData.isEmpty()) return
        list.addAll(position, newData)
        notifyItemRangeInserted(position, newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    fun addData(@IntRange(from = 0) position: Int, newData: List<T>) {
        if (newData == null || newData.isEmpty()) return
        list.addAll(position, newData)
        compatibilityDataSizeChanged(newData.size)
    }

    //增加一条数据
    fun addData(data: T?) {
        if (data == null) return
        list.add(data)
        notifyItemInserted(list.size)
        compatibilityDataSizeChanged(1)
    }

    //下拉刷新使用,如果null，需要清除list
    fun setData(data: List<T>?) {
        list.clear()
        if (data == null || data.isEmpty()) return
        list.addAll(data)
        notifyDataSetChanged()
    }

    //根据position插入item
    fun setData(@IntRange(from = 0) index: Int, data: T) {
        list[index] = data
        notifyItemChanged(index)
    }

    private fun compatibilityDataSizeChanged(size: Int) {
        val dataSize =  list.size
        if (dataSize == size) {
            notifyDataSetChanged()
        }
    }

    fun setCheckBoxVisibility(visibility: Boolean) {
        this.checkBoxVisibility = visibility
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if (list.isNotEmpty()) list.size else 0

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        onBindViewHolderImpl(holder, position, list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        this.context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(getLayoutId(viewType), parent, false)
        return BaseViewHolder(view)
    }

    abstract fun getLayoutId(viewType: Int): Int

    abstract fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: T)

    open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val views: SparseArray<View>? = null

        fun <T : View> getView(@IdRes viewId: Int): T {
            var view: View? = views?.get(viewId)
            if (view == null) {
                view = itemView.findViewById(viewId)
                views?.put(viewId, view)
            }
            return view as T
        }

    }
}

