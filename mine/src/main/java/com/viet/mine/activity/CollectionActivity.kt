package com.viet.mine.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
import com.viet.mine.R
import com.viet.mine.adapter.CollectionAdapter
import com.viet.mine.viewmodel.CollectionViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.InjectActivity
import kotlinx.android.synthetic.main.activity_mine_collection.*

class CollectionActivity : BaseActivity() {

    private val model: CollectionViewModel by viewModelDelegate(CollectionViewModel::class)

    private lateinit var adapter: CollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_collection)
        adapter = CollectionAdapter()
        initData()
        initView()
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
                    adapter.closeAllItem();
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