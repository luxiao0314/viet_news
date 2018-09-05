package com.viet.mine.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.safframework.ext.clickWithTrigger
import com.viet.mine.R
import com.viet.mine.activity.AccountInfoActivity
import com.viet.mine.activity.CollectionActivity
import com.viet.mine.activity.LoginActivity
import com.viet.mine.activity.SettingActivity
import com.viet.news.core.domain.User
import com.viet.news.core.ui.BaseFragment
import com.viet.news.webview.WebActivity
import kotlinx.android.synthetic.main.fragment_mine.*


/**
 * @author null
 */
class MineFragment : BaseFragment() {

    private var mContainerView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
        btn_login.clickWithTrigger { context?.startActivity(Intent(activity, LoginActivity::class.java)) }
        mine_wallet.clickWithTrigger { WebActivity.launch(context, "http://www.baidu.com") }
        mine_settings.setClickDelegate {
            onItemClick = { context?.startActivity(Intent(activity, SettingActivity::class.java)) }
        }

        iv_user_icon.clickWithTrigger {
            if (!User.currentUser.isLogin()) {
                startActivityForResult(Intent(activity, LoginActivity::class.java), 0)
            } else {
                startActivity(Intent(activity, AccountInfoActivity::class.java))
            }
        }

        mine_collection.setClickDelegate {
            onItemClick = {
                context?.startActivity(Intent(activity, CollectionActivity::class.java))
            }
        }

        edit.clickWithTrigger {
            context?.startActivity(Intent(activity, AccountInfoActivity::class.java))
        }
    }
}
