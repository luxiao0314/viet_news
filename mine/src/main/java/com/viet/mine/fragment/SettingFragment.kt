package com.viet.mine.fragment

import android.os.Bundle
import android.view.View
import com.chenenyu.router.annotation.Route
import com.safframework.ext.clickWithTrigger
import com.viet.mine.R
import com.viet.news.core.config.Config
import com.viet.news.core.domain.RefreshSettingInfoEvent
import com.viet.news.core.domain.User
import com.viet.news.core.ext.finishWithAnim
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.utils.RxBus
import com.viet.news.core.utils.SPHelper
import com.viet.news.webview.WebActivity
import kotlinx.android.synthetic.main.fragment_mine_setting.*

/**
 * @Description 设置
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_SETTING_FRAGMENT])
class SettingFragment : BaseFragment() {
    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine_setting
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        compositeDisposable.add(RxBus.get().register(RefreshSettingInfoEvent::class.java) { refresh() })
    }

    override fun initView(view: View) {
        refresh()
        item_language_setting.clickWithTrigger { openPage(this@SettingFragment, Config.ROUTER_MINE_SETTING_LANGUAGE_FRAGMENT, R.id.container_framelayout) }
        item_help.clickWithTrigger { openPage(this@SettingFragment, Config.ROUTER_MINE_SETTING_HELP_FRAGMENT, R.id.container_framelayout) }
        item_feed_back.clickWithTrigger { openPage(this@SettingFragment, Config.ROUTER_MINE_SETTING_FEEDBACK_FRAGMENT, R.id.container_framelayout) }
        item_about.clickWithTrigger { WebActivity.launch(context, "http://www.baidu.com") }
        if (User.currentUser.isLogin()) {
            btn_logout.visibility = View.VISIBLE
            btn_logout.clickWithTrigger {
                User.currentUser.logout()
                (activity as BaseActivity).finishWithAnim()
            }
        } else {
            btn_logout.visibility = View.GONE
        }

    }


    fun refresh() {
        val position = SPHelper.create(context!!).getInt("language", 0)
        val language = resources.getStringArray(R.array.language).toList()[position]
        item_language_setting.setRightText(language)
    }

}