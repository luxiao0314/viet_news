package com.viet.mine.fragment

import android.os.Bundle
import android.view.View
import com.chenenyu.router.Router
import com.chenenyu.router.annotation.Route
import com.viet.mine.R
import com.viet.mine.viewmodel.AccountInfoViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ext.goFragment
import com.viet.news.core.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_mine_bind_new_phone_num.*

/**
 * @Description
 * @author null
 * @date 2018/9/10
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_EDIT_BIND_PHONE_FRAGMENT])
class BindNewPhoneNumFragment : BaseFragment() {
    private val model by viewModelDelegate(AccountInfoViewModel::class)

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine_bind_new_phone_num
    }

    override fun initView(view: View) {

        bind_phone_next_btn.clickWithTrigger {
            when (arguments!!["change_phone_type"]) {
                Config.BIND_SET_PHONE_NUM -> {
                    setPhone(Config.BIND_SET_PHONE_NUM)
                }
                Config.BIND_CHANGE_PHONE_NUM -> {
                    setPhone(Config.BIND_CHANGE_PHONE_NUM)
                }
            }
        }
    }


    private fun setPhone(code: Int) {
        val phone = phone_input.text.toString()
        model.sendSMS(phone, this) {
            val bundle = Bundle()
            bundle.putInt("page_type", Config.SET_PHONE_NUM)
            bundle.putInt("change_phone_type", code)
            bundle.putString("phone_number", phone)
            Router.build(Config.ROUTER_MINE_EDIT_VERIFY_CODE_FRAGMENT).with(bundle).goFragment(this@BindNewPhoneNumFragment, R.id.container_framelayout, false)
        }
    }
}