package com.viet.mine.activity

import android.os.Bundle
import com.viet.mine.R
import com.viet.mine.fragment.LoginFragment
import com.viet.mine.fragment.RegisterFragment
import com.viet.mine.viewmodel.LoginViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.TabFragmentAdapter
import kotlinx.android.synthetic.main.activity_login.*

//@Route(value = [(Config.ROUTER_LOGIN_ACTIVITY)])
class LoginActivity : BaseActivity() {
    private val model by viewModelDelegate(LoginViewModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViewPager()
    }

    private fun initViewPager() {
        val tabFragmentAdapter = TabFragmentAdapter(supportFragmentManager)
        viewpager.adapter = tabFragmentAdapter
        tabFragmentAdapter.addFragment(RegisterFragment())
        tabFragmentAdapter.addTitle(model.titleList[0])
        tabFragmentAdapter.addFragment(LoginFragment())
        tabFragmentAdapter.addTitle(model.titleList[1])
        tabFragmentAdapter.notifyDataSetChanged()
    }
}