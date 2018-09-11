package com.viet.follow.activity

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
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
import com.viet.news.core.ui.InjectActivity
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
class PersonalPageActivity : InjectActivity() {

    @Inject
    internal lateinit var adapter: PersonalPageAdapter
    private val model: PersonalPageModel by viewModelDelegate(PersonalPageModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        setContentView(R.layout.activity_personal_page)
        initView()
        initData()
    }

    private fun initView() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.shape_list_divider_gray_05dp)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)

        iv_article_image.loadCircle("https://og3jro9lh.qnssl.com/Flym8MUqtOAQMBkBPPCbjokZ3xZN")
        iv_header.loadBlur("https://og3jro9lh.qnssl.com/Flym8MUqtOAQMBkBPPCbjokZ3xZN")
        relativeLayout.click { startActivity(Intent(this,FunsAndFollowActivity::class.java)) }
    }

    private fun initData() {
        model.getNewsArticles().observe(this, Observer { adapter.addData(it?.articles) })
    }

    override fun setStatusBar() {
        support(Build.VERSION_CODES.M, {
            StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null)
        }, {
            StatusBarUtil.setTranslucentForImageViewInFragment(this, 30, null)
        })
    }
}
