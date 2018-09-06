package com.viet.mine.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.safframework.ext.click
import com.viet.mine.R
import com.viet.mine.activity.FindPwdActivity
import com.viet.mine.viewmodel.LoginViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ui.RealVisibleHintBaseFragment
import kotlinx.android.synthetic.main.fragment_login_pwd.*

/**
 * @Description 登录tab
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 05/09/2018 5:28 PM
 * @Version
 */
//@Route(value = [(Config.ROUTER_LOGIN_FRAGMENT)])
class PwdToLoginFragment : RealVisibleHintBaseFragment() {

    private val model by viewModelDelegate(LoginViewModel::class, true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_login_pwd, container, false)

    override fun initView(view: View) {
        tv_forget_password.click { startActivity(Intent(context, FindPwdActivity::class.java)) }
    }
}