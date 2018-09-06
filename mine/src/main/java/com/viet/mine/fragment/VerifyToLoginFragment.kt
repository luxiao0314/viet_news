package com.viet.mine.fragment

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.viet.mine.R
import com.viet.mine.viewmodel.LoginViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.RealVisibleHintBaseFragment
import kotlinx.android.synthetic.main.fragment_login_verify.*

/**
 * @Description 登录tab
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 05/09/2018 5:28 PM
 * @Version
 */
//@Route(value = [(Config.ROUTER_LOGIN_FRAGMENT)])
class VerifyToLoginFragment : RealVisibleHintBaseFragment() {

    private val model by viewModelDelegate(LoginViewModel::class, true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_login_verify, container, false)

    @SuppressLint("CheckResult")
    override fun initView(view: View) {
        RxTextView.textChanges(phone_input)
                .subscribe {
                    model.registerPhoneNumber.value = it.toString()
                    model.checkRegisterVCodeBtnEnable()
                }
        RxTextView.textChanges(vcode_input)
                .subscribe {
                    model.registerVCode.value = it.toString()
                    model.checkRegisterVCodeBtnEnable()
                }

        //错误信息展示
        model.statusMsg.observe(this, Observer { it?.let { msg -> toast(msg).show() } })

        //注册按钮能否点击更新
        model.registerVCodeEnable.observe(this, Observer { login_btn.isEnabled = it != null && it })

        //注册按钮点击事件
        login_btn.clickWithTrigger {
            if (model.nextBtnEnable()) {
                activity?.finish()
            }
        }
    }
}