package com.viet.follow.adapter

import android.support.v7.widget.GridLayoutManager
import android.widget.ImageView
import android.widget.TextView
import com.viet.follow.R
import com.viet.news.core.domain.response.NewsListBean
import com.viet.news.core.ext.load
import com.viet.news.core.ui.BaseAdapter
import kotlinx.android.synthetic.main.cell_personal_page_picture_three.view.*
import javax.inject.Inject

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 5:49 PM
 * @Version
 */
class PersonalPageAdapter @Inject constructor() : BaseAdapter<NewsListBean>() {

    override fun getItemViewType(position: Int): Int {
        return getData()[position].contentType
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.cell_personal_page_picture_three
            else -> R.layout.cell_personal_page_picture_one
        }
    }

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: NewsListBean) {
        when (getItemViewType(position)) {
            1 -> {
                holder.itemView.tv_des.text = t.contentTitle
                holder.itemView.tv_time.text = t.createDateTime
                holder.itemView.rv_news_cell.layoutManager = GridLayoutManager(context, 3)
                val cellAdapter = NewsCellAdapter()
                holder.itemView.rv_news_cell.adapter = cellAdapter
//                cellAdapter.addData(t.urlToImage)
            }
            else -> {
                holder.itemView.findViewById<ImageView>(R.id.iv_pic).load(t.contentDetail)
                holder.itemView.findViewById<TextView>(R.id.tv_des).text = t.contentTitle
                holder.itemView.findViewById<TextView>(R.id.tv_time).text = t.createDateTime
            }
        }
    }
}