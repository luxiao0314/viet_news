package com.library.aaron.module.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.library.aaron.module.R
import com.library.aaron.module.adapter.viewholder.NewsArticleViewHolder
import com.library.aaron.module.inflate
import com.library.aaron.module.ui.model.Article

/**
 * Created by abhinav.sharma on 02/11/17.
 */
class NewsArticleAdapter(private val articles: List<Article>)
    : RecyclerView.Adapter<NewsArticleViewHolder>() {

    override fun onBindViewHolder(holder: NewsArticleViewHolder, position: Int) = holder.bind(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsArticleViewHolder = NewsArticleViewHolder(parent.inflate(R.layout.layout_news_article_single))


    override fun getItemCount(): Int = articles.size

    private fun getItem(position: Int): Article = articles[position]

}