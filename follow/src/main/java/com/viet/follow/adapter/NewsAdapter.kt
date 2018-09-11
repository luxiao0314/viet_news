package com.viet.follow.adapter

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.widget.ImageView
import android.widget.TextView
import com.viet.follow.R
import com.viet.follow.activity.PersonalPageActivity
import com.viet.news.core.domain.response.NewsListBean
import com.viet.news.core.ext.click
import com.viet.news.core.ext.load
import com.viet.news.core.ext.loadCircle
import com.viet.news.core.ui.BaseAdapter
import kotlinx.android.synthetic.main.cell_news_picture_three.view.*
import javax.inject.Inject

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 5:49 PM
 * @Version
 */
class NewsAdapter @Inject constructor() : BaseAdapter<NewsListBean>() {

    override fun getItemViewType(position: Int): Int {
        return getData()[position].contentType
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.cell_news_picture_three
            2 -> R.layout.cell_news_picture_one
            3 -> R.layout.cell_news_picture_none
            else -> R.layout.cell_news_picture_only
        }
    }

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: NewsListBean) {
        when (getItemViewType(position)) {
            1 -> {
                holder.itemView.iv_article_image.loadCircle(t.contentDetail)
                holder.itemView.tv_title.text = t.contentTitle
                holder.itemView.tv_time.text = t.createDateTime
                holder.itemView.tv_des.text = t.contentTitle
                holder.itemView.rv_news_cell.layoutManager = GridLayoutManager(context, 3)
                val cellAdapter = NewsCellAdapter()
                holder.itemView.rv_news_cell.adapter = cellAdapter
//                cellAdapter.addData(t.urlToImage)
                holder.itemView.iv_article_image.click { context.startActivity(Intent(context, PersonalPageActivity::class.java)) }
            }
            2 -> {
                holder.itemView.findViewById<ImageView>(R.id.iv_article_image).loadCircle(t.contentDetail)
                holder.itemView.findViewById<ImageView>(R.id.iv_pic).load(t.contentDetail)
                holder.itemView.findViewById<TextView>(R.id.tv_title).text = t.contentTitle
                holder.itemView.findViewById<TextView>(R.id.tv_time).text = t.createDateTime
                holder.itemView.findViewById<TextView>(R.id.tv_des).text = t.contentTitle
                holder.itemView.findViewById<ImageView>(R.id.iv_article_image).click { context.startActivity(Intent(context, PersonalPageActivity::class.java)) }
            }
            3 -> {
                holder.itemView.findViewById<TextView>(R.id.tv_title).text = t.contentTitle
                holder.itemView.findViewById<TextView>(R.id.tv_time).text = t.createDateTime
                holder.itemView.findViewById<TextView>(R.id.tv_des).text = t.contentTitle
            }
            4 -> holder.itemView.findViewById<ImageView>(R.id.iv_pic).load(t.contentDetail)
        }
    }
}