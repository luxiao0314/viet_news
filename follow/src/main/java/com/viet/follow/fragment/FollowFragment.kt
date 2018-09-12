package com.viet.follow.fragment

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.viet.follow.R
import com.viet.follow.adapter.NewsAdapter
import com.viet.follow.viewmodel.FindViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.response.NewsListBean
import com.viet.news.core.ui.RealVisibleHintBaseFragment
import com.viet.news.core.vo.Status
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_news.*
import javax.inject.Inject

/**
 * @Description 关注
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 11:13 AM
 * @Version 1.0.0
 */
class FollowFragment : RealVisibleHintBaseFragment(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var adapter: NewsAdapter
    private val model by viewModelDelegate(FindViewModel::class)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun initView(view: View) {
        model.id = 1//userId
        initListener()
    }

    override fun onFragmentFirstVisible() {
        refreshLayout.autoRefresh()
    }

    private fun initListener() {
        refreshLayout.setOnRefreshListener { initData(false) }
        refreshLayout.setOnLoadMoreListener { initData(true) }
        multiStatusView.setLoadingButtonClickListener(View.OnClickListener { refreshLayout.autoRefresh() })
    }

    private fun initData(loadMore: Boolean) {
        if (loadMore) {
            model.page_number += 1
        } else {
            model.page_number = 1
        }
        model.getlist4Follow()
                .observe(this, Observer {
                    when (it?.status) {
                        Status.SUCCESS -> {
                            model.newsList = it.data?.data?.list as ArrayList<NewsListBean>
                            multiStatusView.showContent()
                            if (loadMore) {
                                if (it.data?.data?.list == null || it.data?.data?.list!!.isEmpty()) {
                                    refreshLayout.finishLoadMoreWithNoMoreData()
                                } else {
                                    refreshLayout.finishLoadMore()
                                    adapter.addData(it.data?.data?.list)
                                }
                            } else {
                                if (it.data?.data?.list == null || it.data?.data?.list!!.isEmpty()) {
                                    multiStatusView.showEmpty()
                                    refreshLayout.setEnableLoadMore(false)
                                }
                                adapter.setData(it.data?.data?.list)
                                refreshLayout.setNoMoreData(false)
                                refreshLayout.finishRefresh()
                            }
                        }
                        Status.ERROR -> {
                            multiStatusView.showError()
                            if (loadMore) {
                                refreshLayout.finishLoadMore(false)//传入false表示加载失败
                            } else {
                                refreshLayout.finishRefresh(false)
                            }
                        }
                        else -> {
                        }
                    }
                })
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