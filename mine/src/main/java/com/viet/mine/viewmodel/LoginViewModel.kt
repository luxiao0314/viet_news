package com.viet.mine.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.CountDownTimer
import com.safframework.ext.then
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
import com.viet.news.core.domain.request.SignInParams
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
class LoginViewModel(private var repository: LoginRepository = LoginRepository()) : BaseViewModel() {
    companion object {
        var countValue4SignIn = -1L//注册用的倒计时
            set(value) {
                field = if (value == 0L) -1L else value
            }
        var countValue4Login = -1L//登录用的倒计时
            set(value) {
                field = if (value == 0L) -1L else value
            }
    }

    val titles = arrayListOf(App.instance.resources.getString(R.string.sign_in), App.instance.resources.getString(R.string.log_in))
    val fragments = arrayListOf<BaseFragment>(RegisterFragment(), LoginFragment())
    val subTitles = mutableListOf(App.instance.resources.getString(R.string.password_to_login), App.instance.resources.getString(R.string.verify_the_login))
    val subFragments = mutableListOf<BaseFragment>(PwdToLoginFragment(), VerifyToLoginFragment())
    var currentTab = 0

    //错误信息
    var statusMsg: MutableLiveData<Int> = MutableLiveData()
    //区号
    var zoneCode: MutableLiveData<String> = MutableLiveData()

    //密码登录
    var pwdLoginPhoneNumber: MutableLiveData<String> = MutableLiveData()    //密码登录 手机号
    var pwdLoginPassword: MutableLiveData<String> = MutableLiveData()   //登录密码
    //密码登录 相关按钮是否可用
    var pwdLoginButtonEnable: MutableLiveData<Boolean> = MutableLiveData()   //密码登录按钮是否可用

    //验证码登录
    var vCodeLoginPhoneNumber: MutableLiveData<String> = MutableLiveData()    //验证码登录手机号
    var vCodeLoginVCode: MutableLiveData<String> = MutableLiveData()  //登录验证码
    //验证码登录 相关按钮是否可用
    var vCodeLoginSendVCodeButtonEnable: MutableLiveData<Boolean> = MutableLiveData()   //验证码登录按钮是否可用
    var vCodeLoginButtonEnable: MutableLiveData<Boolean> = MutableLiveData()   //验证码登录按钮是否可用

    //注册
    var signInPhoneNumber: MutableLiveData<String> = MutableLiveData()    //注册账号
    var signInVCode: MutableLiveData<String> = MutableLiveData()  //注册验证码
    var signInInviteCode: MutableLiveData<String> = MutableLiveData()  //注册邀请码
    var signInPwd: MutableLiveData<String> = MutableLiveData()    //注册密码输入
    var signInConfirmPwd: MutableLiveData<String> = MutableLiveData() //注册密码确认
    //注册 相关按钮是否可用
    var signInSendVCodeEnable: MutableLiveData<Boolean> = MutableLiveData()   //注册 发送验证码按钮是否可用
    var signInNextButtonEnable: MutableLiveData<Boolean> = MutableLiveData()   //注册下一步按钮是否可用
    var signInButtonEnable: MutableLiveData<Boolean> = MutableLiveData() //注册按钮是否可用

    //倒计时
    var countDown4SignIn: MutableLiveData<Int> = MutableLiveData()//注册用的倒计时
    var countDown4Login: MutableLiveData<Int> = MutableLiveData()//登录用的倒计时
    var countDownTimeUnit4SignIn: CountDownTimer? = null
    var countDownTimeUnit4Login: CountDownTimer? = null

    init {
        //默认区号86
        zoneCode.value = "86"
        //初始化倒计时，若上次倒计时未完成，则继续
        if (countValue4SignIn != -1L) {
            countDown4SignIn.value = (countValue4SignIn / 1000).toInt()
            startSignInCountdown(countValue4SignIn)
        }
        if (countValue4Login != -1L) {
            countDown4Login.value = (countValue4Login / 1000).toInt()
            startLoginCountdown(countValue4Login)
        }

    }

