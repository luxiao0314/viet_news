package com.viet.mine.fragment

import android.os.Bundle
import android.view.View
import com.chenenyu.router.Router
import com.chenenyu.router.annotation.Route
import com.viet.mine.R
import com.viet.mine.viewmodel.AccountInfoViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.Settings
import com.viet.news.core.domain.User
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ext.goFragment
import com.viet.news.core.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_mine_setting_change_phone.*

/**
 * @Description 修改手机号
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_EDIT_CHANGE_PHONE_FRAGMENT])
class ChangePhoneNumberFragment : BaseFragment() {
    private val model by viewModelDelegate(AccountInfoViewModel::class)

    override fun isSupportSwipeBack(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine_setting_change_phone
    }

    override fun initView(view: View) {
        val phoneNum = Settings.create(context!!).telephone
        if (phoneNum.length == 11) {
            phone_num.text = phoneNum.replaceRange(3..6, "****")
        }
        if (phoneNum.isNotBlank()) {
            confirm_btn.clickWithTrigger {
                model.sendSMS(User.currentUser.telephone, this) {
                    val bundle = Bundle()
                    bundle.putInt("page_type", Config.CHANGE_PHONE_NUM)
                    bundle.putString("phone_number", User.currentUser.telephone)
                    Router.build(Config.ROUTER_MINE_EDIT_VERIFY_CODE_FRAGMENT).with(bundle).goFragment(this@ChangePhoneNumberFragment, R.id.container_framelayout)
                }
            }
        }

    }


}