package com.viet.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.viet.mine.R
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.ui.widget.CommonItem

/**
 * 设置页面
 */
class SettingFragment :BaseFragment(){

    private var mContainerView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_setting, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
        val languageSettingItem = view.findViewById<CommonItem>(R.id.item_language_setting)
        val helpItem = view.findViewById<CommonItem>(R.id.item_help)

        languageSettingItem.setClickDelegate {
            onItemClick = {
                Navigation.findNavController(languageSettingItem).navigate(R.id.action_page1)
            }
        }

        helpItem.setClickDelegate {
            onItemClick = {
                Navigation.findNavController(helpItem).navigate(R.id.action_page2)
            }
        }

    }
}