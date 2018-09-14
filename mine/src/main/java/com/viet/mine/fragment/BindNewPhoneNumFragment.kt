package com.viet.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chenenyu.router.Router
import com.chenenyu.router.annotation.Route
import com.viet.mine.R
import com.viet.news.core.config.Config
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
    private var mContainerView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_bind_new_phone_num, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
        bind_phone_next_btn.clickWithTrigger {
            var bundle = Bundle()
            bundle.putInt("page_type", Config.SET_PHONE_NUM)
            bundle.putString("phone_number",phone_input.text.toString())
            Router.build(Config.ROUTER_MINE_EDIT_VERIFY_CODE_FRAGMENT).with(bundle).goFragment(this@BindNewPhoneNumFragment, R.id.container_framelayout,false)
        }
    }
}