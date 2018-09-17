package com.viet.follow.activity

import android.arch.lifecycle.Observer
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.View
import com.chenenyu.router.annotation.Route
import com.jaeger.library.StatusBarUtil
import com.safframework.utils.support
import com.viet.follow.R
import com.viet.follow.adapter.PersonalPageAdapter
import com.viet.follow.viewmodel.PersonalPageModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.click
import com.viet.news.core.ext.loadBlur
import com.viet.news.core.ext.loadCircle
import com.viet.news.core.ext.routerWithAnim
import com.viet.news.core.ui.InjectActivity
import com.viet.news.dialog.CancelFollowDialog
import com.viet.news.dialog.interfaces.IPositiveButtonDialogListener
import kotlinx.android.synthetic.main.activity_personal_page.*
import javax.inject.Inject

/**
 * @Description 个人主页
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 07/09/2018 10:45 AM
 * @Version 1.0.0
 */
@Route(value = [Config.ROUTER_PERSONAL_PAGE_ACTIVITY])
class PersonalPageActivity : InjectActivity(), IPositiveButtonDialogListener {

    @Inject
    internal lateinit var adapter: PersonalPageAdapter
    private val model: PersonalPageModel by viewModelDelegate(PersonalPageModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        setContentView(R.layout.activity_personal_page)
        initView()
        model.userId = intent?.getStringExtra(Config.BUNDLE_USER_ID)
        initData(false)
        initInfoData()
    }

    private fun initInfoData() {
        model.getUserInfo(this) {
            iv_article_image.loadCircle(it?.avatar)
            iv_header.loadBlur(it?.avatar)
            tv_title.text = it?.nick_name
            tv_coin.text = it?.follow_count.toString()
            tv_fans_num.text = it?.fans_count.toString()
            tv_follow_num.text = it?.follow_count.toString()
            notifyBtn(!it?.follow_flag!!, if (!it.follow_flag) resources.getString(R.string.follow) else resources.getString(R.string.has_follow))
        }
    }

    private fun initView() {
        initListener()
        adapter.model = model
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.shape_list_divider_gray_05dp)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun initListener() {
        refreshLayout.setEnableRefresh(false)   //关闭下拉刷新
        refreshLayout.setOnRefreshListener { initData(false) }
        refreshLayout.setOnLoadMoreListener { initData(true) }
        multiStatusView.setLoadingButtonClickListener(View.OnClickListener { refreshLayout.autoRefresh() })
        relativeLayout.click { routerWithAnim(Config.ROUTER_FUNS_AND_FOLLOW_ACTIVITY).with(Config.BUNDLE_USER_ID, model.userId).go(this) }
        btn_follow.click {
            if (btn_follow.isSelected) {
                model.follow(this) { notifyBtn(!btn_follow.isSelected, resources.getString(R.string.has_follow)) }
            } else {
                CancelFollowDialog.create(this)
            }
        }
    }

    override fun onPositiveButtonClicked(requestCode: Int) {
        model.cancelfollow(this) {
            notifyBtn(!btn_follow.isSelected, resources.getString(R.string.follow))
            initInfoData()
        }
    }

    private fun notifyBtn(isSelected: Boolean, text: String) {
        btn_follow.isSelected = isSelected
        btn_follow.text = text
        btn_follow.setCompoundDrawablesWithIntrinsicBounds(if (isSelected) null else resources.getDrawable(R.drawable.ic_hook), null, null, null)
    }

    private fun initData(loadMore: Boolean) {
        if (loadMore) {
            model.page_number += 1
        } else {
            model.page_number = 1
        }
        model.getlist4User()
                .observe(this, Observer {
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
                            adapter.setData(it.data?.list)
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

    override fun setStatusBar() {
        support(Build.VERSION_CODES.M, {
            StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null)
        }, {
            StatusBarUtil.setTranslucentForImageViewInFragment(this, 30, null)
        })
    }
}
