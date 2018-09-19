package com.viet.mine.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chenenyu.router.annotation.Route
import com.viet.mine.R
import com.viet.mine.adapter.HelpAdapter
import com.viet.mine.bean.HelpBean
import com.viet.news.core.config.Config
import com.viet.news.core.ui.RealVisibleHintBaseFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_mine_setting_help.*
import javax.inject.Inject

/**
 * @Description 帮助
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_SETTING_HELP_FRAGMENT])
class HelpFragment : RealVisibleHintBaseFragment(), HasSupportFragmentInjector {
    private var mContainerView: View? = null
    @Inject
    internal lateinit var adapter: HelpAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_setting_help, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
        val list = ArrayList<HelpBean>()
        list.add(HelpBean(resources.getString(R.string.description)))
        adapter.addData(list)
        rl_help.adapter = adapter
        rl_help.layoutManager = LinearLayoutManager(activity, OrientationHelper.VERTICAL, false)
    }

    @Inject
    internal lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return childFragmentInjector
    }

    override fun onAttach(context: Context) {
        //使用的Fragment 是V4 包中的，不然就是AndroidInjection.inject(this)
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}