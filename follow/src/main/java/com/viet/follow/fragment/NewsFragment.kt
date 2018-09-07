package com.viet.follow.fragment

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
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.viet.follow.R
import com.viet.follow.adapter.NewsAdapter
import com.viet.follow.viewmodel.FindViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ui.RealVisibleHintBaseFragment
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

    @Inject
    internal lateinit var adapter: NewsAdapter
    private val model: FindViewModel by viewModelDelegate(FindViewModel::class)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun initView(view: View) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, OrientationHelper.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.shape_list_divider_gray_05dp)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)
        initListener()
    }

    override fun onFragmentFirstVisible() {
        refreshLayout.autoRefresh()
    }

    private fun initListener() {
        refreshLayout.setOnRefreshListener {
            model.getNewsArticles().observe(this, Observer { adapter.addData(it?.articles) })
            it.finishRefresh()
        }
        refreshLayout.setOnLoadMoreListener {
            it.finishLoadMore(2000/*,false*/)//传入false表示加载失败
            model.getNewsArticles().observe(this, Observer { adapter.addData(it?.articles) })
        }
        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderFinish(header: RefreshHeader?, success: Boolean) {
                refreshLayout.setPrimaryColorsId(R.color.red_hint, android.R.color.white)
                ClassicsHeader.REFRESH_HEADER_FINISH = "已更新2篇文章"
//                ClassicsHeader.REFRESH_HEADER_FINISH = "暂无更新内容"
            }

            override fun onHeaderMoving(header: RefreshHeader?, isDragging: Boolean, percent: Float, offset: Int, headerHeight: Int, maxDragHeight: Int) {
                refreshLayout.setPrimaryColorsId(R.color.white, R.color.text_gray)
            }
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
