package com.viet.mine.fragment

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import com.viet.mine.R
import com.viet.mine.viewmodel.FindPwdViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ext.routerToMain
import com.viet.news.core.ui.RealVisibleHintBaseFragment
import kotlinx.android.synthetic.main.fragment_register_next.*

/**
 * @author Tsing
 * @email shuqing.li@magicwindow.cn
 */

//@Route(value = [(Config.ROUTER_REGISTER_FRAGMENT)])
class FindPwdNextFragment : RealVisibleHintBaseFragment() {

    private val model by viewModelDelegate(FindPwdViewModel::class, true)

    override fun isSupportSwipeBack(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_find_pwd_next
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    @SuppressLint("CheckResult")
    override fun initView(view: View) {
        //密码1
        RxTextView.textChanges(password_input)
                .subscribe {
                    model.password1.value = it.toString()
                    model.checkResetButtonEnable()
                }

        //密码2
        RxTextView.textChanges(password_input_again)
                .subscribe {
                    model.password2.value = it.toString()
                    model.checkResetButtonEnable()
                }

        //设置密码并登陆
        register_btn.clickWithTrigger {
            if (model.canSetPwd()) {
                model.setPasswordThenLogin(this) {
                    //直接跳转到主界面，同时会关闭其他上层界面
                    routerToMain()
                }
            }
        }
    }

    private fun initEvent() {
        //注册按钮能否点击更新
        model.nextButtonEnable.observe(this, Observer { register_btn.isEnabled = it != null && it })
    }
}