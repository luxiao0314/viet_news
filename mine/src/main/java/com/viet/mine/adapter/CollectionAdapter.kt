package com.viet.mine.adapter

import android.support.v7.widget.GridLayoutManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.safframework.ext.clickWithTrigger
import com.viet.follow.adapter.NewsCellAdapter
import com.viet.mine.R
import com.viet.news.core.domain.response.NewsListBean
import com.viet.news.core.ext.load
import com.viet.news.core.ui.BaseAdapter
import com.viet.news.core.ui.widget.SwipeLayout
import com.viet.news.core.utils.DateUtils
import com.viet.news.webview.WebActivity
import kotlinx.android.synthetic.main.cell_collection_picture_three.view.*
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

    override fun getItemViewType(position: Int): Int {
        return getData()[position].content.contentType
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.cell_collection_picture_three
            2 -> R.layout.cell_collection_picture_one
            else -> R.layout.cell_collection_text_only
        }
    }

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: NewsListBean) {

        holder.itemView.findViewById<TextView>(R.id.tv_des)?.text = t.content.contentTitle
        holder.itemView.findViewById<TextView>(R.id.tv_time)?.text = DateUtils.getTimestamp(Date(t.content.createDateTime))

        holder.itemView.clickWithTrigger { WebActivity.launch(context, t.content.contentDetail) }

        val swipeLayout = holder.itemView.findViewById<SwipeLayout>(R.id.swipe)
        swipeLayout.setOnSwipingListener(this)
        holder.itemView.findViewById<TextView>(R.id.tv_delete).clickWithTrigger {
            Toast.makeText(context, "删除", Toast.LENGTH_SHORT).show()
            swipeLayout.closeItem(true)
        }

        when (getItemViewType(position)) {
            1 -> {
                holder.itemView.rv_news_cell.layoutManager = GridLayoutManager(context, 3)
                val cellAdapter = NewsCellAdapter()
                holder.itemView.rv_news_cell.adapter = cellAdapter
                cellAdapter.addData(t.image_array)
            }
            2 -> holder.itemView.findViewById<ImageView>(R.id.iv_pic)?.load(t.image_array[0].cover)
        }
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