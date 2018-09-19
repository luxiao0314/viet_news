package com.viet.mine.fragment

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chenenyu.router.Router
import com.chenenyu.router.annotation.Route
import com.safframework.ext.clickWithTrigger
import com.viet.mine.R
import com.viet.mine.viewmodel.AccountInfoViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.Settings
import com.viet.news.core.domain.User
import com.viet.news.core.ext.finishWithAnim
import com.viet.news.core.ext.goFragment
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.ui.code.CodeView
import com.viet.news.core.utils.SPHelper
import kotlinx.android.synthetic.main.fragment_mine_verify_code.*

/**
 * @Description 验证码
 * @author null
 * @date 2018/9/10
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_EDIT_VERIFY_CODE_FRAGMENT])
class VerifyCodeFragment : BaseFragment() {
    private var mContainerView: View? = null
    private var phone = "18812345678"
    private val model by viewModelDelegate(AccountInfoViewModel::class)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_verify_code, container, false)
        initListener()
        return mContainerView
    }

    private fun initListener() {
        count_down.clickWithTrigger {
            if (it.text.toString().equals(getString(R.string.get_vcode))) {
                when (arguments?.getInt("page_type")) {
                    Config.CHANGE_PHONE_NUM -> {
                        model.startChangePhoneCountdown(Config.COUNT_DOWN_TIMER)
                        model.sendSMS(User.currentUser.telephone, this) {}
                    }
                    Config.SET_PHONE_NUM -> {
                        model.startSetNewPhoneCountdown(Config.COUNT_DOWN_TIMER)
                        model.sendSMS(arguments!!.getString("phone_number", ""), this) {}
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        keyboard.hide()
        password_view.clickWithTrigger { keyboard.show() }
        keyboard.setCodeView(password_view)
        password_view.setListener(object : CodeView.Listener {
            override fun onValueChanged(value: String?) {
                //TODO:待处理
            }

            override fun onComplete(value: String) {
//                if (value == "123456") {
//                    when (arguments?.getInt("page_type")) {
//                        Config.CHANGE_PHONE_NUM -> {
//                            SPHelper.create().putString("verify_code", value)
//                            val bundle = Bundle()
//                            bundle.putInt("change_phone_type", Config.CHANGE_PHONE_NUM)
//                            Router.build(Config.ROUTER_MINE_EDIT_BIND_PHONE_FRAGMENT).with(bundle).goFragment(this@VerifyCodeFragment, R.id.container_framelayout, false)
//                        }
//                        Config.SET_PHONE_NUM -> {
//                            when (arguments!!["change_phone_type"]) {
//                                Config.BIND_CHANGE_PHONE_NUM -> {
//                                    model.resetPhoneNum(arguments!!.getString("phone_number", ""), User.currentUser.telephone, value, SPHelper.create().getString("verify_code"), this@VerifyCodeFragment) {
//                                        (activity as BaseActivity).finishWithAnim()
//                                    }
//                                }
//                                Config.BIND_SET_PHONE_NUM -> {
//                                    //设置密码
//                                    val bundle = Bundle()
//                                    bundle.putString("phone_number", arguments?.getString("phone_number"))
//                                    bundle.putString("verify_code", value)
//                                    Router.build(Config.ROUTER_MINE_EDIT_SETUP_PWD_FRAGMENT).with(bundle).goFragment(this@VerifyCodeFragment, R.id.container_framelayout)
//                                }
//                            }
//                        }
//                    }
//                }
                var phone = ""
                when (arguments?.getInt("page_type")) {
                    Config.CHANGE_PHONE_NUM -> {
                        phone = User.currentUser.telephone
                    }
                    Config.SET_PHONE_NUM -> {
                        phone = arguments!!.getString("phone_number", "")
                    }
                }

                model.checkVerifyCode(phone, value, this@VerifyCodeFragment) {
                    when (arguments?.getInt("page_type")) {
                        Config.CHANGE_PHONE_NUM -> {
                            SPHelper.create().putString("verify_code", value)
                            val bundle = Bundle()
                            bundle.putInt("change_phone_type", Config.CHANGE_PHONE_NUM)
                            Router.build(Config.ROUTER_MINE_EDIT_BIND_PHONE_FRAGMENT).with(bundle).goFragment(this@VerifyCodeFragment, R.id.container_framelayout, false)
                        }
                        Config.SET_PHONE_NUM -> {
                            when (arguments!!["change_phone_type"]) {
                                Config.BIND_CHANGE_PHONE_NUM -> {
                                    model.resetPhoneNum(arguments!!.getString("phone_number", ""), phone, value, SPHelper.create().getString("verify_code"), this@VerifyCodeFragment) {
                                        toast("修改成功")
                                        (activity as BaseActivity).finishWithAnim()
                                    }
                                }
                                Config.BIND_SET_PHONE_NUM -> {
                                    //设置密码
                                    val bundle = Bundle()
                                    bundle.putString("phone_number", phone)
                                    bundle.putString("verify_code", value)
                                    Router.build(Config.ROUTER_MINE_EDIT_SETUP_PWD_FRAGMENT).with(bundle).goFragment(this@VerifyCodeFragment, R.id.container_framelayout)
                                }
                            }
                        }
                    }
                }
            }
        })
        when (arguments!!["page_type"]) {
            Config.CHANGE_PHONE_NUM -> {
                phone = Settings.create(context!!).telephone
                model.startChangePhoneCountdown(Config.COUNT_DOWN_TIMER)
                model.changePhoneCountDown.observe(this, Observer {
                    if (it != null && it > 0 && it < (Config.COUNT_DOWN_TIMER / 1000).toInt()) {
                        //正在倒计时
                        count_down.text = String.format(getString(R.string.sending_code_s), it.toString())
                    } else {
                        //倒计时未开始/已结束
                        count_down.text = getString(R.string.get_vcode)//重新发送验证码
                    }
                })
            }
            Config.SET_PHONE_NUM -> {
                phone = arguments!!.getString("phone_number", "")
                model.startSetNewPhoneCountdown(Config.COUNT_DOWN_TIMER)
                model.setNewPhoneCountDown.observe(this, Observer {
                    if (it != null && it > 0 && it < (Config.COUNT_DOWN_TIMER / 1000).toInt()) {
                        //正在倒计时
                        count_down.text = String.format(getString(R.string.sending_code_s), it.toString())
                    } else {
                        //倒计时未开始/已结束
                        count_down.text = getString(R.string.get_vcode)//重新发送验证码
                    }
                })
            }
        }
        phone_num.text = if (phone.isNotBlank()) "验证码已经发送至${phone.replaceRange(3..6, "****")}" else ""
    }

}