    /**
     * 开始注册验证码倒计时
     */
    private fun startSignInCountdown(time: Long) {
        if (countValue4SignIn == -1L) {
            countDownTimeUnit4SignIn = object : CountDownTimer(time, 1000) {
                override fun onFinish() {
                    countDown4SignIn.value = 0
                    countValue4SignIn = -1L
                }

                override fun onTick(millisUntilFinished: Long) {
                    countDown4SignIn.value = (millisUntilFinished / 1000).toInt()
                    countValue4SignIn = millisUntilFinished
                }
            }.start()
        }
    }

    /**
     * 开始登录验证码倒计时
     */
    private fun startLoginCountdown(time: Long) {
        if (countValue4Login == -1L) {
            countDownTimeUnit4Login = object : CountDownTimer(time, 1000) {
                override fun onFinish() {
                    countDown4Login.value = 0
                    countValue4Login = -1L
                }

                override fun onTick(millisUntilFinished: Long) {
                    countDown4Login.value = (millisUntilFinished / 1000).toInt()
                    countValue4Login = millisUntilFinished
                }
            }.start()
        }
    }

    //结束【登录验证码】倒计时
    private fun stopLoginCountdown() {
        countDownTimeUnit4Login?.cancel()
        countDown4Login.value = 0
        countValue4Login = -1L
    }

    //结束【注册验证码】倒计时
    private fun stopSignInCountdown() {
        countDownTimeUnit4SignIn?.cancel()
        countDown4SignIn.value = 0
        countValue4SignIn = -1L
    }


    /* --------------------以下为输入框文字变化监听相关-----------------------------------------------------
     */

    //检查【注册发送验证码】按钮是否可点击
    fun checkSignInSendVCodeEnable() {
        signInSendVCodeEnable.value = !signInPhoneNumber.value.isNullOrEmpty()
    }

    //检查【注册下一步】按钮是否可用
    fun checkSignInNextButtonEnable() {
        signInNextButtonEnable.value = !signInPhoneNumber.value.isNullOrEmpty()
                && !signInVCode.value.isNullOrEmpty()
    }

    //检查【注册按钮】是否可用
    fun checkSignInButtonEnable() {
        signInButtonEnable.value = !signInPwd.value.isNullOrEmpty()
                && !signInConfirmPwd.value.isNullOrEmpty()
    }

    //检查【登录发送验证码】按钮是否可用
    fun checkVCodeLoginSendVCodeButtonEnable() {
        vCodeLoginSendVCodeButtonEnable.value = !vCodeLoginPhoneNumber.value.isNullOrEmpty()
    }

    //检查【验证码登录】按钮是否可用
    fun checkVCodeLoginButtonEnable() {
        vCodeLoginButtonEnable.value = !vCodeLoginPhoneNumber.value.isNullOrEmpty()
                && !vCodeLoginVCode.value.isNullOrEmpty()
    }

    //检查【密码登录】按钮是否可用
    fun checkPwdLoginButtonEnable() {
        pwdLoginButtonEnable.value = !pwdLoginPhoneNumber.value.isNullOrEmpty()
                && !pwdLoginPassword.value.isNullOrEmpty()
    }


    /* --------------------以下为按钮点击时检查相关-----------------------------------------------------
     * 一般用于判断手机号\密码长度等异常信息。
     * TODO tsing 根据需求修改，例如密码长度最少6位最大xx位。暂不处理，都返回的true，使用实例如下第一个，注释部分
     */
    fun canSendLoginVCode(): Boolean = when {
    //检查手机号码长度，格式，区号 等信息。
//        signInerVCodeEnable.value!!.not() -> {
//            statusMsg.value = R.string.verify_the_login
//            false
//        }
        else -> true
    }

    fun canVCodeLogin(): Boolean = true

    fun canPwdLogin(): Boolean = true
    fun canSendSignInVCode(): Boolean = true
    fun canNextSign(): Boolean = true
    fun canSign(): Boolean = true


    //--------------------以下为接口调用相关-----------------------------------------------------


