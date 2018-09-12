package com.viet.mine.adapter

import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.safframework.ext.clickWithTrigger
import com.viet.mine.R
import com.viet.news.core.domain.response.CollectionListBean
import com.viet.news.core.ext.load
import com.viet.news.core.ui.BaseAdapter
import com.viet.news.core.ui.widget.SwipeLayout
import javax.inject.Inject

/**
 * @Description 用户收藏适配器
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
class CollectionAdapter @Inject constructor() : BaseAdapter<CollectionListBean>(), SwipeLayout.OnSwipingListener {


    private var layouts: HashSet<SwipeLayout> = HashSet()

    override fun getItemViewType(position: Int): Int {
        return getData()[position].content.contentType
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            0 -> R.layout.collection_cell_news_text
            else -> R.layout.collection_cell_news_picture
        }
    }

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: CollectionListBean) {
        when (getItemViewType(position)) {
            0 -> {
                holder.itemView.findViewById<TextView>(R.id.tv_des).text = t.content.contentTitle
            }
            else -> {
                holder.itemView.findViewById<ImageView>(R.id.iv_pic).load(t.image_array[0].cover)
                holder.itemView.findViewById<TextView>(R.id.tv_des).text = t.content.contentTitle
            }
        }
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val swipeLayout = holder.itemView.findViewById<SwipeLayout>(R.id.swipe)
        swipeLayout.setOnSwipingListener(this)
        holder.itemView.findViewById<TextView>(R.id.tv_delete).clickWithTrigger {
            Toast.makeText(context, "删除", Toast.LENGTH_SHORT).show()
            swipeLayout.closeItem(true)
        }
    }

    override fun onOpened(layout: SwipeLayout) {
        System.out.println("打开了");
        layouts.add(layout);
    }

    override fun onClosed(layout: SwipeLayout) {
        System.out.println("关闭了");
        layouts.remove(layout);
    }

    override fun onSwiping(layout: SwipeLayout) {
        for (layout in layouts) {
            layout.closeItem(true)
        }
        layouts.clear()
    }

    fun closeAllItem() {
        for (layout in layouts) {
            layout.closeItem(true)
        }
        layouts.clear()
    }

}