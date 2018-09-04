package com.viet.mine.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import com.safframework.ext.click
import com.safframework.ext.dimen2px
import com.safframework.ext.hideKeyboard
import com.viet.mine.R
import com.viet.mine.fragment.LoginFragment
import com.viet.mine.fragment.RegisterFragment
import com.viet.mine.viewmodel.LoginViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.Config
import com.viet.news.core.domain.User
import com.viet.news.core.ui.App
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.TabFragmentAdapter
import kotlinx.android.synthetic.main.activity_login.*


//@Route(value = [(Config.ROUTER_LOGIN_ACTIVITY)])
class LoginActivity : BaseActivity(), LoginAndSignInListener {
    private val model by viewModelDelegate(LoginViewModel::class)

    private var isOpen: Boolean = false

    private val titleList = listOf(App.instance.resources.getString(R.string.log_in), App.instance.resources.getString(R.string.sign_in))

    private val FRAGMENT_LOGIN = 0
    private val FRAGMENT_REGISTER = 1

    private var mSelfHeight = 0f  //用以判断是否得到正确的宽高值
    private var mLoginRegisterScaleY: Float = 0.toFloat()
    private var mLoginScaleX: Float = 0.toFloat()
    private var mLoginScaleX1: Float = 0.toFloat()
    private var mRegisterScaleX: Float = 0.toFloat()
    private var mRegisterScaleX1: Float = 0.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViewPager()
        initListener()
        initData()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (KeyEvent.KEYCODE_BACK == keyCode && !isOpen) {
            openTitle()
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onBackPressed() {
        if (!isOpen) {
            openTitle()
        } else {
            finish()
            super.onBackPressed()
        }
    }

    private fun initViewPager() {
        val tabFragmentAdapter = TabFragmentAdapter(supportFragmentManager)
        viewpager.adapter = tabFragmentAdapter
        tabFragmentAdapter.addFragment(LoginFragment())
        tabFragmentAdapter.addTitle(titleList[0])

        tabFragmentAdapter.addFragment(RegisterFragment())
        tabFragmentAdapter.addTitle(titleList[1])
        tabFragmentAdapter.notifyDataSetChanged()

        tabEvent()

    }

    private fun tabEvent() {
        login_text.click {
            if (!isOpen) {
                openTitle()
                app_bar.postDelayed({
                    //先隐藏键盘，后打开title，
                    if (viewpager.currentItem != FRAGMENT_LOGIN) {
                        viewpager.currentItem = FRAGMENT_LOGIN
                    }
                }, 600)
            } else {
                if (viewpager.currentItem != FRAGMENT_LOGIN) {
                    viewpager.currentItem = FRAGMENT_LOGIN
                }
            }
        }

        register_text.click {
            if (!isOpen) {
                openTitle()
                app_bar.postDelayed({
                    //先隐藏键盘，后打开title，
                    if (viewpager.currentItem != FRAGMENT_REGISTER) {
                        viewpager.currentItem = FRAGMENT_REGISTER
                    }
                }, 600)
            } else {
                if (viewpager.currentItem != FRAGMENT_REGISTER) {
                    viewpager.currentItem = FRAGMENT_REGISTER
                }
            }
        }

        //初始化文字大小
        if (viewpager.currentItem == FRAGMENT_LOGIN) {
            login_text.scaleX = 1.2f
            login_text.scaleY = 1.2f
            register_text.scaleX = 1f
            register_text.scaleY = 1f
        } else {
            login_text.scaleX = 1f
            login_text.scaleY = 1f
            register_text.scaleX = 1.2f
            register_text.scaleY = 1.2f
        }
    }


    private fun initListener() {
        iv_close.click { finish() }
        //切换时修改title
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (!isOpen) {
                    openTitle()
                } else {
                    if (position == FRAGMENT_LOGIN) {
                        login_text.scaleX = 1.2f
                        login_text.scaleY = 1.2f
                        register_text.scaleX = 1f
                        register_text.scaleY = 1f
                    } else {
                        login_text.scaleX = 1f
                        login_text.scaleY = 1f
                        register_text.scaleX = 1.2f
                        register_text.scaleY = 1.2f
                    }
                }

            }
        })
        //appbar滑动监听
        val screenW = resources.displayMetrics.widthPixels.toFloat()

        val toolbarHeight = resources.getDimension(R.dimen.toolbar_height)
        val appbarHeight = resources.getDimension(R.dimen.login_appbar_height)

        val statusBarHeight = dimen2px(R.dimen.statusbar_view_height)
        val distance = appbarHeight - toolbarHeight - dimen2px(R.dimen.statusbar_view_height)
        app_bar.addOnOffsetChangedListener { _, verticalOffset ->
            //由于设置了snap 那么最终肯定会恢复为一个为0或者非0的负数 所以这里直接取0来比较，除非手快到松手的瞬间就点击返回键，那么 其实也无所谓 ，这么急切的点击返回那就返回吧。。
            isOpen = verticalOffset == 0
            //获取动画必备的初始参数
            if (mSelfHeight == 0f) {
                mSelfHeight = login_text.height.toFloat()

                //Login页面时的参数
                val loginY = login_text.y - (toolbarHeight + statusBarHeight - login_text.height) / 2.0f
                val loginX = screenW / 2.0f - login_text.x - login_text.width / 2
                val registerX = screenW - register_text.x - register_text.width
                mLoginRegisterScaleY = loginY / (distance)
                mLoginScaleX = loginX / (distance)
                mRegisterScaleX = registerX / (distance)

                //register页面时的参数
                val loginX1 = screenW - login_text.x - login_text.width
                val registerX1 = screenW / 2.0f - register_text.x - register_text.width / 2
                mLoginScaleX1 = loginX1 / (distance)
                mRegisterScaleX1 = registerX1 / (distance)
            }
            //竖线：不透明 ->透明。  文字：x跟y缩放
            val scale = 1.0f + verticalOffset / (distance)

//            L.e("aaron alpha:$scale, verticalOffset:$verticalOffset")
            login_line.alpha = scale
            login_line.translationY = mLoginRegisterScaleY * verticalOffset


            //设置登录、测试TextView的相关动画
            if (viewpager.currentItem == 0) {
                val scaleLogin = 1.2f + 0.3f * verticalOffset / (distance)
                val scaleRegister = 1.0f + 0.4f * verticalOffset / (distance)
                login_text.translationY = mLoginRegisterScaleY * verticalOffset
                login_text.translationX = -mLoginScaleX * verticalOffset
                register_text.translationY = mLoginRegisterScaleY * verticalOffset
                register_text.translationX = -mRegisterScaleX * verticalOffset
                login_text.scaleX = scaleLogin
                login_text.scaleY = scaleLogin
                register_text.scaleX = scaleRegister
                register_text.scaleY = scaleRegister
            } else {
                val scaleLogin = 1.0f + 0.4f * verticalOffset / (distance)
                val scaleRegister = 1.2f + 0.3f * verticalOffset / (distance)
                login_text!!.translationY = mLoginRegisterScaleY * verticalOffset
                login_text.translationX = -mLoginScaleX1 * verticalOffset
                register_text.translationY = mLoginRegisterScaleY * verticalOffset
                register_text.translationX = -mRegisterScaleX1 * verticalOffset
                register_text.scaleX = scaleRegister
                register_text.scaleY = scaleRegister
                login_text.scaleX = scaleLogin
                login_text.scaleY = scaleLogin
            }
        }
    }

    private fun openTitle() {
        app_bar.hideKeyboard()
        app_bar.postDelayed({
            //先隐藏键盘，后打开title，
            app_bar.setExpanded(true, true)
        }, 300)
    }

    private fun closeTitle() {
        app_bar.setExpanded(false, true)
    }

    override fun onEditTextHasFocus() {
        closeTitle()
    }

    override fun onLoginResult() {
//        if (User.currentUser.userId.isNotBlank())
//            StatService.setUserId(this, User.currentUser.userId)
        val intent = Intent()
        intent.putExtra(Config.USER_NAME, User.currentUser.userName)
        intent.putExtra(Config.TEL_PHONE, User.currentUser.telephone)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    // 获取国家码表
    private fun initData() {
//        model.currentCountryByIp()
//                .bindLifecycle(this)
//                .subscribe({
//                    if (it.isOkStatus) {
//                        it.data?.apply {
//                            initZoneCode(this)
//                        }
//                    }
//                }, { it.printStackTrace() })
    }

//    private fun initZoneCode(country: CurrentCountryResponse) {
//
//        model.currentCountryResponse.value = country
//
//        var currentCountry: ListCountriesResponse? = null
//        //如果zoneCode不为空，默认取zoneCode对应值，否则根据ip地址取值。
//        if (User.currentUser.zoneCode.isNotBlank()) {
//            val list = country.countries
//            for (item in list) {
//                if (item.zoneCode == User.currentUser.zoneCode) {
//                    currentCountry = item
//                    break
//                }
//            }
//
//        } else {
//            currentCountry = country.current_country
//        }
//        select_country_text.text = currentCountry?.countryEnglishName
//        model.countryEnglishName.value = currentCountry?.countryEnglishName
//        model.zoneCode.value = currentCountry?.zoneCode
//        model.countryAbbreviation.value = currentCountry?.countryAbbreviation
////        Settings.create().countryAbbreviation = currentCountry?.countryAbbreviation ?: ""
//    }
}

internal interface LoginAndSignInListener {

    /**
     * viewpager中的fragment里面的Edit获取焦点后 调用该方法
     */
    fun onEditTextHasFocus()

    /**
     * 登录成功
     */
    fun onLoginResult()
}