package com.viet.news.adapter

import com.viet.news.R
import com.viet.news.core.domain.response.NewsResponse
import com.viet.news.core.ui.BaseAdapter
import com.viet.news.load
import kotlinx.android.synthetic.main.cell_news_picture.view.*

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 05/09/2018 11:49 AM
 * @Version
 */
class NewsCellAdapter : BaseAdapter<NewsResponse.ArticlesBean.UrlToImageBean>() {
    override fun getLayoutId(viewType: Int): Int = R.layout.cell_news_picture

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: NewsResponse.ArticlesBean.UrlToImageBean) {
        holder.itemView.iv_pic.load(t.cover)
    }
}