package com.viet.mine.adapter

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.viet.mine.R
import com.viet.mine.activity.MineWalletActivity
import com.viet.news.core.ext.clickWithTrigger

/**
 * @Description 卡片切换适配器
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
class CardRvAdapter(private val context: Context, private val list: List<MineWalletActivity.Entity>) : RecyclerView.Adapter<CardRvAdapter.ItemViewHolder>() {

    private var mDelegate: Delegate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.wallet_card_item, parent, false)
        return ItemViewHolder(itemView)
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.mName.text = list[position].name
        holder.mAsset.text = list[position].count
        if (position == 0) {
            holder.mCard.background = context.resources.getDrawable(R.drawable.shape_wallet_card_red)
            val params = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams(dip2px(context, 300f), dip2px(context, 120f)))
            params.marginStart = dip2px(context, 16f)
            holder.mCard.layoutParams = params
        } else {
            holder.mCard.background = context.resources.getDrawable(R.drawable.shape_wallet_card_yellow)
            val params = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams(dip2px(context, 300f), dip2px(context, 120f)))
            params.marginEnd = dip2px(context, 16f)
            holder.mCard.layoutParams = params
        }
        holder.mCard.clickWithTrigger {
            mDelegate?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun setDelegate(delegate: ClickDelegate) {
        mDelegate = delegate
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mName: TextView = itemView.findViewById(R.id.tv_wallet_name)
        val mAsset: TextView = itemView.findViewById(R.id.tv_wallet_asset)
        val mCard: ConstraintLayout = itemView.findViewById(R.id.card)
    }

    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun setClickDelegate(init: ClickDelegate.() -> Unit) {
        val delegate = ClickDelegate()
        delegate.init()
        setDelegate(delegate)
    }

    /**
     * 点击事件监听代理
     */
    interface Delegate {
        fun onItemClick(position: Int)
    }

    class ClickDelegate : Delegate {
        var onItemClick: ((Int) -> Unit)? = null

        override fun onItemClick(position: Int) {
            onItemClick?.let { it(position) }
        }
    }

}
