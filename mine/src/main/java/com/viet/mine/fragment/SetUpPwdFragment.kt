package com.viet.mine.fragment

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chenenyu.router.annotation.Route
import com.jakewharton.rxbinding2.widget.RxTextView
import com.safframework.ext.clickWithTrigger
import com.viet.mine.R
import com.viet.mine.viewmodel.AccountInfoViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.finishWithAnim
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_mine_setting_resetpwd.*

/**
 * @Description
 * @author null
 * @date 2018/9/17
 * @Email zongjia.long@merculet.io
 * @Version
 */


@Route(value = [Config.ROUTER_MINE_EDIT_SETUP_PWD_FRAGMENT])
class SetUpPwdFragment : BaseFragment() {
    private var mContainerView: View? = null
    private val model by viewModelDelegate(AccountInfoViewModel::class)

    override fun isSupportSwipeBack(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine_setting_setuppwd
    }

    @SuppressLint("CheckResult")
    override fun initView(view: View) {
        RxTextView.textChanges(new_password_input)
                .subscribe {
                    model.setNewPwd.value = it.toString()
                    model.checkSetupSubmitBtnEnable()
                }

        RxTextView.textChanges(password_input_confirm)
                .subscribe {
                    model.confirmNewPwd.value = it.toString()
                    model.checkSetupSubmitBtnEnable()
                }


        confirm_btn.clickWithTrigger {
            if (model.setupSubmitEnable()) {
                var oldpwd = old_password_input.text.toString()
                val newpwd = new_password_input.text.toString()
                val phone = arguments?.getString("phone_number")
                val verifyCode = arguments?.getString("verify_code")
                model.setPassword(phone, verifyCode, newpwd, this) {
                    (activity as BaseActivity).finishWithAnim()
                }
            }
        }

        model.setupEnable.observe(this, Observer { confirm_btn.isEnabled = it != null && it })

    }
}