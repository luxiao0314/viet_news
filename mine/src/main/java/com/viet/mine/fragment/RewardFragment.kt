package com.viet.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.viet.mine.R
import com.viet.news.core.ui.RealVisibleHintBaseFragment

/**
 * @Description
 * @author null
 * @date 2018/9/8
 * @Email zongjia.long@merculet.io
 * @Version
 */
class RewardFragment : RealVisibleHintBaseFragment(){
    private var mContainerView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_wallet_reward, container, false)
        return mContainerView}

    override fun initView(view: View) {

    }
}