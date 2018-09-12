package com.viet.mine.viewmodel

import android.annotation.SuppressLint
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
import com.viet.news.core.config.Config
import com.viet.news.core.config.VerifyCodeTypeEnum
import com.viet.news.core.domain.LoginEvent
import com.viet.news.core.domain.User
import com.viet.news.core.domain.request.RegisterParams
import com.viet.news.core.domain.response.LoginRegisterResponse
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.App
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.utils.RxBus
import com.viet.news.core.viewmodel.BaseViewModel
import com.viet.news.core.vo.Status

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 29/03/2018 17:17
 * @description
 */
class LoginViewModel(var repository: LoginRepository = LoginRepository()) : BaseViewModel() {
    companion object {
        var signInCountValue = -1L//注册用的倒计时
            set(value) {
                field = if (value == 0L) -1L else value
            }
        var loginCountValue = -1L//登录用的倒计时
            set(value) {
                field = if (value == 0L) -1L else value
            }
    }

    val titles = arrayListOf(App.instance.resources.getString(R.string.sign_in), App.instance.resources.getString(R.string.log_in))
    val fragments = arrayListOf<BaseFragment>(RegisterFragment(), LoginFragment())
    val subTitles = mutableListOf(App.instance.resources.getString(R.string.password_to_login), App.instance.resources.getString(R.string.verify_the_login))
    val subFragments = mutableListOf<BaseFragment>(PwdToLoginFragment(), VerifyToLoginFragment())
    var currentTab = 0

    //Pwd To Login tab
    var password: MutableLiveData<String> = MutableLiveData()   //登录密码
    var loginVCode: MutableLiveData<String> = MutableLiveData()  //登录验证码
    var phoneNumber: MutableLiveData<String> = MutableLiveData()    //登录手机号
    var loginEnable: MutableLiveData<Boolean> = MutableLiveData()   //登录按钮是否可用

    //sign in tab / verify To Login tab
    var registerPhoneNumber: MutableLiveData<String> = MutableLiveData()    //注册账号
    var registerVCode: MutableLiveData<String> = MutableLiveData()  //注册验证码
    var registerInviteCode: MutableLiveData<String> = MutableLiveData()  //注册邀请码
    var registerButtonEnable: MutableLiveData<Boolean> = MutableLiveData()   //注册按钮是否可用
    var registerNextButtonEnable: MutableLiveData<Boolean> = MutableLiveData()   //注册下一步按钮是否可用
    var registerVCodeEnable: MutableLiveData<Boolean> = MutableLiveData()   //注册验证码按钮是否可用

    //sign in next
    var registerPwd: MutableLiveData<String> = MutableLiveData()    //注册密码输入
    var registerConfirmPwd: MutableLiveData<String> = MutableLiveData() //注册密码确认
    var registerBtnEnable: MutableLiveData<Boolean> = MutableLiveData() //注册按钮是否可用

    var statusMsg: MutableLiveData<Int> = MutableLiveData()
    var zoneCode: MutableLiveData<String> = MutableLiveData()
    //倒计时
    var signInCountDown: MutableLiveData<Int> = MutableLiveData()//注册用的倒计时
    var loginCountDown: MutableLiveData<Int> = MutableLiveData()//登录用的倒计时
    var loginCountDownTimeUnit: CountDownTimer? = null
    var signInCountDownTimeUnit: CountDownTimer? = null

    init {
        zoneCode.value = "86"
        if (signInCountValue != -1L) {
            signInCountDown.value = (signInCountValue / 1000).toInt()
            startSignInCountdown(signInCountValue)
        }
        if (loginCountValue != -1L) {
            loginCountDown.value = (loginCountValue / 1000).toInt()
            startLoginCountdown(loginCountValue)
        }

    }

    fun startSignInCountdown(time: Long) {
        if (signInCountValue == -1L) {
            signInCountDownTimeUnit = object : CountDownTimer(time, 1000) {
                override fun onFinish() {
                    signInCountDown.value = 0
                    signInCountValue = -1L
                }

                override fun onTick(millisUntilFinished: Long) {
                    signInCountDown.value = (millisUntilFinished / 1000).toInt()
                    signInCountValue = millisUntilFinished
                }
            }.start()
        }
    }

    fun startLoginCountdown(time: Long) {
        if (loginCountValue == -1L) {
            loginCountDownTimeUnit = object : CountDownTimer(time, 1000) {
                override fun onFinish() {
                    loginCountDown.value = 0
                    loginCountValue = -1L
                }

                override fun onTick(millisUntilFinished: Long) {
                    loginCountDown.value = (millisUntilFinished / 1000).toInt()
                    loginCountValue = millisUntilFinished
                }
            }.start()
        }
    }

