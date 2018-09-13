package com.viet.follow.adapter

import com.viet.follow.R
import com.viet.news.core.domain.response.UserInfo
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
class FunsAndFollowAdapter @Inject constructor() : BaseAdapter<UserInfo>() {

    override fun getLayoutId(viewType: Int): Int = R.layout.cell_funs_and_follow

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: UserInfo) {
        holder.itemView.iv_article_image.loadCircle(t.avatar)
        holder.itemView.tv_title.text = t.nick_name
        holder.itemView.tv_funs.text = context.resources.getString(R.string.funs) + t.fans_count
        if (t.follow_flag) {
            if (t.self_flag) {
                holder.itemView.btn_follow.text = context.getString(R.string.follow_each_other)
            } else {
                holder.itemView.btn_follow.text = context.getString(R.string.has_follow)
            }
            holder.itemView.btn_follow.isEnabled = false
            holder.itemView.btn_follow.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.ic_hook), null, null, null)
            holder.itemView.btn_follow.compoundDrawablePadding = 4
        } else {
            holder.itemView.btn_follow.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            holder.itemView.btn_follow.isEnabled = true
            holder.itemView.btn_follow.text = context.getString(R.string.add_follow)
        }
    }
}