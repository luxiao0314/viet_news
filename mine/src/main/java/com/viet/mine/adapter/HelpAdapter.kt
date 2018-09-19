package com.viet.mine.adapter

import com.viet.mine.R
import com.viet.mine.bean.HelpBean
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ui.BaseAdapter
import com.viet.news.core.ui.widget.CommonItem
import com.viet.news.webview.WebActivity
import javax.inject.Inject

/**
 * @Description
 * @author null
 * @date 2018/9/19
 * @Email zongjia.long@merculet.io
 * @Version
 */
class HelpAdapter @Inject constructor() : BaseAdapter<HelpBean>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_help
    }

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: HelpBean) {
        val item = holder.itemView.findViewById<CommonItem>(R.id.item_help)
        item.setRightText(t.name)
        item.clickWithTrigger {
            WebActivity.launch(context, "http://www.baidu.com")
        }
    }

}