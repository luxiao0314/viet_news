package com.viet.mine.activity

import android.graphics.Bitmap
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import com.chenenyu.router.annotation.Route
import com.jaeger.library.StatusBarUtil
import com.viet.mine.R
import com.viet.news.core.config.Config
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.InjectActivity

/**
 * @Description 用户信息承载Activity
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_EDIT_INFO_ACTIVITY])
class AccountInfoActivity : InjectActivity() {

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