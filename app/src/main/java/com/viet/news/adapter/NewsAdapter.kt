package com.viet.news.adapter

import android.support.v7.widget.GridLayoutManager
import android.widget.ImageView
import android.widget.TextView
import com.viet.news.R
import com.viet.news.core.domain.response.NewsResponse
import com.viet.news.core.ui.BaseAdapter
import com.viet.news.load
import kotlinx.android.synthetic.main.cell_news_picture_three.view.*
import javax.inject.Inject

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 5:49 PM
 * @Version
 */
class NewsAdapter @Inject constructor() : BaseAdapter<NewsResponse.ArticlesBean>() {

    override fun getItemViewType(position: Int): Int {
        return if (getData()[position].urlToImage?.size == 3) {
            1
        } else if (getData()[position].urlToImage?.size == 1 && getData()[position].author.isNullOrEmpty().not()) {
            2
        } else if (getData()[position].urlToImage?.size == 0) {
            3
        } else {
            4
        }
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.cell_news_picture_three
            2 -> R.layout.cell_news_picture_one
            3 -> R.layout.cell_news_picture_none
            else -> R.layout.cell_news_picture_only
        }
    }

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: NewsResponse.ArticlesBean) {
        when (getItemViewType(position)) {
            1 -> {
                holder.itemView.iv_article_image.load(t.authorUrl)
                holder.itemView.tv_title.text = t.title
                holder.itemView.tv_des.text = t.description
                holder.itemView.rv_news_cell.layoutManager = GridLayoutManager(context,3)
                val cellAdapter = NewsCellAdapter()
                holder.itemView.rv_news_cell.adapter = cellAdapter
                cellAdapter.addData(t.urlToImage)
            }
            2 -> {
                holder.itemView.findViewById<ImageView>(R.id.iv_article_image).load(t.authorUrl)
                holder.itemView.findViewById<ImageView>(R.id.iv_pic).load(t.urlToImage?.get(0)?.cover)
                holder.itemView.findViewById<TextView>(R.id.tv_title).text = t.title
                holder.itemView.findViewById<TextView>(R.id.tv_des).text = t.description
            }
            3 -> {
                holder.itemView.findViewById<TextView>(R.id.tv_title).text = t.title
                holder.itemView.findViewById<TextView>(R.id.tv_des).text = t.description
            }
            4 -> {
                holder.itemView.findViewById<ImageView>(R.id.iv_pic).load(t.urlToImage?.get(0)?.cover)
            }
        }
    }
}