package com.viet.mine.viewmodel

import com.viet.mine.R
import com.viet.mine.fragment.*
import com.viet.news.core.ui.App
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.viewmodel.BaseViewModel

/**
 * @Description 钱包ViewModel
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
class MineWalletViewModel : BaseViewModel() {

    val fragments = arrayListOf<BaseFragment>(AccountInAndOutFragment(), RewardFragment())
    val subTitles = mutableListOf(App.instance.resources.getString(R.string.coin_in), App.instance.resources.getString(R.string.coin_out))
    val subFragments = mutableListOf<BaseFragment>(AccountInOutFragment(), AccountInOutFragment())
    var currentTab = 0
}