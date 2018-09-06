package com.viet.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.viet.mine.R
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.ui.widget.CommonItem

class AccountInfoFragment : BaseFragment() {

    private var mContainerView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_account_info, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
        val changeNameItem = view.findViewById<CommonItem>(R.id.item_change_name)
        val changePhoneNumItem = view.findViewById<CommonItem>(R.id.item_change_phone_num)
        val resetPwdItem = view.findViewById<CommonItem>(R.id.item_reset_pwd)
        changeNameItem.setClickDelegate {
            onItemClick = {
                Navigation.findNavController(changeNameItem).navigate(R.id.action_page_1)
            }
        }
        changePhoneNumItem.setClickDelegate {
            onItemClick = {
                Navigation.findNavController(changePhoneNumItem).navigate(R.id.action_page_2)
            }
        }

        resetPwdItem.setClickDelegate {
            onItemClick={
                Navigation.findNavController(resetPwdItem).navigate(R.id.action_page_3)
            }
        }
    }


}