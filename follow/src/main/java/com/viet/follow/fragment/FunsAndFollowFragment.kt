package com.viet.follow.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.viet.follow.R
import com.viet.follow.adapter.FunsAndFollowAdapter
import com.viet.follow.viewmodel.FunsAndFollowViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ui.InjectFragment
import kotlinx.android.synthetic.main.fragment_funs_and_follow.*
import javax.inject.Inject

/**
 * @Description 任务
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 11:13 AM
 * @Version 1.0.0
 */
class FunsAndFollowFragment : InjectFragment() {

    @Inject
    internal lateinit var adapter: FunsAndFollowAdapter
    private val model by viewModelDelegate(FunsAndFollowViewModel::class)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_funs_and_follow, container, false)
    }

    override fun initView(view: View) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, OrientationHelper.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.shape_list_divider_gray_05dp)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)

        initData()
    }

    private fun initData() {
        model.getData().observe(this, Observer { adapter.addData(it?.data) })
    }
}