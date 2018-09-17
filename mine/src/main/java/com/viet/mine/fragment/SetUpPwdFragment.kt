package com.viet.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chenenyu.router.annotation.Route
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_setting_setuppwd, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
        confirm_btn.clickWithTrigger {
            var oldpwd = old_password_input.text.toString()
            val newpwd = new_password_input.text.toString()
            val phone = arguments?.getString("phone_number")
            val verifyCode = arguments?.getString("verify_code")
            model.setPassword(phone, verifyCode, newpwd, this) {
                (activity as BaseActivity).finishWithAnim()
            }
        }


    }
}