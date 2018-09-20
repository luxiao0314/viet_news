package com.viet.follow.adapter

import android.widget.ImageView
import android.widget.TextView
import com.viet.follow.R
import com.viet.follow.viewmodel.PersonalPageModel
import com.viet.news.core.domain.response.ImageEntity
import com.viet.news.core.domain.response.NewsListBean
import com.viet.news.core.dsl.adapter.CommonAdapter
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ext.grid
import com.viet.news.core.ext.load
import com.viet.news.core.ui.BaseAdapter
import com.viet.news.core.ui.widget.BehaviorBar
import com.viet.news.core.utils.DateUtils
import com.viet.news.webview.WebActivity
import kotlinx.android.synthetic.main.cell_news_picture.view.*
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

    lateinit var model: PersonalPageModel

    override fun getItemViewType(position: Int): Int {
        return getData()[position].content.contentType
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.cell_personal_page_picture_three  //3图
            2 -> R.layout.cell_personal_page_picture_one    //1图
            else -> R.layout.cell_personal_page_text_only //无图(文字)
        }
    }

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: NewsListBean) {
        holder.itemView.findViewById<TextView>(R.id.tv_des)?.text = t.content.contentTitle
        holder.itemView.findViewById<TextView>(R.id.tv_time)?.text = DateUtils.getTimestamp(Date(t.content.createDateTime))

        holder.itemView.findViewById<BehaviorBar>(R.id.behaviorBar)?.setReadNumStatus(t.content.contentProfit)
        holder.itemView.findViewById<BehaviorBar>(R.id.behaviorBar)?.setLikeStatus(t.content.likeFlag, t.content.likeNumber)
        holder.itemView.findViewById<BehaviorBar>(R.id.behaviorBar)?.setFavoriteStatus(t.content.collectionFlag, t.content.collectionNumber)

        holder.itemView.clickWithTrigger { WebActivity.launch(context, t.content.contentDetail) }
        holder.itemView.findViewById<BehaviorBar>(R.id.behaviorBar)?.setClickDelegate {
            onLikeClick = {
                if (!t.content.likeFlag) {
                    t.content.likeFlag = it(t.content.likeFlag)
                    model.like(context, t.content.id.toString())
                }
            }
            onCollectionClick = {
                t.content.collectionFlag = it(t.content.collectionFlag)
                model.collection(context, t.content.id.toString())
            }
        }

        when (getItemViewType(position)) {
            1 -> {
                holder.itemView.rv_news_cell.grid(3)
                holder.itemView.rv_news_cell.adapter = CommonAdapter<ImageEntity> {
                    itemDSL {
                        resId(R.layout.cell_news_picture)
                        showItem { t, pos, view -> view.iv_pic.load(t.cover) }
                        onClick { it, pos -> WebActivity.launch(context, t.content.contentDetail) }
                    }
                }.addData(t.image_array)
            }
            2 -> holder.itemView.findViewById<ImageView>(R.id.iv_pic)?.load(t.image_array[0].cover)
        }
    }
}