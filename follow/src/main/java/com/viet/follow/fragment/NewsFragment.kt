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
import android.widget.TextView
import com.safframework.ext.then
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.internal.InternalClassics
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.viet.follow.R
import com.viet.follow.adapter.NewsAdapter
import com.viet.follow.viewmodel.FindViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.RefreshNewsEvent
import com.viet.news.core.domain.response.NewsListBean
import com.viet.news.core.ui.RealVisibleHintBaseFragment
import com.viet.news.core.utils.RxBus
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
    var id: String? = ""
    var page_number = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onFragmentFirstVisible() {
        id = arguments?.getString(Config.BUNDLE_ID)
        refreshLayout.autoRefresh()
        initEvent()
    }

    override fun initView(view: View) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, OrientationHelper.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.shape_list_divider_gray_05dp)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)
        initListener()
    }

    private fun initEvent() {
        compositeDisposable.add(RxBus.get().register(RefreshNewsEvent::class.java) {
            if (isFragmentVisible(this)) {
                recyclerView.scrollToPosition(0)
                refreshLayout.autoRefresh()
            }
        })
    }

    private fun initListener() {
        refreshLayout.setOnMultiPurposeListener(listener)
        refreshLayout.setOnRefreshListener { initData(false) }
        refreshLayout.setOnLoadMoreListener { initData(true) }
        multiStatusView.setLoadingButtonClickListener(View.OnClickListener { refreshLayout.autoRefresh() })
        adapter.setClickDelegate {
            onLikeClick = { isLiked, num, id, func ->
                model.like(this@NewsFragment, id.toString()) { func() }
            }
            onFavoriteClick = { isFavorite, num, id, func ->
                model.collection(this@NewsFragment, id.toString()) { func() }
            }
        }
    }

    private fun initData(loadMore: Boolean) {
        if (loadMore) {
            page_number += 1
        } else {
            page_number = 1
        }
        model.getlist4Channel(id,page_number)
                .observe(this, Observer {
                    it?.data?.isOkStatus?.then({
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
                            recyclerView.scrollToPosition(0)
                            adapter.addData(0, it.data?.data?.list as ArrayList<NewsListBean>)
                            refreshLayout.setNoMoreData(false)
                            refreshLayout.finishRefresh()
                        }
                    }, {
                        multiStatusView.showError()
                        if (loadMore) {
                            refreshLayout.finishLoadMore(false)//传入false表示加载失败
                        } else {
                            refreshLayout.finishRefresh(false)
                        }
                    })
                })
    }

    private val listener = object : SimpleMultiPurposeListener() {

        override fun onHeaderFinish(header: RefreshHeader?, success: Boolean) {
            header as ClassicsHeader
            if (model.newsList.isEmpty()) {
                header.findViewById<TextView>(InternalClassics.ID_TEXT_TITLE.toInt()).text = getString(R.string.no_updates_are_available)
            } else {
                header.findViewById<TextView>(InternalClassics.ID_TEXT_TITLE.toInt()).text = String.format(getString(R.string.updates_s_article), model.newsList.size)
            }
            header.setPrimaryColor(resources.getColor(R.color.red_hint))
            header.setAccentColor(resources.getColor(R.color.white))
        }

        override fun onHeaderMoving(header: RefreshHeader?, isDragging: Boolean, percent: Float, offset: Int, headerHeight: Int, maxDragHeight: Int) {
            header as ClassicsHeader
            header.setPrimaryColor(resources.getColor(R.color.white))
            header.setAccentColor(resources.getColor(R.color.text_gray))
        }
    }

    companion object {
        fun newInstance(id: String?): NewsFragment {
            val fragment = NewsFragment()
            val bundle = Bundle()
            bundle.putString(Config.BUNDLE_ID, id)
            fragment.arguments = bundle
            return fragment
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
