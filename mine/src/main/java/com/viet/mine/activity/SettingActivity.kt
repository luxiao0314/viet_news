package com.viet.mine.activity

import android.os.Bundle
import androidx.navigation.Navigation
import com.chenenyu.router.annotation.Route
import com.jaeger.library.StatusBarUtil
import com.viet.mine.R
import com.viet.news.core.config.Config
import com.viet.news.core.ext.goFragment
import com.viet.news.core.ext.routerWithAnim
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.InjectActivity

/**
 * @Description 设置
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_SETTING_ACTIVITY])
class SettingActivity : InjectActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_setting)
        initView()
        initData()
    }

    private fun initView() {
        routerWithAnim(Config.ROUTER_MINE_SETTING_FRAGMENT).goFragment(this, R.id.container_framelayout)
    }

    private fun initData() {

    }

}