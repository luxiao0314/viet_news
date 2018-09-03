package com.viet.news.adapter

import com.viet.news.R
import com.viet.news.core.ui.BaseAdapter

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 5:49 PM
 * @Version
 */
class ChannelPageAdapter : BaseAdapter<Any>() {


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.abc_action_bar_title_item
            2 -> R.layout.abc_action_bar_title_item
            else -> R.layout.abc_action_bar_title_item
        }
    }

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: Any) {

    }
}