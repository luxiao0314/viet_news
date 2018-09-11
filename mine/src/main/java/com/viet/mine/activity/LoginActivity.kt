package com.viet.mine.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.KeyEvent
import com.facebook.CallbackManager
import com.safframework.ext.click
import com.viet.mine.R
import com.viet.mine.viewmodel.LoginViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.dsl.addOnPageChangeListener
import com.viet.news.core.ext.clickWithTrack
import com.viet.news.core.ui.InjectActivity
import com.viet.news.core.ui.TabFragmentAdapter
import com.viet.news.webview.WebActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 * @Description 登录注册页面
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 06/09/2018 1:42 PM
 * @Version 1.0.0
 */
//@Route(value = [(Config.ROUTER_LOGIN_ACTIVITY)])
class LoginActivity : InjectActivity() {

    @Inject
    internal lateinit var adapter: TabFragmentAdapter
    private val model by viewModelDelegate(LoginViewModel::class)
    val callbackManager = CallbackManager.Factory.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        initListener()
    }

    private fun initView() {
        adapter.setTitles(model.titles)
        adapter.setFragment(model.fragments)
        viewpager.adapter = adapter
        tablayout.setViewPager(viewpager)
        setTabText(0, 1)

        //点击协议
        agreement_text.clickWithTrack(Config.login_userProtocol, 2000) {
            WebActivity.launch(this, Config.PACT_URL)
        }

        login_button.setOnLoginListener(this, callbackManager) {
            onSuccess = {
                //...
            }
            onCancel = {
                //...
            }
            onError = {
                //...
            }
        }
    }

    private fun initListener() {
        iv_close.click { finish() }
        viewpager.addOnPageChangeListener { onPageSelected = { setTabText(it, model.currentTab) } }
    }

    private fun setTabText(currentTab: Int, otherTab: Int) {
        tablayout.getTitleView(currentTab).textSize = 25F
        tablayout.getTitleView(otherTab).textSize = 15F
        tablayout.getTitleView(currentTab).typeface = Typeface.DEFAULT_BOLD
        tablayout.getTitleView(otherTab).typeface = Typeface.DEFAULT
        model.currentTab = currentTab
    }

    /**
     * 双击返回键退出
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return if (model.currentTab != 0) {
                tablayout.currentTab = 0
                true
            } else {
                super.onKeyDown(keyCode, event)
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}