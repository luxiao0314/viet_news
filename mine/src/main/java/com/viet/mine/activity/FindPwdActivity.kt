package com.viet.mine.activity

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import com.jakewharton.rxbinding2.widget.RxTextView
import com.viet.mine.R
import com.viet.mine.viewmodel.LoginViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_mine_find_pwd.*

/**
 * @Description 找回密码
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 06/09/2018 10:33 AM
 * @Version
 */
class FindPwdActivity : BaseActivity() {

    private val model by viewModelDelegate(LoginViewModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_find_pwd)
        initView()
        initData()
    }

    @SuppressLint("CheckResult")
    private fun initView() {
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
        model.registerVCodeEnable.observe(this, Observer { next_btn.isEnabled = it != null && it })

        //注册按钮点击事件
        next_btn.clickWithTrigger {
            if (model.nextBtnEnable()) {
                finish()
            }
        }
    }

    private fun initData() {

    }
}