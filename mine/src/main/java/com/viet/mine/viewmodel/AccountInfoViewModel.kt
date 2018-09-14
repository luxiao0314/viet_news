package com.viet.mine.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.CountDownTimer
import com.luck.picture.lib.entity.LocalMedia
import com.viet.mine.repository.MineRepository
import com.viet.news.core.config.VerifyCodeTypeEnum
import com.viet.news.core.domain.response.UserInfoResponse
import com.viet.news.core.viewmodel.BaseViewModel

class AccountInfoViewModel(var repository: MineRepository = MineRepository()) : BaseViewModel() {

    companion object {
        var countValue = -1L//倒计时
            set(value) {
                field = if (value == 0L) -1L else value
            }
    }

    var selectList = ArrayList<LocalMedia>()


    var nickName: MutableLiveData<String> = MutableLiveData()    //提交昵称

    var submitEnable: MutableLiveData<Boolean> = MutableLiveData()   //提交按钮是否可用

    //区号
    var zoneCode: MutableLiveData<String> = MutableLiveData()

    //倒计时
    var countDown: MutableLiveData<Int> = MutableLiveData()//注册用的倒计时
    var countDownTimeUnit: CountDownTimer? = null

    init {
        zoneCode.value = "86"
//        //初始化倒计时，若上次倒计时未完成，则继续
        if (FindPwdViewModel.countValue != -1L) {
            countDown.value = (FindPwdViewModel.countValue / 1000).toInt()
            startResetPwdCountdown(FindPwdViewModel.countValue)
        }
    }

    /**
     * 开始倒计时
     */
    fun startResetPwdCountdown(time: Long) {
        if (FindPwdViewModel.countValue == -1L) {
            countDownTimeUnit = object : CountDownTimer(time, 1000) {
                override fun onFinish() {
                    countDown.value = 0
                    FindPwdViewModel.countValue = -1L
                }

                override fun onTick(millisUntilFinished: Long) {
                    countDown.value = (millisUntilFinished / 1000).toInt()
                    FindPwdViewModel.countValue = millisUntilFinished
                }
            }.start()
        }
    }

    //结束【注册验证码】倒计时
    fun stopSignInCountdown() {
        countDownTimeUnit?.cancel()
        countDown.value = 0
        countValue = -1L
    }


    fun checkNickNameSubmitBtnEnable() {
        submitEnable.value = nickName.value != null && nickName.value!!.isNotEmpty()
    }

    fun nickNameSubmitEnable(): Boolean = when {
        nickName.value == null || nickName.value.isNullOrBlank() -> {
            false
        }
        else -> true
    }


    //修改昵称
    fun updateNickName(owner: LifecycleOwner, nickname: String, finish: (isSuccess: Boolean) -> Unit) {
        return repository.updateNickName(nickname).observe(owner, Observer {
            it?.work(
                    onSuccess = { finish(true) },
                    onError = { finish(false) }
            )
        })
    }


    fun uploadFile(owner: LifecycleOwner, finish: (isSuccess: Boolean) -> Unit) {
        return repository.uploadFile(selectList[0].compressPath).observe(owner, Observer {
            it?.work(
                    onSuccess = { finish(true) },
                    onError = { finish(false) }
            )
        })
    }

    fun getUserInfo(userId: String, owner: LifecycleOwner, function: (user: UserInfoResponse?) -> Unit) {
        repository.getUserInfo(userId).observe(owner, Observer {
            it?.work {
                function(it.data)
            }
        })
    }

    fun sendSMS(phoneNumber: String, owner: LifecycleOwner, onSent: () -> Unit) {
        //发送验证码接口
        repository.sendSMS(phoneNumber, zoneCode.value, VerifyCodeTypeEnum.RESET_PASSWORD).observe(owner, Observer { resource ->
            resource?.work(
                    onSuccess = { onSent() },
                    onError = {}
            )
        })
    }

    fun checkVerifyCode(phoneNumber: String, verifyCode: String, owner: LifecycleOwner, onValidate: () -> Unit) {
        //发送验证码接口
        repository.checkVerifyCode(phoneNumber = phoneNumber, verifyCode = verifyCode, zone_code = zoneCode.value, type = VerifyCodeTypeEnum.RESET_PASSWORD)
                .observe(owner, Observer { resource ->
                    resource?.work(
                            onSuccess = { onValidate() }
                    )
                })
    }

    fun resetPwdWithOldPwd(oldPwd: String, newPwd: String, owner: LifecycleOwner, finish: () -> Unit) {
        repository.resetPwdWithOldPwd(oldPwd, newPwd).observe(owner, Observer {
            it?.work {
                finish()
            }
        })
    }

    fun resetPhoneNum(newPhoneNum: String, oldPhoneNum: String, newVrifyCode: String, oldVerifyCode: String, owner: LifecycleOwner, finish: () -> Unit) {
        repository.resetPhoneNum(newPhoneNum, oldPhoneNum, newVrifyCode, oldVerifyCode).observe(owner, Observer {
            it?.work {
                finish()
            }
        })
    }


}