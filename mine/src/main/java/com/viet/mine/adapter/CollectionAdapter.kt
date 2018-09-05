package com.viet.mine.adapter

import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.safframework.ext.clickWithTrigger
import com.viet.mine.R
import com.viet.news.core.config.GlideApp
import com.viet.news.core.domain.response.NewsResponse
import com.viet.news.core.ui.BaseAdapter
import com.viet.news.core.ui.widget.SwipeLayout


class CollectionAdapter : BaseAdapter<NewsResponse.ArticlesBean>(), SwipeLayout.OnSwipingListener {
    private var layouts: HashSet<SwipeLayout> = HashSet()

    override fun getItemViewType(position: Int): Int {
        return if (getData()[position].urlToImage?.size == 0) {
            1
        } else {
            2
        }
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.collection_cell_news_text
            else -> R.layout.collection_cell_news_picture
        }
    }

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: NewsResponse.ArticlesBean) {
        when (getItemViewType(position)) {

            1 -> {
                holder.itemView.findViewById<TextView>(R.id.tv_des).text = t.description
            }
            2 -> {
                GlideApp.with(context).load(t.urlToImage?.get(0)?.cover).placeholder(R.drawable.shape_default_rec_bg)
                        .error(R.drawable.shape_default_rec_bg)
                        .into(holder.itemView.findViewById<ImageView>(R.id.iv_pic))
                holder.itemView.findViewById<TextView>(R.id.tv_des).text = t.description
            }
        }
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val swipeLayout = holder.itemView.findViewById<SwipeLayout>(R.id.swipe)
        swipeLayout.setOnSwipingListener(this)
        holder.itemView.findViewById<TextView>(R.id.tv_delete).clickWithTrigger {
            Toast.makeText(context, "删除", Toast.LENGTH_SHORT).show();
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