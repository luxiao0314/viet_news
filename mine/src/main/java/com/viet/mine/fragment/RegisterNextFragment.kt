package com.viet.mine.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.viet.mine.R
import com.viet.mine.viewmodel.LoginViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.clickWithTrack
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.RealVisibleHintBaseFragment
import com.viet.news.webview.WebActivity
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * @author Tsing
 * @email shuqing.li@magicwindow.cn
 */

//@Route(value = [(Config.ROUTER_REGISTER_FRAGMENT)])
class RegisterNextFragment : RealVisibleHintBaseFragment() {

    private val model by viewModelDelegate(LoginViewModel::class, true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_register_next, container, false)

    override fun initView(view: View) {
        //手机号
        RxTextView.textChanges(phone_input)
                .subscribe {
                    model.registerPhoneNumber.value = it.toString()
                    model.checkRegisterVCodeBtnEnable()
                }

        //点击协议
        agreement_text.clickWithTrack(Config.login_userProtocol, 2000) {
            WebActivity.launch(context!!, Config.PACT_URL)
        }

        //+86点击事件
        select_country_text.clickWithTrigger {
//            routerWithAnim(Config.ROUTER_SELECT_COUNTRY_ACTIVITY)
//                    .with(Config.BUNDLE_COUNTRIES, model.currentCountryResponse.value)
//                    .with(Config.BUNDLE_SELECTED_COUNTRY, model.countryAbbreviation.value)
//                    .requestCode(0).go(this)
        }

        //+86展示更新
        model.zoneCode.observe(this, Observer {
            select_country_text.text = "+$it"
        })

        //错误信息展示
        model.statusMsg.observe(this, Observer {
            it?.let { msg ->
                toast(msg).show()
            }
        })

        //注册按钮能否点击更新
        model.registerVCodeEnable.observe(this, Observer {
            register_btn.isEnabled = it != null && it
        })

        //注册按钮点击事件
        register_btn.clickWithTrigger { button ->
//            if (model.verificationEnable()) {
//                model.canRegister(this)
//                        .subscribe {
//                            it.data?.then({
//                                //未注册，可注册
//                                routerWithAnim(Config.ROUTER_GET_VCODE_ACTIVITY)
//                                        .with(Config.BUNDLE_VCODE_FROM_KEY, Config.VCODE_FROM_REGISTER_VALUE)
//                                        .with(Config.BUNDLE_ZONE_CODE, model.zoneCode.value)
//                                        .with(Config.BUNDLE_PHONE_NUM, model.registerPhoneNumber.value)
//                                        .go(this)
//                            }, {
//                                //已注册
//                                it.message?.let {
//                                    toast(it).show()
//                                }
//                            })
//                        }
//            }
        }

    }
}