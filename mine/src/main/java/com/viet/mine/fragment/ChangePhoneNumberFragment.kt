package com.viet.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chenenyu.router.annotation.Route
import com.viet.mine.R
import com.viet.news.core.config.Config
import com.viet.news.core.domain.Settings
import com.viet.news.core.ext.clickWithTrigger
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
    private var mContainerView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_setting_change_phone, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
        val phoneNum = Settings.create(context!!).telephone
        if (phoneNum.length == 11) {
            phone_num.text = phoneNum.replaceRange(3..6, "****")
        }
        confirm_btn.clickWithTrigger {
            openPage(this@ChangePhoneNumberFragment, Config.ROUTER_MINE_EDIT_VERIFY_CODE_FRAGMENT, R.id.container_framelayout)
        }

    }


}