package com.viet.mine.adapter

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.viet.mine.R
import com.viet.mine.activity.MineWalletActivity


/**
 * @Description 卡片适配器
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
class ItemAdapter(private val context: Context, private val list: List<MineWalletActivity.Entity>) : PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = inflater.inflate(R.layout.wallet_card_item, container, false)
        val name = view.findViewById<TextView>(R.id.tv_wallet_name)
        val count = view.findViewById<TextView>(R.id.tv_wallet_asset)
        val bg = view.findViewById<ConstraintLayout>(R.id.card)
        name.text = list[position].name
        count.text = list[position].count
        when (position) {
            0 -> {
                bg.background = context.resources.getDrawable(R.drawable.shape_wallet_card_red)
            }
            1 -> {
                bg.background = context.resources.getDrawable(R.drawable.shape_wallet_card_yellow)
            }
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
