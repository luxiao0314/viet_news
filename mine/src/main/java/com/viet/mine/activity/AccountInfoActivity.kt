package com.viet.mine.activity

import android.os.Bundle
import com.chenenyu.router.annotation.Route
import com.viet.mine.R
import com.viet.news.core.config.Config
import com.viet.news.core.ext.goFragment
import com.viet.news.core.ext.routerWithAnim
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
        routerWithAnim(Config.ROUTER_MINE_EDIT_INFO_FRAGMENT).goFragment(this, R.id.container_framelayout)
    }

    private fun initData() {

    }

}