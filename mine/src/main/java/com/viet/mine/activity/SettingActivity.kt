package com.viet.mine.activity

import android.os.Bundle
import androidx.navigation.Navigation
import com.jaeger.library.StatusBarUtil
import com.viet.mine.R
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.InjectActivity

/**
 * @Description 设置
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
class SettingActivity : InjectActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_setting)
        initView()
        initData()
    }

    private fun initView() {
    }

    private fun initData() {

    }


    override fun onSupportNavigateUp() =
            Navigation.findNavController(this, R.id.my_nav_setting_host_fragment).navigateUp()
}