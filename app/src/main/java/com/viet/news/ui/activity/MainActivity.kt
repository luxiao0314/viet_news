package com.viet.news.ui.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import com.jaeger.library.StatusBarUtil
import com.safframework.utils.support
import com.viet.news.R
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.Config
import com.viet.news.core.dsl.addOnPageChangeListener
import com.viet.news.core.dsl.setOnTabSelectListener
import com.viet.news.core.ui.InjectActivity
import com.viet.news.core.ui.TabFragmentAdapter
import com.viet.news.core.utils.LanguageUtil
import com.viet.news.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : InjectActivity() {

    @Inject
    internal lateinit var pagerAdapter: TabFragmentAdapter
    private val model: MainViewModel by viewModelDelegate(MainViewModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        pagerAdapter.setFragment(model.fragments)
        pagerAdapter.setTitles(model.titles)
        pagerAdapter.notifyDataSetChanged()
        container.adapter = pagerAdapter
        bottomBar.setTabData(model.tabEntities)

        container.offscreenPageLimit = 1//缓存1个界面
        container.addOnPageChangeListener { onPageSelected = { bottomBar.currentTab = it } }
        bottomBar.setOnTabSelectListener { onTabSelect = { container.currentItem = it } }
    }

    /*
    * 当配置信息改变而不需要重建activity时调用，配置信息在manifest文件中
    * 例如切换系统语言，针对我们的App，主页也没用keyboard事件，基本上就是切换系统语言才会调用此方法
    */
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        LanguageUtil.onConfigurationChanged(this)
        reLoadView()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val languageChanged = intent?.extras?.getBoolean(Config.LANGUAGE_CHANGED)
        languageChanged?.let {
            if (it) {
                LanguageUtil.onConfigurationChanged(this)
                reLoadView()
            }
        }
    }

    private fun reLoadView() {
        //TODO tsing 如果到时候要在App内切换语言 这里可能需要刷新一下当前界面
    }

    override fun setStatusBar() {
        support(Build.VERSION_CODES.M, {
            StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null)
        }, {
            StatusBarUtil.setTranslucentForImageViewInFragment(this, 30, null)
        })
    }
}
