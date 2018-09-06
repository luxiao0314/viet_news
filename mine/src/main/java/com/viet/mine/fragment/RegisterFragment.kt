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
import com.viet.news.core.config.FragmentExchangeManager
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.RealVisibleHintBaseFragment
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * @author Tsing
 * @email shuqing.li@magicwindow.cn
 */

//@Route(value = [(Config.ROUTER_REGISTER_FRAGMENT)])
class RegisterFragment : RealVisibleHintBaseFragment() {

    private val model by viewModelDelegate(LoginViewModel::class, true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    @SuppressLint("CheckResult")
    override fun initView(view: View) {
        //手机号
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

        //+86点击事件
        select_country_text.clickWithTrigger {}

        //注册按钮点击事件
        register_btn.clickWithTrigger {
            if (model.nextBtnEnable()) {
                //未注册，可注册
                FragmentExchangeManager.addFragment(fragmentManager, RegisterNextFragment(), R.id.constraintLayout, "RegisterNextFragment",true)
            }
        }
    }

    private fun initEvent() {
        //+86展示更新
        model.zoneCode.observe(this, Observer {
            select_country_text.text = "+$it"
        })

        //错误信息展示
        model.statusMsg.observe(this, Observer { it?.let { msg -> toast(msg).show() } })

        //注册按钮能否点击更新
        model.registerVCodeEnable.observe(this, Observer { register_btn.isEnabled = it != null && it })
    }
}