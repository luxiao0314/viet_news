package com.viet.mine.adapter

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.safframework.ext.clickWithTrigger
import com.viet.mine.R
import com.viet.news.core.domain.response.NewsListBean
import com.viet.news.core.ext.load
import com.viet.news.core.ui.BaseAdapter
import com.viet.news.core.ui.widget.SwipeLayout
import com.viet.news.core.utils.DateUtils
import java.util.*
import javax.inject.Inject

/**
 * @Description 用户收藏适配器
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
class CollectionAdapter @Inject constructor() : BaseAdapter<NewsListBean>(), SwipeLayout.OnSwipingListener {

    private var layouts: HashSet<SwipeLayout> = HashSet()
    private var mDelegate: Delegate? = null

    override fun getItemViewType(position: Int): Int {
        return getData()[position].content.contentType
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.cell_collection_picture_one
            2 -> R.layout.cell_collection_picture_one
            else -> R.layout.cell_collection_text_only
        }
    }

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: NewsListBean) {

        holder.itemView.findViewById<TextView>(R.id.tv_des)?.text = t.content.contentTitle
        holder.itemView.findViewById<TextView>(R.id.tv_time)?.text = DateUtils.getTimestamp(Date(t.content.createDateTime))
        val swipeLayout = holder.itemView.findViewById<SwipeLayout>(R.id.swipe)
        val content = holder.itemView.findViewById<LinearLayout>(R.id.content)
        swipeLayout.setOnSwipingListener(this)
        content.clickWithTrigger {
            mDelegate?.onItemClick(t.content.contentDetail!!)
            closeAllItem()
        }
        holder.itemView.findViewById<TextView>(R.id.tv_delete).clickWithTrigger {
            Toast.makeText(context, "删除", Toast.LENGTH_SHORT).show()
            swipeLayout.closeItem(true)
            list.removeAt(position)
            notifyDataSetChanged()
            mDelegate?.onItemDelete(t.content.id.toString())
        }

        when (getItemViewType(position)) {
            2 -> holder.itemView.findViewById<ImageView>(R.id.iv_pic)?.load(t.image_array[0].cover)
        }
    }

    override fun onOpened(layout: SwipeLayout) {
        System.out.println("打开了")
        var l2: SwipeLayout? = null
        layouts.add(layout)
        for (l in layouts) {
            if (l != layout) {
                l2 = l
                l.closeItem(true)
                break
            }
        }
        l2?.let { layouts.remove(l2) }
    }

    override fun onClosed(layout: SwipeLayout) {
        System.out.println("关闭了")
        layouts.remove(layout)
    }

    override fun onSwiping(layout: SwipeLayout) {
        for (layout in layouts) {
            layout.closeItem(true)
        }
        layouts.clear()
    }

    private fun setDelegate(delegate: ClickDelegate): CollectionAdapter {
        mDelegate = delegate
        return this
    }

    fun closeAllItem() {
        for (layout in layouts) {
            layout.closeItem(true)
        }
        layouts.clear()
    }

    /**
     * 设置点击事件
     * @param init [@kotlin.ExtensionFunctionType] Function1<ClickDelegate, Unit>
     */
    fun setClickDelegate(init: ClickDelegate.() -> Unit) {
        val delegate = ClickDelegate()
        delegate.init()
        setDelegate(delegate)
    }

    /**
     * 点击事件监听代理
     */
    interface Delegate {
        fun onItemClick(url: String)
        fun onItemDelete(id: String)
    }

    class ClickDelegate : Delegate {
        var onItemDelete: ((id: String) -> Unit)? = null

        var onItemClick: ((url: String) -> Unit)? = null

        override fun onItemClick(url: String) {
            onItemClick?.let { it(url) }
        }

        override fun onItemDelete(id: String) {
            onItemDelete?.let { it(id) }
        }
    }

}