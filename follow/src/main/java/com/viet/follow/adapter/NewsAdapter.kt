package com.viet.follow.adapter

import android.support.v7.widget.GridLayoutManager
import android.widget.ImageView
import com.viet.follow.R
import com.viet.follow.viewmodel.FindViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.domain.response.NewsListBean
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ext.load
import com.viet.news.core.ext.loadCircle
import com.viet.news.core.ext.routerWithAnim
import com.viet.news.core.ui.BaseAdapter
import com.viet.news.core.ui.widget.BehaviorBar
import com.viet.news.core.utils.DateUtils
import com.viet.news.webview.WebActivity
import kotlinx.android.synthetic.main.cell_news_picture_three.view.*
import java.util.*
import javax.inject.Inject


/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 5:49 PM
 * @Version
 */
class NewsAdapter @Inject constructor() : BaseAdapter<NewsListBean>() {

    lateinit var model: FindViewModel

    override fun getItemViewType(position: Int): Int {
        return getData()[position].content.contentType
    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.cell_news_picture_three   //3图
            2 -> R.layout.cell_news_picture_one //1图
            3 -> R.layout.cell_news_text_only    //无图(文字)
            else -> R.layout.cell_news_picture_only //广告
        }
    }

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: NewsListBean) {
        holder.itemView.tv_title.text = t.author.nick_name
        holder.itemView.tv_time.text = DateUtils.getTimestamp(Date(t.content.createDateTime))
        holder.itemView.tv_des.text = t.content.contentTitle
        holder.itemView.findViewById<ImageView>(R.id.iv_article_image).loadCircle(t.author.avatar)

        holder.itemView.findViewById<BehaviorBar>(R.id.behaviorBar)?.setReadNumStatus(t.content.contentProfit)
        holder.itemView.findViewById<BehaviorBar>(R.id.behaviorBar)?.setLikeStatus(t.content.likeFlag, t.content.likeNumber)
        holder.itemView.findViewById<BehaviorBar>(R.id.behaviorBar)?.setFavoriteStatus(t.content.collectionFlag, t.content.collectionNumber)

        holder.itemView.findViewById<ImageView>(R.id.iv_article_image).clickWithTrigger { routerWithAnim(Config.ROUTER_PERSONAL_PAGE_ACTIVITY).with(Config.BUNDLE_USER_ID, t.content.userId).go(context) }
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
                holder.itemView.rv_news_cell.layoutManager = GridLayoutManager(context, 3)
                val cellAdapter = NewsCellAdapter()
                holder.itemView.rv_news_cell.adapter = cellAdapter
                cellAdapter.addData(t.image_array)
            }
            2 -> holder.itemView.findViewById<ImageView>(R.id.iv_pic).load(t.image_array[0].cover)
        }
    }
}