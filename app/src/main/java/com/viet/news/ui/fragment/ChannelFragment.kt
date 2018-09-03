package com.viet.news.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.viet.news.R
import com.viet.news.adapter.ChannelPageAdapter
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ui.RealVisibleHintBaseFragment
import com.viet.news.viewmodel.FindViewModel
import kotlinx.android.synthetic.main.fragment_channel.*

/**
 * @Description 频道详情页面
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 3:34 PM
 * @Version 1.0.0
 */
class ChannelFragment : RealVisibleHintBaseFragment() {

    private var tabName: String? = null
    private val model: FindViewModel by viewModelDelegate(FindViewModel::class)
    private var adapter: ChannelPageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tabName = arguments?.getString("tabName")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_channel, container, false)
    }

    override fun initView(view: View) {
        adapter = ChannelPageAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    companion object {
        fun newInstance(tabTitle: String): ChannelFragment {
            val twoFragment = ChannelFragment()
            val bundle = Bundle()
            bundle.putString("tabName", tabTitle)
            twoFragment.arguments = bundle
            return twoFragment
        }
    }
}
