package com.viet.mine.fragment

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.view.View
import com.chenenyu.router.annotation.Route
import com.jakewharton.rxbinding2.widget.RxTextView
import com.viet.mine.R
import com.viet.mine.viewmodel.AccountInfoViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ext.finishWithAnim
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_mine_setting_resetpwd.*

@Route(value = [Config.ROUTER_MINE_EDIT_CHANGE_PWD_FRAGMENT])
class ResetPasswordFragment : BaseFragment() {
    private val model by viewModelDelegate(AccountInfoViewModel::class)

    override fun isSupportSwipeBack(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine_setting_resetpwd
    }

    @SuppressLint("CheckResult")
    override fun initView(view: View) {
        //手机号
        RxTextView.textChanges(old_password_input)
                .subscribe {
                    model.oldPwd.value = it.toString()
                    model.checkResetSubmitBtnEnable()
                }

        //手机号
        RxTextView.textChanges(new_password_input)
                .subscribe {
                    model.newPwd.value = it.toString()
                    model.checkResetSubmitBtnEnable()
                }

        //手机号
        RxTextView.textChanges(password_input_confirm)
                .subscribe {
                    model.confirmPwd.value = it.toString()
                    model.checkResetSubmitBtnEnable()
                }

        confirm_btn.clickWithTrigger {
            if (model.reSetSubmitEnable()) {
                val oldPwd = old_password_input.text.toString()
                val newPwd = new_password_input.text.toString()
                model.resetPwdWithOldPwd(oldPwd, newPwd, this@ResetPasswordFragment) {
                    (activity as BaseActivity).finishWithAnim()
                    toast("修改成功")
                }
            }
        }


        model.resetEnable.observe(this, Observer { confirm_btn.isEnabled = it != null && it  })
    }


}