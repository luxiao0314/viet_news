package com.viet.follow.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.magicwindow.core.ui.ItemClickSupport
import com.viet.follow.R
import com.viet.follow.adapter.FuncAndFollowItem
import com.viet.follow.viewmodel.FansAndFollowViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.response.UserInfoResponse
import com.viet.news.core.dsl.adapter.CommonAdapter
import com.viet.news.core.ext.itemDecoration
import com.viet.news.core.ext.linear
import com.viet.news.core.ui.InjectFragment
import com.viet.news.dialog.CancelFollowDialog
import com.viet.news.dialog.interfaces.IPositiveButtonDialogListener
import kotlinx.android.synthetic.main.fragment_follow_tab.*

/**
 * @Description 关注tab
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 11:13 AM
 * @Version 1.0.0
 */
class FollowTabFragment : InjectFragment(), IPositiveButtonDialogListener {

    lateinit var adapter: CommonAdapter<UserInfoResponse>
    private val model by viewModelDelegate(FansAndFollowViewModel::class)
    var page_number = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_follow_tab, container, false)
    }

    override fun initView(view: View) {
        adapter = CommonAdapter { item { FuncAndFollowItem() } }
        recyclerView.adapter = adapter
        recyclerView.linear()
        recyclerView.itemDecoration()

        refreshLayout.autoRefresh()
        refreshLayout.setOnRefreshListener { initData(false) }
        refreshLayout.setOnLoadMoreListener { initData(true) }
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        multiStatusView.setLoadingButtonClickListener(View.OnClickListener { refreshLayout.autoRefresh() })
        ItemClickSupport.addTo(recyclerView).addOnChildClickListener(R.id.btn_follow, listener)
    }

    private val listener = object : ItemClickSupport.OnChildClickListener {
        override fun onChildClicked(recyclerView: RecyclerView, position: Int, v: View) {
            if (adapter.getData()[position].follow_flag) {
                model.cancelPosition.value = position
                CancelFollowDialog.create(this@FollowTabFragment)
            } else {
                model.follow(this@FollowTabFragment, adapter.getData()[position].id) {
                    adapter.getData()[position].follow_flag = true
                    adapter.notifyItemChanged(position)
                }
            }
        }
    }

    override fun onPositiveButtonClicked(requestCode: Int) {
        model.cancelfollow(this@FollowTabFragment, adapter.getData()[model.cancelPosition.value!!].id) {
            adapter.getData()[model.cancelPosition.value!!].follow_flag = false
            adapter.notifyItemChanged(model.cancelPosition.value!!)
        }
    }

    private fun initData(loadMore: Boolean) {
        if (loadMore) {
            page_number += 1
        } else {
            page_number = 1
        }
        model.followList(page_number)
                .observe(this, Observer {
                    it?.work(
                            onSuccess = {
                                multiStatusView.showContent()
                                if (loadMore) {
                                    if (it.data?.list == null || it.data?.list!!.isEmpty()) {
                                        refreshLayout.finishLoadMoreWithNoMoreData()
                                    } else {
                                        refreshLayout.finishLoadMore()
                                        adapter.addData(it.data?.list!!)
                                    }
                                } else {
                                    if (it.data?.list == null || it.data?.list!!.isEmpty()) {
                                        multiStatusView.showEmpty()
                                        refreshLayout.setEnableLoadMore(false)
                                    }
                                    adapter.setData(it.data?.list!!)
                                    refreshLayout.setNoMoreData(false)
                                    refreshLayout.finishRefresh()
                                }
                            },
                            onError = {
                                multiStatusView.showError()
                                if (loadMore) {
                                    refreshLayout.finishLoadMore(false)//传入false表示加载失败
                                } else {
                                    refreshLayout.finishRefresh(false)
                                }
                            }
                    )
                })
    }
}