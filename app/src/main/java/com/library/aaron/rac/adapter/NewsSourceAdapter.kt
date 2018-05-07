package com.library.aaron.rac.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.library.aaron.rac.adapter.viewholder.NewsSourceViewHolder
import com.library.aaron.rac.db.SourceEntity
import com.library.aaron.rac.R
import com.library.aaron.rac.inflate

/**
 * Created by abhinav.sharma on 01/11/17.
 */
class NewsSourceAdapter(private val listener: (SourceEntity) -> Unit, private var sourceList: List<SourceEntity>) : RecyclerView.Adapter<NewsSourceViewHolder>() {


    override fun getItemCount(): Int {
        return sourceList.size
    }

    private fun getItem(position: Int): SourceEntity {
        return sourceList[position]
    }

    override fun onBindViewHolder(holder: NewsSourceViewHolder, position: Int) =
            holder.bind(getItem(position), listener)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsSourceViewHolder =
            NewsSourceViewHolder(parent.inflate(R.layout.layout_news_source_single))

    fun updateDataSet(data: List<SourceEntity>){
        sourceList = data
        notifyDataSetChanged()
    }
}