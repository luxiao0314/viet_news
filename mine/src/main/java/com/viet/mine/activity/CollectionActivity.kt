package com.viet.mine.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
import com.chenenyu.router.annotation.Route
import com.viet.mine.R
import com.viet.mine.adapter.CollectionAdapter
import com.viet.mine.viewmodel.CollectionViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.User
import com.viet.news.core.domain.response.NewsListBean
import com.viet.news.core.ui.InjectActivity
import com.viet.news.webview.WebActivity
import kotlinx.android.synthetic.main.activity_mine_collection.*
import javax.inject.Inject

/**
 * @Description 用户收藏
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_COLLECTION_ACTIVITY], interceptors = [(Config.LOGIN_INTERCEPTOR)])
class CollectionActivity : InjectActivity() {

    @Inject
    internal lateinit var adapter: CollectionAdapter
    private val model: CollectionViewModel by viewModelDelegate(CollectionViewModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_collection)
        initView()
        initListener()
        refreshLayout.autoRefresh()
    }

    private fun initListener() {
        refreshLayout.setOnRefreshListener { initData(false) }
        refreshLayout.setOnLoadMoreListener { initData(true) }
        multiStatusView.setLoadingButtonClickListener(View.OnClickListener { refreshLayout.autoRefresh() })
        adapter.setClickDelegate {
            onItemClick = { url -> WebActivity.launch(this@CollectionActivity, url) }
        }
    }

    private fun initView() {
        rl_collection.adapter = adapter
        rl_collection.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        rl_collection.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                if (newState == SCROLL_STATE_TOUCH_SCROLL) {
                    adapter.closeAllItem()
                }
            }
        })
    }

    private fun initData(loadMore: Boolean) {
        if (loadMore) {
            model.page_number += 1
        } else {
            model.page_number = 1
        }
        model.getCollectionList(User.currentUser.userId).observe(this, Observer {
            it?.work(onSuccess = {
                multiStatusView.showContent()
                if (loadMore) {
                    if (it.data?.list == null || it.data?.list!!.isEmpty()) {
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    } else {
                        refreshLayout.finishLoadMore()
                        adapter.addData(it.data?.list)
                    }
                } else {
                    if (it.data?.list == null || it.data?.list!!.isEmpty()) {
                        multiStatusView.showEmpty()
                        refreshLayout.setEnableLoadMore(false)
                    }
                    adapter.setData(it.data?.list as ArrayList<NewsListBean>)
                    refreshLayout.setNoMoreData(false)
                    refreshLayout.finishRefresh()
                }
            }, onError = {
                multiStatusView.showError()
                if (loadMore) {
                    refreshLayout.finishLoadMore(false)//传入false表示加载失败
                } else {
                    refreshLayout.finishRefresh(false)
                }
            })
        })
    }
}