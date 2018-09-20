package com.viet.follow.adapter

import android.view.View
import com.viet.follow.R
import com.viet.news.core.domain.response.UserInfoResponse
import com.viet.news.core.dsl.adapter.CommonItem
import com.viet.news.core.ext.loadCircle
import kotlinx.android.synthetic.main.cell_funs_and_follow.view.*

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 20/09/2018 11:41 AM
 * @Version
 */
class FuncAndFollowItem : CommonItem<UserInfoResponse>() {

    override fun getResId(): Int = R.layout.cell_funs_and_follow

    override fun showItem(t: UserInfoResponse, pos: Int, view: View) {
        if (t.follow_flag) {
//            if (t.self_flag) {
//                holder.itemView.btn_follow.text = context.getString(R.string.follow_each_other)
//            } else {
//                holder.itemView.btn_follow.text = context.getString(R.string.has_follow)
//            }
            view.btn_follow.text = view.context.getString(R.string.has_follow)
            view.btn_follow.isSelected = false
            view.btn_follow.setCompoundDrawablesWithIntrinsicBounds(view.context.resources.getDrawable(R.drawable.ic_hook), null, null, null)
            view.btn_follow.compoundDrawablePadding = 4
        } else {
            view.btn_follow.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            view.btn_follow.isSelected = true
            view.btn_follow.text = view.context.getString(R.string.add_follow)
        }
        view.iv_article_image.loadCircle(t.avatar)
        view.tv_title.text = t.nick_name
        view.tv_funs.text = view.context.resources.getString(R.string.funs) + t.fans_count
        view.btn_follow.visibility = if (t.self_flag) View.GONE else View.VISIBLE
    }
}