package com.viet.news.ui.fragment

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.viet.news.R
import com.viet.news.adapter.NewsAdapter
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ui.RealVisibleHintBaseFragment
import com.viet.news.viewmodel.FindViewModel
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_news.*
import javax.inject.Inject

/**
 * @Description 频道详情页面
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 3:34 PM
 * @Version 1.0.0
 */
class NewsFragment : RealVisibleHintBaseFragment(), HasSupportFragmentInjector {

    private val model: FindViewModel by viewModelDelegate(FindViewModel::class)

    @Inject
    internal lateinit var adapter: NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun initView(view: View) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity,OrientationHelper.VERTICAL,false)
        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.shape_list_divider_gray_05dp)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    override fun onFragmentFirstVisible() {
        model.getNewsArticles().observe(this, Observer {
            adapter.addData(it?.articles)
        })
    }

    companion object {
        fun newInstance(tabTitle: String): NewsFragment {
            val twoFragment = NewsFragment()
            val bundle = Bundle()
            bundle.putString("tabName", tabTitle)
            twoFragment.arguments = bundle
            return twoFragment
        }
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