    fun sendSMS(owner: LifecycleOwner, type: VerifyCodeTypeEnum, onSent: () -> Unit) {
        var phone: String? = null
        when (type) {
        //开始倒计时
            VerifyCodeTypeEnum.LOGIN -> {
                startLoginCountdown(Config.COUNT_DOWN_TIMER)
                phone = vCodeLoginPhoneNumber.value
            }
            VerifyCodeTypeEnum.REGISTER -> {
                startSignInCountdown(Config.COUNT_DOWN_TIMER)
                phone = signInPhoneNumber.value
            }
            else -> {
            }
        }
        //发送验证码接口
        repository.sendSMS(phone, zoneCode.value, type).observe(owner, Observer { resource ->
            resource?.apply {
                if (status == Status.SUCCESS) {
                    data?.isOkStatus?.then(
                            { onSent() },
                            {
                                //发送验证码失败，结束倒计时
                                when (type) {
                                    VerifyCodeTypeEnum.LOGIN -> stopLoginCountdown()
                                    VerifyCodeTypeEnum.REGISTER -> stopSignInCountdown()
                                    else -> {
                                    }
                                }
                            })


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
                phone = pwdLoginPhoneNumber.value
                verifyCode = vCodeLoginVCode.value
            }
            VerifyCodeTypeEnum.REGISTER -> {
                phone = signInPhoneNumber.value
                verifyCode = signInVCode.value
            }
            else -> {
            }
        }
        //发送验证码接口
        repository.checkVerifyCode(phoneNumber = phone, verifyCode = verifyCode, zone_code = zoneCode.value, type = type).observe(owner, Observer { resource ->
            resource?.apply {
                if (status == Status.SUCCESS) {
                    data?.isOkStatus?.then({ onValidate() }, {})
                }
            }
        })
    }

    fun signIn(owner: LifecycleOwner, onSignInSuccess: () -> Unit) {
        val params = SignInParams()
        params.phone_number = signInPhoneNumber.value
        params.password = signInConfirmPwd.value
        params.verify_code = signInVCode.value
        params.invite_code = signInInviteCode.value
        repository.signIn(params).observe(owner, Observer { resource ->
            resource?.apply {
                if (status == Status.SUCCESS) {
                    data?.isOkStatus?.then(
                            {
                                data?.data?.let {
                                    signInPhoneNumber.value?.let { phoneNumber -> User.currentUser.telephone = phoneNumber }
                                    zoneCode.value?.let { zoneCode -> User.currentUser.zoneCode = zoneCode }
                                    User.currentUser.login(it)
                                    stopSignInCountdown()//注册成功后结束本次倒计时
                                    RxBus.get().post(LoginEvent())
                                    onSignInSuccess()
                                }
                            },
                            {})
                }
            }
        })
    }

    @SuppressLint("CheckResult")
    fun loginByPwd(owner: LifecycleOwner, onLoginSuccess: () -> Unit) {
        repository.loginByPwd(pwdLoginPhoneNumber.value, pwdLoginPassword.value).observe(owner, Observer { resource ->
            resource?.apply {
                if (status == Status.SUCCESS) {
                    data?.isOkStatus?.then(
                            {
                                data?.data?.let {
                                    signInPhoneNumber.value?.let { phoneNumber -> User.currentUser.telephone = phoneNumber }
                                    zoneCode.value?.let { zoneCode -> User.currentUser.zoneCode = zoneCode }
                                    User.currentUser.login(it)
                                    stopSignInCountdown()//注册成功后结束本次倒计时
                                    RxBus.get().post(LoginEvent())
                                    onLoginSuccess()
                                }
                            },
                            {})
                }
            }
        })
    }

    @SuppressLint("CheckResult")
    fun loginBySMS(owner: LifecycleOwner, onLoginSuccess: () -> Unit) {
        repository.loginBySMS(vCodeLoginPhoneNumber.value, vCodeLoginVCode.value).observe(owner, Observer { resource ->
            resource?.apply {
                if (status == Status.SUCCESS) {
                    data?.isOkStatus?.then(
                            {
                                data?.data?.let {
                                    signInPhoneNumber.value?.let { phoneNumber -> User.currentUser.telephone = phoneNumber }
                                    zoneCode.value?.let { zoneCode -> User.currentUser.zoneCode = zoneCode }
                                    User.currentUser.login(it)
                                    stopSignInCountdown()//注册成功后结束本次倒计时
                                    RxBus.get().post(LoginEvent())
                                    onLoginSuccess()
                                }
                            },
                            {})
                }
            }
        })
    }

}