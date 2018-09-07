package com.viet.mine.activity

import android.os.Bundle
import com.jaeger.library.StatusBarUtil
import com.viet.mine.R
import com.viet.news.core.ui.BaseActivity

class MineWalletActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_wallet)
        initView()
        initData()
    }

    private fun initView() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 70, null)
    }

    private fun initData() {

    }

}