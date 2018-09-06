package cn.magicwindow.core.ui

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View
import com.safframework.ext.clickWithTrigger
import com.viet.news.core.R
import org.jetbrains.annotations.NotNull


/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 27/03/2018 00:56
 * @description
 */
class ItemClickSupport private constructor(private val mRecyclerView: RecyclerView) {
    private var mOnItemClickListener: OnItemClickListener? = null
    private val childListenerMap = hashMapOf<Int, OnChildClickListener>()

    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    private val mAttachListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            //item 点击
            if (mOnItemClickListener != null) {
                view.clickWithTrigger { v ->
                    if (mOnItemClickListener != null) {
                        val holder = mRecyclerView.getChildViewHolder(v)
                        mOnItemClickListener?.onItemClicked(mRecyclerView, holder.adapterPosition, v)
                    }
                }
            }

            //子 View 点击
            if (childListenerMap.isNotEmpty()) {
                for (key in childListenerMap.keys) {
                    view.findViewById<View>(key)?.clickWithTrigger { v ->
                        val holder = mRecyclerView.findContainingViewHolder(v)
                        if (holder != null) {
                            childListenerMap[key]!!.onChildClicked(mRecyclerView, holder.adapterPosition, v)
                        }
                    }
                }

            }

            //Item 长按点击
            if (mOnItemLongClickListener != null) {
                view.setOnLongClickListener(View.OnLongClickListener { v ->
                    if (mOnItemLongClickListener != null) {
                        val holder = mRecyclerView.getChildViewHolder(v)
                        return@OnLongClickListener mOnItemLongClickListener!!.onItemLongClicked(mRecyclerView, holder.adapterPosition, v)
                    }
                    false
                })
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {

        }
    }

    init {
        mRecyclerView.setTag(R.id.item_click_support, this)
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener)
    }


    fun addOnChildClickListener(@IdRes resId: Int, @NotNull listener: OnChildClickListener): ItemClickSupport {
        childListenerMap[resId] = listener
        return this
    }

    fun addOnItemClickListener(@NotNull listener: OnItemClickListener): ItemClickSupport {
        mOnItemClickListener = listener
        return this
    }

    fun addOnItemLongClickListener(@NotNull listener: OnItemLongClickListener): ItemClickSupport {
        mOnItemLongClickListener = listener
        return this
    }

    private fun detach(view: RecyclerView) {
        view.removeOnChildAttachStateChangeListener(mAttachListener)
        view.setTag(R.id.item_click_support, null)
    }

    // 子 View点击接口
    interface OnChildClickListener {
        fun onChildClicked(recyclerView: RecyclerView, position: Int, v: View)
    }

    // 点击接口
    interface OnItemClickListener {
        fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View)
    }

    // 长按接口
    interface OnItemLongClickListener {
        fun onItemLongClicked(recyclerView: RecyclerView, position: Int, v: View): Boolean
    }

    companion object {

        fun addTo(view: RecyclerView): ItemClickSupport {
            var support: ItemClickSupport? = view.getTag(R.id.item_click_support) as ItemClickSupport?
            if (support == null) {
                support = ItemClickSupport(view)
            }
            return support
        }

        fun removeFrom(view: RecyclerView): ItemClickSupport? {
            val support = view.getTag(R.id.item_click_support) as ItemClickSupport
            support.detach(view)
            return support
        }
    }
}