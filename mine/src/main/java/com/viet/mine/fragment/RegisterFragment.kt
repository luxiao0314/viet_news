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
import com.viet.news.core.config.Config
import com.viet.news.core.config.FragmentExchangeManager
import com.viet.news.core.config.VerifyCodeTypeEnum
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.clickWithTrigger
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
                    model.signInPhoneNumber.value = it.toString()
                    model.checkSignInSendVCodeEnable()
                    model.checkSignInNextButtonEnable()
                }

        //验证码
        RxTextView.textChanges(vcode_input)
                .subscribe {
                    model.signInVCode.value = it.toString()
                    model.checkSignInNextButtonEnable()
                }
        //邀请码
        RxTextView.textChanges(vcode_input)
                .subscribe {
                    model.signInInviteCode.value = it.toString()
                }

        //发送验证码
        btn_send_vcode.setOnClickListener {
            if (model.canSendSignInVCode()) {
                model.sendSMS(this, type = VerifyCodeTypeEnum.REGISTER) {
                }
            }
        }

        //+86点击事件
        select_country_text.clickWithTrigger {}

        //注册按钮点击事件
        register_btn.clickWithTrigger {
            if (model.canNextSign()) {
                //校验验证码，然后跳转到设置密码
                model.checkVerifyCode(this, type = VerifyCodeTypeEnum.REGISTER) {
                    FragmentExchangeManager.addFragment(fragmentManager, RegisterNextFragment(), R.id.constraintLayout, "RegisterNextFragment", true)
                }
            }
        }
    }

    private fun initEvent() {
        //+86展示更新
        model.zoneCode.observe(this, Observer {
            select_country_text.text = "+$it"
        })
        //发送验证码可否点击
        model.signInSendVCodeEnable.observe(this, Observer { btn_send_vcode.isEnabled = it != null && it })
        //注册下一步按钮能否点击更新
        model.signInNextButtonEnable.observe(this, Observer { register_btn.isEnabled = it != null && it })
        // 判断是否倒计时内
        model.countDown4SignIn.observe(this, Observer<Int> {
            if (it != null && it > 0 && it < (Config.COUNT_DOWN_TIMER / 1000).toInt()) {
                //正在倒计时
                model.signInSendVCodeEnable.value = false
                btn_send_vcode.text = String.format(getString(R.string.sending_code_s), it.toString())
            } else {
                //倒计时未开始/已结束
                model.signInSendVCodeEnable.value = true
                btn_send_vcode.text = getString(R.string.get_vcode)
            }
        })
    }
}