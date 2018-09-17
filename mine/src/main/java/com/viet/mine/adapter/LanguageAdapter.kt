package com.viet.mine.adapter

import android.support.v4.content.ContextCompat
import com.viet.mine.R
import com.viet.news.core.config.Config
import com.viet.news.core.ui.BaseAdapter
import com.viet.news.core.utils.SPHelper
import kotlinx.android.synthetic.main.cell_select_language.view.*

/**
 *
 * @FileName:
 *          io.merculet.wallet.adapter.CountryAdapter.java
 * @author: Tony Shen
 * @date: 2018-03-29 14:26
 * @version V1.0 <描述当前版本功能>
 */
class LanguageAdapter : BaseAdapter<String>() {

    override fun getLayoutId(viewType: Int): Int = R.layout.cell_select_language

    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: String) {
        holder.itemView.language_name.text = t
        val selectedLanguageIndex = SPHelper.create().getInt(Config.SELECTED_LANGUAGE)
        if (position == selectedLanguageIndex) {
//            holder.itemView.iv_arrow_right.visibility = View.VISIBLE
            holder.itemView.language_name.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.itemView.rl_mvp_holdings.setBackgroundResource(R.drawable.shape_list_bg)
        } else {
//            holder.itemView.iv_arrow_right.visibility = View.GONE
            holder.itemView.language_name.setTextColor(ContextCompat.getColor(context, R.color.text_gray))
            holder.itemView.rl_mvp_holdings.setBackgroundResource(R.color.white)
        }

    }


}