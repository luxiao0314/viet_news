package com.viet.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chenenyu.router.annotation.Route
import com.viet.mine.R
import com.viet.news.core.config.Config
import com.viet.news.core.ui.BaseFragment

/**
 * @Description 帮助
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_SETTING_HELP_FRAGMENT])
class HelpFragment : BaseFragment() {
    private var mContainerView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_setting_help, container, false)
        return mContainerView
    }

    override fun initView(view: View) {

    }
}