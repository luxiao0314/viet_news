package com.viet.mine.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
import android.widget.TextView
import com.chenenyu.router.annotation.Route
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.internal.InternalClassics
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.viet.mine.R
import com.viet.mine.adapter.CollectionAdapter
import com.viet.mine.viewmodel.CollectionViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.User
import com.viet.news.core.domain.response.CollectionListBean
import com.viet.news.core.ui.InjectActivity
import com.viet.news.core.vo.Status
import kotlinx.android.synthetic.main.activity_mine_collection.*

/**
 * @Description 用户收藏
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_COLLECTION_ACTIVITY],interceptors = [(Config.LOGIN_INTERCEPTOR)])
class CollectionActivity : InjectActivity() {

    private val model: CollectionViewModel by viewModelDelegate(CollectionViewModel::class)

//    @Inject
    private lateinit var adapter: CollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_collection)
        initListener()
        initView()
        refreshLayout.autoRefresh()
    }

    private fun initListener() {
        refreshLayout.setOnMultiPurposeListener(listener)
        refreshLayout.setOnRefreshListener { initData(false) }
        refreshLayout.setOnLoadMoreListener { initData(false) }
        multiStatusView.setLoadingButtonClickListener(View.OnClickListener { refreshLayout.autoRefresh() })
    }

    private fun initView() {
        adapter = CollectionAdapter()
        rl_collection.adapter = adapter
        rl_collection.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.shape_list_divider_gray_05dp)!!)
        rl_collection.addItemDecoration(dividerItemDecoration)
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
            when (it?.status) {
                Status.SUCCESS -> {
                    model.collectionList = it.data?.data?.list as ArrayList<CollectionListBean>
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

    private val listener = object : SimpleMultiPurposeListener() {

        override fun onHeaderFinish(header: RefreshHeader?, success: Boolean) {
            header as ClassicsHeader
            if (model.collectionList.isEmpty()) {
                header.findViewById<TextView>(InternalClassics.ID_TEXT_TITLE.toInt()).text = "暂无更新内容"
            } else {
                header.findViewById<TextView>(InternalClassics.ID_TEXT_TITLE.toInt()).text = "已更新2篇文章"
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

}