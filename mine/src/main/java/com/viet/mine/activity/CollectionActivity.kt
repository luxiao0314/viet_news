package com.viet.mine.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
import com.jaeger.library.StatusBarUtil
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.viet.mine.R
import com.viet.mine.adapter.CollectionAdapter
import com.viet.mine.viewmodel.CollectionViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.RefreshNewsEvent
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.InjectActivity
import com.viet.news.core.utils.RxBus
import kotlinx.android.synthetic.main.activity_mine_collection.*

/**
 * @Description 用户收藏
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
class CollectionActivity : InjectActivity() {

    private val model: CollectionViewModel by viewModelDelegate(CollectionViewModel::class)

    private lateinit var adapter: CollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_collection)
        adapter = CollectionAdapter()
        initData()
        initView()
        initListener()
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

    private fun initView() {
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

    private fun initData() {
        model.getNewsArticles().observe(this, Observer {
            adapter.addData(it?.articles)
        })
    }


}