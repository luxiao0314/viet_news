package com.viet.mine.fragment

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.safframework.ext.clickWithTrigger
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

    @SuppressLint("CheckResult")
    override fun initView(view: View) {

        //注册按钮能否点击更新
        model.pwdLoginButtonEnable.observe(this, Observer { login_btn.isEnabled = it != null && it })

        //手机
        RxTextView.textChanges(phone_input)
                .subscribe {
                    model.pwdLoginPhoneNumber.value = it.toString()
                    model.checkPwdLoginButtonEnable()
                }
        //密码
        RxTextView.textChanges(password_input)
                .subscribe {
                    model.pwdLoginPassword.value = it.toString()
                    model.checkPwdLoginButtonEnable()
                }

        //忘记密码
        tv_forget_password.clickWithTrigger {

            startActivity(Intent(context, FindPwdActivity::class.java)) }

        //登录
        login_btn.clickWithTrigger {
            if (model.canPwdLogin()) {
                model.loginByPwd(this@PwdToLoginFragment) { activity?.finish() }
            }
        }
  }
}