package com.viet.mine.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.CountDownTimer
import com.safframework.ext.then
import com.viet.mine.R
import com.viet.mine.repository.LoginRepository
import com.viet.news.core.config.Config
import com.viet.news.core.config.VerifyCodeTypeEnum
import com.viet.news.core.domain.LoginEvent
import com.viet.news.core.domain.User
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.App
import com.viet.news.core.utils.RxBus
import com.viet.news.core.viewmodel.BaseViewModel

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 29/03/2018 17:17
 * @description
 */
class FindPwdViewModel(private var repository: LoginRepository = LoginRepository()) : BaseViewModel() {
    companion object {
        var countValue = -1L//倒计时
            set(value) {
                field = if (value == 0L) -1L else value
            }
    }


    //错误信息
    var statusMsg: MutableLiveData<Int> = MutableLiveData()
    //区号
    var zoneCode: MutableLiveData<String> = MutableLiveData()

    var phoneNumber: MutableLiveData<String> = MutableLiveData()
    var vCode: MutableLiveData<String> = MutableLiveData()
    var password1: MutableLiveData<String> = MutableLiveData()
    var password2: MutableLiveData<String> = MutableLiveData()

    var vCodeButtonEnable: MutableLiveData<Boolean> = MutableLiveData()
    var nextButtonEnable: MutableLiveData<Boolean> = MutableLiveData()
    var resetButtonEnable: MutableLiveData<Boolean> = MutableLiveData()

    //倒计时
    var countDown: MutableLiveData<Int> = MutableLiveData()//注册用的倒计时
    var countDownTimeUnit: CountDownTimer? = null

    init {
        //默认区号86
        zoneCode.value = "86"
        //初始化倒计时，若上次倒计时未完成，则继续
        if (countValue != -1L) {
            countDown.value = (countValue / 1000).toInt()
            startSignInCountdown(countValue)
        }
    }

    /**
     * 开始注册验证码倒计时
     */
    private fun startSignInCountdown(time: Long) {
        if (countValue == -1L) {
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
    }


    //结束【注册验证码】倒计时
    private fun stopSignInCountdown() {
        countDownTimeUnit?.cancel()
        countDown.value = 0
        countValue = -1L
    }


    /* --------------------以下为输入框文字变化监听相关-----------------------------------------------------
     */

    fun checkNextButtonEnable() {
        nextButtonEnable.value = !phoneNumber.value.isNullOrEmpty()
    }

    fun checkResetButtonEnable() {
        resetButtonEnable.value = !password1.value.isNullOrEmpty() && !password2.value.isNullOrEmpty()
    }


    /* --------------------以下为按钮点击时检查相关-----------------------------------------------------
     * 一般用于判断手机号\密码长度等异常信息。
     * TODO tsing 根据需求修改，例如密码长度最少6位最大xx位。暂不处理，都返回的true，使用实例如下第一个，注释部分
     */
    fun canSendVCode(): Boolean = true

    fun canNext(): Boolean = true

    fun canSetPwd(): Boolean = true


    //--------------------以下为接口调用相关-----------------------------------------------------


    @SuppressLint("CheckResult")
    fun setPasswordThenLogin(owner: LifecycleOwner, onSetPwdSuccess: () -> Unit) {
        repository.setPassword(phoneNumber = phoneNumber.value, verifyCode = vCode.value, password = password1.value).observe(owner, Observer { resource ->
            resource?.apply {
                data?.isOkStatus?.then({
                    data?.data?.let {
                        phoneNumber.value?.let { phoneNumber -> User.currentUser.telephone = phoneNumber }
                        zoneCode.value?.let { zoneCode -> User.currentUser.zoneCode = zoneCode }
                        User.currentUser.login(it)
                        stopSignInCountdown()//注册成功后结束本次倒计时
                        RxBus.get().post(LoginEvent())
                        onSetPwdSuccess()
                    }
                }, {
                    toast(App.instance.resources.getString(R.string.error_msg)).show()
                })
            }
        })
    }

    fun sendSMS(owner: LifecycleOwner, onSent: () -> Unit) {
        startSignInCountdown(Config.COUNT_DOWN_TIMER)
        //发送验证码接口
        repository.sendSMS(phoneNumber.value, zoneCode.value, VerifyCodeTypeEnum.RESET_PASSWORD).observe(owner, Observer { resource ->
            resource?.apply {
                data?.isOkStatus?.then(
                        { onSent() },
                        {
                            //发送验证码失败，结束倒计时
                            stopSignInCountdown()
                        })
            }
        })
    }


    fun checkVerifyCode(owner: LifecycleOwner, onValidate: () -> Unit) {
        //发送验证码接口
        repository.checkVerifyCode(phoneNumber = phoneNumber.value, verifyCode = vCode.value, zone_code = zoneCode.value, type = VerifyCodeTypeEnum.RESET_PASSWORD).observe(owner, Observer { resource ->
            resource?.apply {
                data?.isOkStatus?.then({ onValidate() }, { toast(App.instance.resources.getString(R.string.error_msg)).show() })
            }
        })
    }

}