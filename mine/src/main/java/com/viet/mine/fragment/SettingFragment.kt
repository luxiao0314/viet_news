package com.viet.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.chenenyu.router.annotation.Route
import com.viet.mine.R
import com.viet.news.core.config.Config
import com.viet.news.core.domain.User
import com.viet.news.core.ext.click
import com.viet.news.core.ext.finishWithAnim
import com.viet.news.core.ext.goFragment
import com.viet.news.core.ext.routerWithAnim
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.ui.widget.CommonItem
import kotlinx.android.synthetic.main.fragment_mine_setting.*

/**
 * @Description 设置
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_SETTING_FRAGMENT])
class SettingFragment : BaseFragment() {

    private var mContainerView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_setting, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
        val languageSettingItem = view.findViewById<CommonItem>(R.id.item_language_setting)
        val helpItem = view.findViewById<CommonItem>(R.id.item_help)
        val feedBackItem = view.findViewById<CommonItem>(R.id.item_feed_back)

        languageSettingItem.setClickDelegate {
            onItemClick = {
                openPage(Config.ROUTER_MINE_SETTING_LANGUAGE_FRAGMENT)
            }
        }

        helpItem.setClickDelegate {
            onItemClick = {
                openPage(Config.ROUTER_MINE_SETTING_HELP_FRAGMENT)
            }
        }

        feedBackItem.setClickDelegate {
            onItemClick = {
                openPage(Config.ROUTER_MINE_SETTING_FEEDBACK_FRAGMENT)
            }
        }

        if (User.currentUser.isLogin()) {
            btn_logout.visibility = View.VISIBLE
            btn_logout.click {
                User.currentUser.logout()
                (activity as BaseActivity).finishWithAnim()
            }
        } else {
            btn_logout.visibility = View.GONE
        }

    }

    private fun openPage(path: String) {
        routerWithAnim(path).goFragment(this@SettingFragment, R.id.container_framelayout)
    }
}