    //结束倒计时
    fun stopLoginCountdown() {
        loginCountDownTimeUnit?.cancel()
        loginCountDown.value = 0
        loginCountValue = -1L
    }

    //结束倒计时
    fun stopSignInCountdown() {
        signInCountDownTimeUnit?.cancel()
        signInCountDown.value = 0
        signInCountValue = -1L
    }


    //检查注册 发送验证码按钮是否可点击
    fun checkRegisterVCodeBtnEnable() {
        registerVCodeEnable.value = !registerPhoneNumber.value.isNullOrEmpty()
    }

    //检查注册 下一步按钮是否可用
    fun checkRegisterNextBtnEnable() {
        registerNextButtonEnable.value = !registerPhoneNumber.value.isNullOrEmpty()
                && !registerVCode.value.isNullOrEmpty()
    }

    fun checkRegisterBtnEnable() {
        registerBtnEnable.value = !registerPwd.value.isNullOrEmpty()
                && !registerConfirmPwd.value.isNullOrEmpty()
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

    //--------------------以下为接口调用相关-----------------------------------------------------


    @SuppressLint("CheckResult")
    fun login(owner: LifecycleOwner, func: () -> Unit) {
        repository.login(phoneNumber.value, password.value).observe(owner, Observer { resource ->
            resource?.apply {
                if (status == Status.SUCCESS) {
                    if (data != null) {
                        phoneNumber.value?.let { phoneNumber -> User.currentUser.telephone = phoneNumber }
                        zoneCode.value?.let { zoneCode -> User.currentUser.zoneCode = zoneCode }
                        User.currentUser.login(data!!.data!!)
                        stopLoginCountdown()//登录成功后结束本次倒计时
                        RxBus.get().post(LoginEvent())
                        func()
                    }
                } else {
                    message?.let { msg ->
                        toast(msg).show()
                    }
                }
            }
        })
    }

    fun sendSMS(owner: LifecycleOwner, type: VerifyCodeTypeEnum, onSent: () -> Unit) {
        var phone: String? = null
        when (type) {
        //开始倒计时
            VerifyCodeTypeEnum.LOGIN -> {
                startLoginCountdown(Config.COUNT_DOWN_TIMER)
                phone = phoneNumber.value
            }
            VerifyCodeTypeEnum.REGISTER -> {
                startSignInCountdown(Config.COUNT_DOWN_TIMER)
                phone = registerPhoneNumber.value
            }
            else -> {
            }
        }
        //发送验证码接口
        repository.sendSMS(phone, zoneCode.value, type).observe(owner, Observer { resource ->
            resource?.apply {
                if (status == Status.SUCCESS) {
                    onSent()
                } else {
                    message?.let { msg ->
                        toast(msg).show()
                        //TODO tsing 放这里不对，要放到请求失败的地方。暂时注释掉
//                        when (type) {
//                        //发送验证码失败，结束倒计时
//                            VerifyCodeTypeEnum.LOGIN -> stopLoginCountdown()
//                            VerifyCodeTypeEnum.REGISTER -> stopSignInCountdown()
//                            else -> {
//                            }
//                        }
                    }
                }
            }
        })
    }


    fun checkVerifyCode(owner: LifecycleOwner, type: VerifyCodeTypeEnum, onValidate: () -> Unit) {
        var phone: String? = null
        var verifyCode: String? = null
        when (type) {
        //开始倒计时
            VerifyCodeTypeEnum.LOGIN -> {
                phone = phoneNumber.value
                verifyCode = loginVCode.value
            }
            VerifyCodeTypeEnum.REGISTER -> {
                phone = registerPhoneNumber.value
                verifyCode = registerVCode.value
            }
            else -> {
            }
        }
        //发送验证码接口
        repository.checkVerifyCode(phoneNumber = phone, verifyCode = verifyCode, zone_code = zoneCode.value, type = type).observe(owner, Observer { resource ->
            resource?.apply {
                if (status == Status.SUCCESS) {
                    onValidate()
                } else {
                    message?.let { msg ->
                        toast(msg).show()
                    }
                }
            }
        })
    }

    fun signIn(owner: LifecycleOwner, onSignInSuccess: (data: LoginRegisterResponse.LoginRegister?) -> Unit) {
        val params = RegisterParams()
        params.phone_number=registerPhoneNumber.value
        params.password=registerConfirmPwd.value
        params.verify_code=registerVCode.value
        params.invite_code=registerInviteCode.value
        repository.signIn(params).observe(owner, Observer { resource ->
            resource?.apply {
                if (status == Status.SUCCESS) {
                    onSignInSuccess(data?.data)
                } else {
                    message?.let { msg ->
                        toast(msg).show()
                    }
                }
            }
        })
    }


}