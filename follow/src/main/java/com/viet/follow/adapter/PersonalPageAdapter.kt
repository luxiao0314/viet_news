package com.viet.follow.adapter

import android.support.v7.widget.GridLayoutManager
import android.widget.ImageView
import android.widget.TextView
import com.viet.follow.R
import com.viet.news.core.domain.response.NewsListBean
import com.viet.news.core.ext.load
import com.viet.news.core.ui.BaseAdapter
import com.viet.news.core.ui.widget.BehaviorBar
import com.viet.news.core.utils.DateUtils
import kotlinx.android.synthetic.main.cell_personal_page_picture_three.view.*
import java.util.*
import javax.inject.Inject

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 5:49 PM
 * @Version
 */
class PersonalPageAdapter @Inject constructor() : BaseAdapter<NewsListBean>() {

    override fun getItemViewType(position: Int): Int {
        return getData()[position].content.contentType
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.cell_personal_page_picture_three
            else -> R.layout.cell_personal_page_picture_one
        }
    }

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: NewsListBean) {
        holder.itemView.findViewById<ImageView>(R.id.iv_pic)?.load(t.image_array[0].cover)
        holder.itemView.findViewById<TextView>(R.id.tv_des)?.text = t.content.contentTitle
        holder.itemView.findViewById<TextView>(R.id.tv_time)?.text = DateUtils.getTimestamp(Date(t.content.createDateTime))

        holder.itemView.behaviorBar?.setClickDelegate {
            onLikeClick = { isLiked, num, id, func -> delegate?.onLikeClick(isLiked, num, t.content.id, func) }
            onFavoriteClick = { isFavorite, num, id, func -> delegate?.onLikeClick(isFavorite, num, t.content.id, func) }
        }
        when (getItemViewType(position)) {
            1 -> {
                holder.itemView.rv_news_cell.layoutManager = GridLayoutManager(context, 3)
                val cellAdapter = NewsCellAdapter()
                holder.itemView.rv_news_cell.adapter = cellAdapter
                cellAdapter.addData(t.image_array)
            }
        }
    }

    var delegate: BehaviorBar.ClickDelegate? = null
    fun setClickDelegate(init: BehaviorBar.ClickDelegate.() -> Unit) {
        val delegate = BehaviorBar.ClickDelegate()
        delegate.init()
        this.delegate = delegate
    }
}