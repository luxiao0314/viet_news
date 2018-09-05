package com.viet.news.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.viet.news.adapter.viewholder.NewsSourceViewHolder
import com.viet.news.db.SourceEntity
import com.viet.news.R
import com.viet.news.core.ui.widget.SwipeLayout
import com.viet.news.core.ext.inflate
import android.widget.Toast
import com.safframework.ext.clickWithTrigger
import kotlinx.android.synthetic.main.layout_news_source_single.view.*


/**
 * Created by abhinav.sharma on 01/11/17.
 */
class NewsSourceAdapter(private val listener: (SourceEntity) -> Unit, private var sourceList: List<SourceEntity>) : RecyclerView.Adapter<NewsSourceViewHolder>(), SwipeLayout.OnSwipingListener {
    private val layouts: HashSet<SwipeLayout> = HashSet()

    override fun getItemCount(): Int {
        return sourceList.size
    }

    private fun getItem(position: Int): SourceEntity {
        return sourceList[position]
    }

    override fun onBindViewHolder(holder: NewsSourceViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
        val view =  holder.itemView as SwipeLayout
        view.tv_add.clickWithTrigger {
            Toast.makeText(it.context, "关注", Toast.LENGTH_SHORT).show()
            view.closeItem(true)
        }
        view.tv_delete.clickWithTrigger {
            Toast.makeText(it.context, "删除", Toast.LENGTH_SHORT).show()
            view.closeItem(true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsSourceViewHolder {
        val swipeLayout = parent.inflate(R.layout.layout_news_source_single) as SwipeLayout
        swipeLayout.setOnSwipingListener(this)
        return NewsSourceViewHolder(swipeLayout)
    }

    fun updateDataSet(data: List<SourceEntity>) {
        sourceList = data
        notifyDataSetChanged()
    }


    override fun onOpened(layout: SwipeLayout) {
        System.out.println("打开了")
        layouts.add(layout)
    }

    override fun onClosed(layout: SwipeLayout) {
        System.out.println("关闭了")
        layouts.remove(layout)
    }

    override fun onSwiping(layout: SwipeLayout) {
        System.out.println("关闭所有")
        closeAllItem()
    }

    fun closeAllItem() {
        for (layout in layouts) {
            layout.closeItem(true)
        }
        layouts.clear()
    }
}