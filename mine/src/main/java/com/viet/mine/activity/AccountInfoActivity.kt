package com.viet.mine.activity

import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import com.jaeger.library.StatusBarUtil
import com.viet.mine.R
import com.viet.news.core.ui.BaseActivity

class AccountInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_account_info)
        initView()
        initData()
    }

    private fun initView() {
    }

    private fun initData() {

    }

    override fun onSupportNavigateUp() =
            findNavController(this, R.id.my_nav_host_fragment).navigateUp()
}