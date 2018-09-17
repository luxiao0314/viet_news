package com.viet.follow.activity

import android.graphics.Typeface
import android.os.Bundle
import com.chenenyu.router.annotation.Route
import com.viet.follow.R
import com.viet.follow.viewmodel.FansAndFollowViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.dsl.addOnPageChangeListener
import com.viet.news.core.ui.InjectActivity
import com.viet.news.core.ui.TabFragmentAdapter
import kotlinx.android.synthetic.main.activity_funs_and_follow.*
import javax.inject.Inject

/**
 * @Description 粉丝和关注页面
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 07/09/2018 10:45 AM
 * @Version 1.0.0
 */
@Route(Config.ROUTER_FUNS_AND_FOLLOW_ACTIVITY)
class FansAndFollowActivity : InjectActivity() {

    @Inject
    internal lateinit var adapter: TabFragmentAdapter
    private val model by viewModelDelegate(FansAndFollowViewModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        setContentView(R.layout.activity_funs_and_follow)
        initView()
    }

    private fun initView() {
        model.userId.value = intent?.getStringExtra(Config.BUNDLE_USER_ID)
        adapter.setTitles(model.titles)
        adapter.setFragment(model.fragments)
        viewpager.adapter = adapter
        tablayout.setViewPager(viewpager)
        setTabText(0, 1)

        viewpager.addOnPageChangeListener { onPageSelected = { setTabText(it, model.currentTab.value!!) } }
    }

    private fun setTabText(currentTab: Int, otherTab: Int) {
        tablayout.getTitleView(currentTab).textSize = 18F
        tablayout.getTitleView(otherTab).textSize = 15F
        tablayout.getTitleView(currentTab).typeface = Typeface.DEFAULT_BOLD
        tablayout.getTitleView(otherTab).typeface = Typeface.DEFAULT
        model.currentTab.value = currentTab
    }
}
