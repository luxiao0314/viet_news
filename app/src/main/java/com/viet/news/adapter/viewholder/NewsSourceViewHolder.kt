package com.viet.news.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.viet.news.db.SourceEntity
import kotlinx.android.synthetic.main.layout_news_source_single.view.*

/**
 * Created by abhinav.sharma on 01/11/17.
 */
class NewsSourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(source: SourceEntity, listener: (SourceEntity) -> Unit) = with(itemView) {
        tv_title.text = source.name
        tv_source_description.text = source.description
        tv_category.text = source.category
        itemView.setOnClickListener { listener(source) }
    }
}