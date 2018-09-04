package com.viet.news.adapter

import com.viet.news.R
import com.viet.news.core.ui.BaseAdapter
import javax.inject.Inject

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 5:49 PM
 * @Version
 */
class NewsAdapter @Inject constructor() : BaseAdapter<Any>() {

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.cell_news_picture_three
            2 -> R.layout.cell_news_picture_one
            3 -> R.layout.cell_news_picture_none
            else -> R.layout.cell_news_picture_only
        }
    }

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: Any) {

    }
}