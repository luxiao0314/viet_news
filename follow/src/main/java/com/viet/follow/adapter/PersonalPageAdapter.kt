package com.viet.follow.adapter

import android.support.v7.widget.GridLayoutManager
import android.widget.ImageView
import android.widget.TextView
import com.viet.follow.R
import com.viet.news.core.domain.response.ArticlesBean
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
class PersonalPageAdapter @Inject constructor() : BaseAdapter<ArticlesBean>() {

    override fun getItemViewType(position: Int): Int {
        return if (getData()[position].urlToImage?.size == 3) {
            1
        } else {
            2
        }
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.cell_personal_page_picture_three
            else -> R.layout.cell_personal_page_picture_one
        }
    }

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: ArticlesBean) {
        when (getItemViewType(position)) {
            1 -> {
                holder.itemView.tv_des.text = t.description
                holder.itemView.tv_time.text = t.time
                holder.itemView.rv_news_cell.layoutManager = GridLayoutManager(context, 3)
                val cellAdapter = NewsCellAdapter()
                holder.itemView.rv_news_cell.adapter = cellAdapter
                cellAdapter.addData(t.urlToImage)
            }
            else -> {
                holder.itemView.findViewById<ImageView>(R.id.iv_pic).load(t.urlToImage?.get(0)?.cover)
                holder.itemView.findViewById<TextView>(R.id.tv_des).text = t.description
                holder.itemView.findViewById<TextView>(R.id.tv_time).text = t.time
            }
        }
    }
}