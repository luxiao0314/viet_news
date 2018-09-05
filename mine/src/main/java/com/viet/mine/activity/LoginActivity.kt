package com.viet.mine.activity

import android.os.Bundle
import com.safframework.ext.click
import com.viet.mine.R
import com.viet.mine.viewmodel.LoginViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.dsl.setOnTabSelectListener
import com.viet.news.core.ui.InjectActivity
import com.viet.news.core.ui.TabFragmentAdapter
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

//@Route(value = [(Config.ROUTER_LOGIN_ACTIVITY)])
class LoginActivity : InjectActivity() {

    @Inject
    internal lateinit var adapter: TabFragmentAdapter
    private val model by viewModelDelegate(LoginViewModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        initListener()
    }

    private fun initView() {
        adapter.setTitles(model.titles)
        adapter.setFragment(model.fragments)
        viewpager.adapter = adapter
        tablayout.setViewPager(viewpager)
        setTabText(0, 1)
    }

    private fun initListener() {
        iv_close.click { finish() }
        tablayout.setOnTabSelectListener { onTabSelect = { setTabText(it, model.currentTab) } }
    }

    private fun setTabText(currentTab: Int, otherTab: Int) {
        tablayout.getTitleView(currentTab).textSize = 25F
        tablayout.getTitleView(otherTab).textSize = 15F
        model.currentTab = currentTab
    }
}