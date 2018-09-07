package com.viet.mine.activity

import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import com.jaeger.library.StatusBarUtil
import com.viet.mine.R
import com.viet.news.core.ui.InjectActivity

class InviteFriendActivity : InjectActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        setContentView(R.layout.activity_mine_invite)
        initView()
        initData()
    }

    private fun initView() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null)
    }

    private fun initData() {

    }

    override fun onSupportNavigateUp() =
            findNavController(this, R.id.my_nav_host_fragment).navigateUp()
}