package com.viet.follow.adapter

import com.viet.follow.R
import com.viet.news.core.domain.response.DataBean
import com.viet.news.core.ext.loadCircle
import com.viet.news.core.ui.BaseAdapter
import kotlinx.android.synthetic.main.cell_funs_and_follow.view.*
import javax.inject.Inject

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 07/09/2018 2:27 PM
 * @Version
 */
class FunsAndFollowAdapter @Inject constructor() : BaseAdapter<DataBean>() {

    override fun getLayoutId(viewType: Int): Int = R.layout.cell_funs_and_follow

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: DataBean) {
        holder.itemView.iv_article_image.loadCircle(t.cover)
        holder.itemView.tv_title.text = t.title
        holder.itemView.tv_funs.text = context.resources.getString(R.string.funs) + t.funsNum
        if (t.follow!!) {
            holder.itemView.btn_follow.isEnabled = false
            holder.itemView.btn_follow.text = "已关注"
            holder.itemView.btn_follow.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.ic_hook), null, null, null)
            holder.itemView.btn_follow.compoundDrawablePadding = 4
        } else {
            holder.itemView.btn_follow.isEnabled = true
            holder.itemView.btn_follow.text = "+ 关注"
        }
    }
}