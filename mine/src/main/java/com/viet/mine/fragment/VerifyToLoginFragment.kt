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
import com.viet.news.core.config.VerifyCodeTypeEnum
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.User
import com.viet.news.core.ext.clickWithTrigger
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
        phone_input.setText(User.currentUser.telephone)
        // 判断是否倒计时内
        model.countDown4Login.observe(this, Observer<Int> {
            if (it != null && it > 0 && it < (Config.COUNT_DOWN_TIMER / 1000).toInt()) {
                //正在倒计时
                btn_send_vcode.isEnabled = false
                btn_send_vcode.text = String.format(getString(R.string.sending_code_s), it.toString())
            } else {
                btn_send_vcode.isEnabled = true
                btn_send_vcode.text = getString(R.string.get_vcode)
                //倒计时未开始/已结束
            }
        })

        //注册按钮能否点击更新
        model.vCodeLoginButtonEnable.observe(this, Observer { login_btn.isEnabled = it != null && it })

        //手机号
        RxTextView.textChanges(phone_input)
                .subscribe {
                    model.vCodeLoginPhoneNumber.value = it.toString()
                    model.checkVCodeLoginSendVCodeButtonEnable()
                    model.checkVCodeLoginButtonEnable()
                }
        //验证码
        RxTextView.textChanges(vcode_input)
                .subscribe {
                    model.vCodeLoginVCode.value = it.toString()
                    model.checkVCodeLoginButtonEnable()
                }

        btn_send_vcode.setOnClickListener {
            if (model.canSendLoginVCode()) {
                model.sendSMS(this, type = VerifyCodeTypeEnum.LOGIN) {
                }
            }
        }

        //注册按钮点击事件
        login_btn.clickWithTrigger {
            if (model.canVCodeLogin()) {
                model.loginBySMS(this) {
                    activity?.finish()
                }
            }
        }
    }
}