package com.viet.mine.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.viet.mine.R
import com.viet.mine.viewmodel.LoginViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.config.Config
import com.viet.news.core.domain.User
import com.viet.news.core.ext.click
import com.viet.news.core.ext.clickWithTrack
import com.viet.news.core.ui.RealVisibleHintBaseFragment
import com.viet.news.webview.WebActivity
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * @author Tsing
 * @email shuqing.li@magicwindow.cn
 */

//@Route(value = [(Config.ROUTER_LOGIN_FRAGMENT)])
class LoginFragment : RealVisibleHintBaseFragment() {

    private val model by viewModelDelegate(LoginViewModel::class, true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_login, container, false)

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {

        initUserInfo()

        model.zoneCode.observe(this, Observer {
            select_country_text.text = "+$it"
        })

        model.loginEnable.observe(this, Observer {
            register_btn.isEnabled = it != null && it
        })

        //初始化用户协议
        model.agreement.value = agreement_checkbox.isChecked

        //手机号
        RxTextView.textChanges(phone_input)
                .subscribe {
                    model.phoneNumber.value = it.toString()
                    model.checkLoginBtnEnable()
                }
        //密码
        RxTextView.textChanges(password_input)
                .subscribe {
                    model.password.value = it.toString()
                    model.checkLoginBtnEnable()
                }
        //同意
        agreement_checkbox.click {
            model.agreement.value = agreement_checkbox.isChecked
            model.checkLoginBtnEnable()
        }
        //点击协议
        agreement_text.clickWithTrack(Config.login_userProtocol, 2000) {
            WebActivity.launch(context!!, Config.PACT_URL)
        }

        //+86点击
//        select_country_text.clickWithTrigger {
//            routerWithAnim(Config.ROUTER_SELECT_COUNTRY_ACTIVITY)
//                    .with(Config.BUNDLE_COUNTRIES, model.currentCountryResponse.value)
//                    .with(Config.BUNDLE_SELECTED_COUNTRY, model.countryAbbreviation.value)
//                    .requestCode(0)
//                    .go(this)
//        }
//        //忘记密码
//        tv_forget_password.clickWithTrigger {
//            routerWithAnim(Config.ROUTER_FORGET_PASSWORD_ACTIVITY)
//                    .with(Config.BUNDLE_COUNTRIES, model.currentCountryResponse.value)
//                    .with(Config.BUNDLE_SELECTED_COUNTRY, model.countryAbbreviation.value)
//                    .with(Config.COUNTRY, model.zoneCode.value)
//                    .requestCode(0)
//                    .go(this)
//        }
//        //验证码登录
//        tv_login_with_verify_code.clickWithTrigger {
//            routerWithAnim(Config.ROUTER_LOGIN_BY_SMS_ACTIVITY)
//                    .with(Config.BUNDLE_COUNTRIES, model.currentCountryResponse.value)
//                    .with(Config.BUNDLE_SELECTED_COUNTRY, model.countryAbbreviation.value)
//                    .with(Config.COUNTRY, model.zoneCode.value)
//                    .requestCode(0)
//                    .go(this)
//        }

    }




    //对用户可见
    override fun onFragmentResume() {
        //初始化用户协议
        model.agreement.value = agreement_checkbox.isChecked
    }

    private fun initUserInfo() {
        if (User.currentUser.telephone.isNotBlank()) {
            model.phoneNumber.value = User.currentUser.telephone
            phone_input.setText(User.currentUser.telephone)
            phone_input.setSelection(User.currentUser.telephone.length)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
//            val result = data?.getSerializableExtra(Config.COUNTRY) as ListCountriesResponse
//            model.zoneCode.value = result.zoneCode
//            model.countryAbbreviation.value = result.countryAbbreviation
//            model.countryEnglishName.value = result.countryEnglishName
        }
    }
}