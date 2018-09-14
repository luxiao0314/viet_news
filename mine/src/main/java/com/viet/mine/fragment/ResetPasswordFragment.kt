package com.viet.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chenenyu.router.annotation.Route
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
    private var mContainerView: View? = null
    private val model by viewModelDelegate(AccountInfoViewModel::class)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_setting_resetpwd, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
        confirm_btn.clickWithTrigger {
            var oldpwd = old_password_input.text.toString()
            val newpwd = new_password_input.text.toString()
            model.resetPwdWithOldPwd(oldpwd, newpwd,this@ResetPasswordFragment) {
                (activity as BaseActivity).finishWithAnim()
                toast("修改成功")
            }
        }
    }
}