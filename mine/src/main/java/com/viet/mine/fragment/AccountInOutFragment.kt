package com.viet.mine.fragment

import android.view.View
import com.viet.mine.R
import com.viet.news.core.ui.RealVisibleHintBaseFragment

/**
 * @Description
 * @author null
 * @date 2018/9/8
 * @Email zongjia.long@merculet.io
 * @Version
 */
class AccountInOutFragment : RealVisibleHintBaseFragment(){

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine_wallet_in_out_sub
    }

    override fun initView(view: View) {

    }
}