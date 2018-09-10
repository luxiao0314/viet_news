package com.viet.mine.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.CountDownTimer
import com.viet.mine.R
import com.viet.mine.fragment.LoginFragment
import com.viet.mine.fragment.PwdToLoginFragment
import com.viet.mine.fragment.RegisterFragment
import com.viet.mine.fragment.VerifyToLoginFragment
import com.viet.mine.repository.LoginRepository
import com.viet.mine.viewmodel.LoginViewModel.StaticFiled.countValue
import com.viet.news.core.domain.User
import com.viet.news.core.ui.App
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.viewmodel.BaseViewModel

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 29/03/2018 17:17
 * @description
 */
class LoginViewModel(var repository: LoginRepository = LoginRepository()) : BaseViewModel() {

    val titles = arrayListOf(App.instance.resources.getString(R.string.sign_in), App.instance.resources.getString(R.string.log_in))
    val fragments = arrayListOf<BaseFragment>(RegisterFragment(), LoginFragment())
    val subTitles = mutableListOf(App.instance.resources.getString(R.string.password_to_login), App.instance.resources.getString(R.string.verify_the_login))
    val subFragments = mutableListOf<BaseFragment>(PwdToLoginFragment(), VerifyToLoginFragment())
    var currentTab = 0

    //Pwd To Login tab
    var password: MutableLiveData<String> = MutableLiveData()   //登录密码
    var phoneNumber: MutableLiveData<String> = MutableLiveData()    //登录手机号
    var loginEnable: MutableLiveData<Boolean> = MutableLiveData()   //登录按钮是否可用

    //sign in tab / verify To Login tab
    var registerPhoneNumber: MutableLiveData<String> = MutableLiveData()    //注册账号
    var registerVCode: MutableLiveData<String> = MutableLiveData()  //注册验证码
    var registerVCodeEnable: MutableLiveData<Boolean> = MutableLiveData()   //注册下一步按钮是否可用

    //sign in next
    var registerPwd: MutableLiveData<String> = MutableLiveData()    //注册密码输入
    var registerConfirmPwd: MutableLiveData<String> = MutableLiveData() //注册密码确认
    var registerBtnEnable: MutableLiveData<Boolean> = MutableLiveData() //注册按钮是否可用

    var countDown: MutableLiveData<Int> = MutableLiveData()
    var statusMsg: MutableLiveData<Int> = MutableLiveData()
    var zoneCode: MutableLiveData<String> = MutableLiveData()
    var countDownTimeUnit: CountDownTimer? = null

    init {
        if (countValue != -1L) {
            countDown.value = (countValue / 1000).toInt()
            startCountdown(countValue)
        }
    }

    fun startCountdown(time: Long) {
        countDownTimeUnit = object : CountDownTimer(time, 1000) {
            override fun onFinish() {
                countDown.value = 0
                countValue = -1L
            }

            override fun onTick(millisUntilFinished: Long) {
                countDown.value = (millisUntilFinished / 1000).toInt()
                countValue = millisUntilFinished
            }
        }.start()
    }

    //结束倒计时
    fun stopCountdown() {
        countDownTimeUnit?.cancel()
        countDown.value = 0
        countValue = -1L
    }

    internal object StaticFiled {
        var countValue = -1L
            set(value) {
                field = if (value == 0L) -1L else value
            }
    }

    fun checkRegisterVCodeBtnEnable() {
        registerVCodeEnable.value = (registerPhoneNumber.value != null && registerPhoneNumber.value!!.isNotEmpty()
                && registerVCode.value != null && registerVCode.value!!.isNotEmpty())
    }

    fun checkRegisterBtnEnable() {
        registerBtnEnable.value = (registerPwd.value != null && registerPwd.value!!.isNotEmpty()
                && registerConfirmPwd.value == registerPwd.value)
    }

    fun checkLoginBtnEnable() {
        loginEnable.value = (phoneNumber.value != null && phoneNumber.value!!.isNotEmpty()
                && password.value != null && password.value!!.isNotEmpty())
    }

    fun nextBtnEnable(): Boolean = when {
        registerVCodeEnable.value!!.not() -> {
            statusMsg.value = R.string.verify_the_login
            false
        }
        else -> true
    }

    fun registerBtnEnable(): Boolean = when {
        registerBtnEnable.value!!.not() -> {
            statusMsg.value = R.string.verify_the_login
            false
        }
        else -> true
    }

    fun loginEnable(): Boolean = when {
//        zoneCode.value == null || zoneCode.value.isNullOrBlank() -> {
//            statusMsg.value = R.string.verify_the_login
//            false
//        }
        phoneNumber.value == null || phoneNumber.value.isNullOrBlank() -> {
            statusMsg.value = R.string.verify_the_login
            false
        }
        password.value == null || password.value.isNullOrBlank() -> {
            statusMsg.value = R.string.verify_the_login
            false
        }
        else -> true
    }

    fun login(owner: LifecycleOwner) {
        repository.login(phoneNumber.value, password.value).observe(owner, Observer {
            if (it?.data != null) {
                phoneNumber.value?.let { phoneNumber -> User.currentUser.telephone = phoneNumber }
                zoneCode.value?.let { zoneCode -> User.currentUser.zoneCode = zoneCode }
                User.currentUser.login(it.data!!)
                stopCountdown()//登录成功后结束本次倒计时
            }
        })
    }
}