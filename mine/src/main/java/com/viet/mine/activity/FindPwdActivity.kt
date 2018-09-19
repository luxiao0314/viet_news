package com.viet.mine.activity

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import com.viet.mine.R
import com.viet.mine.fragment.FindPwdNextFragment
import com.viet.mine.viewmodel.FindPwdViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.config.FragmentExchangeManager
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.InjectActivity
import kotlinx.android.synthetic.main.activity_mine_find_pwd.*

/**
 * @Description 找回密码
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 06/09/2018 10:33 AM
 * @Version
 */
class FindPwdActivity : InjectActivity() {

    private val model by viewModelDelegate(FindPwdViewModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_find_pwd)
        initView()
        initData()
    }

    @SuppressLint("CheckResult")
    private fun initView() {

        //错误信息展示
        model.statusMsg.observe(this, Observer { it?.let { msg -> toast(msg)  } })

        //验证码按钮是否可点击
        model.vCodeButtonEnable.observe(this, Observer { btn_send_vcode.isEnabled = it != null && it })
        //下一步按钮是否可点击
        model.nextButtonEnable.observe(this, Observer { next_btn.isEnabled = it != null && it })
        // 判断是否倒计时内
        model.countDown.observe(this, Observer<Int> {
            if (it != null && it > 0 && it < (Config.COUNT_DOWN_TIMER / 1000).toInt()) {
                //正在倒计时
                model.vCodeButtonEnable.value = false
                btn_send_vcode.text = String.format(getString(R.string.sending_code_s), it.toString())
            } else {
                //倒计时未开始/已结束
                model.vCodeButtonEnable.value = true
                btn_send_vcode.text = getString(R.string.get_vcode)
            }
        })

        //手机
        RxTextView.textChanges(phone_input)
                .subscribe {
                    model.phoneNumber.value = it.toString()
                    model.checkNextButtonEnable()
                }
        //验证码
        RxTextView.textChanges(vcode_input)
                .subscribe {
                    model.vCode.value = it.toString()
                    model.checkNextButtonEnable()
                }
        btn_send_vcode.clickWithTrigger {
            if (model.canSendVCode()) {
                model.sendSMS(this,{
                    tv_hint.visibility = View.INVISIBLE
                },{
                    tv_hint.visibility = View.VISIBLE
                })
            }
        }


        //下一步
        next_btn.clickWithTrigger {
            if (model.canNext()) {
                model.checkVerifyCode(this){
                 //校验验证码成功
                FragmentExchangeManager.addFragment(supportFragmentManager, FindPwdNextFragment(), R.id.container_frame_layout, "FindPwdNextFragment", true)
                }
            }
        }
    }

    private fun initData() {

    }
}