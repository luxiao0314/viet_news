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
import com.viet.news.core.ui.RealVisibleHintBaseFragment
import kotlinx.android.synthetic.main.fragment_register_next.*

/**
 * @author Tsing
 * @email shuqing.li@magicwindow.cn
 */

//@Route(value = [(Config.ROUTER_REGISTER_FRAGMENT)])
class RegisterNextFragment : RealVisibleHintBaseFragment() {

    private val model by viewModelDelegate(LoginViewModel::class, true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_register_next, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    @SuppressLint("CheckResult")
    override fun initView(view: View) {
        //密码1
        RxTextView.textChanges(password_input)
                .subscribe {
                    model.registerPwd.value = it.toString()
                    model.checkRegisterBtnEnable()
                }

        //密码2
        RxTextView.textChanges(password_input_again)
                .subscribe {
                    model.registerConfirmPwd.value = it.toString()
                    model.checkRegisterBtnEnable()
                }

        //注册
        register_btn.clickWithTrigger {
//            if (model.registerBtnEnable()) {
//                activity?.finish()
//            }
            model.signIn(this){
            }
        }
    }

    private fun initEvent() {
        //注册按钮能否点击更新
        model.registerBtnEnable.observe(this, Observer { register_btn.isEnabled = it != null && it })
    }
}