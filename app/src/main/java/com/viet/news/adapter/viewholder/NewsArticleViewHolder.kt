package com.viet.news.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.viet.news.core.domain.response.Article
import com.viet.news.load
import kotlinx.android.synthetic.main.layout_news_article_single.view.*

/**
 * Created by abhinav.sharma on 02/11/17.
 */
class NewsArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(article: Article) = with(itemView){
        tv_author.text = article.author
        tv_publish_date.text = article.publishedAt
        tv_title.text = article.title
        iv_article_image.load(article.urlToImage!!)
//        Picasso.with(itemView.context).load(article.urlToImage).fit().centerCrop().into(iv_article_image)
    }
}