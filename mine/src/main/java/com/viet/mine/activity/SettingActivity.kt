package com.viet.mine.activity

import android.os.Bundle
import androidx.navigation.Navigation
import com.jaeger.library.StatusBarUtil
import com.viet.mine.R
import com.viet.news.core.ui.BaseActivity

/**
 * 设置板块
 */
class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_setting)
        initView()
        initData()
    }

    private fun initView() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 70, null)
    }

    private fun initData() {

    }


    override fun onSupportNavigateUp() =
            Navigation.findNavController(this, R.id.my_nav_setting_host_fragment).navigateUp()
}