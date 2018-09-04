package com.viet.mine.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.safframework.ext.clickWithTrigger
import com.viet.mine.R
import com.viet.mine.activity.SettingActivity
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
        click_to_login.clickWithTrigger { Navigation.findNavController(it).navigate(R.id.loginActivity) }
        mine_group.clickWithTrigger { WebActivity.launch(context, "https://www.baidu,com") }
        mine_settings.clickWithTrigger {
            context!!.startActivity(Intent(activity, SettingActivity::class.java))
        }
    }
}
