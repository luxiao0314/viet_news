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

        var setNewPhoneCountValue = -1L//倒计时
            set(value) {
                field = if (value == 0L) -1L else value
            }
    }

    var selectList = ArrayList<LocalMedia>()


    var nickName: MutableLiveData<String> = MutableLiveData()    //提交昵称
    var oldPwd: MutableLiveData<String> = MutableLiveData()
    var newPwd: MutableLiveData<String> = MutableLiveData()
    var confirmPwd: MutableLiveData<String> = MutableLiveData()

    var setNewPwd: MutableLiveData<String> = MutableLiveData()
    var confirmNewPwd: MutableLiveData<String> = MutableLiveData()

    var submitEnable: MutableLiveData<Boolean> = MutableLiveData()   //提交按钮是否可用
    var resetEnable: MutableLiveData<Boolean> = MutableLiveData()
    var setupEnable: MutableLiveData<Boolean> = MutableLiveData()

    //区号
    var zoneCode: MutableLiveData<String> = MutableLiveData()

    //倒计时
    var changePhoneCountDown: MutableLiveData<Int> = MutableLiveData()//更换手机号用的倒计时
    var changePhoneCountDownTimeUnit: CountDownTimer? = null

    var setNewPhoneCountDown: MutableLiveData<Int> = MutableLiveData()//更换手机号用的倒计时
    var setNewPhoneCountDownTimeUnit: CountDownTimer? = null

    init {
        zoneCode.value = "86"
//        //初始化倒计时，若上次倒计时未完成，则继续
        if (AccountInfoViewModel.countValue != -1L) {
            changePhoneCountDown.value = (AccountInfoViewModel.countValue / 1000).toInt()
            startChangePhoneCountdown(AccountInfoViewModel.countValue)
        }

        if (AccountInfoViewModel.setNewPhoneCountValue != -1L) {
            changePhoneCountDown.value = (AccountInfoViewModel.setNewPhoneCountValue / 1000).toInt()
            startSetNewPhoneCountdown(AccountInfoViewModel.setNewPhoneCountValue)
        }
    }

    /**
     * 开始倒计时
     */
    fun startChangePhoneCountdown(time: Long) {
        if (AccountInfoViewModel.countValue == -1L) {
            changePhoneCountDownTimeUnit = object : CountDownTimer(time, 1000) {
                override fun onFinish() {
                    changePhoneCountDown.value = 0
                    FindPwdViewModel.countValue = -1L
                }

                override fun onTick(millisUntilFinished: Long) {
                    changePhoneCountDown.value = (millisUntilFinished / 1000).toInt()
                    FindPwdViewModel.countValue = millisUntilFinished
                }
            }.start()
        }
    }

    //结束【注册验证码】倒计时
    fun stopChangePhoneCountdown() {
        changePhoneCountDownTimeUnit?.cancel()
        changePhoneCountDown.value = 0
        countValue = -1L
    }

    /**
     * 开始倒计时
     */
    fun startSetNewPhoneCountdown(time: Long) {
        if (AccountInfoViewModel.setNewPhoneCountValue == -1L) {
            setNewPhoneCountDownTimeUnit = object : CountDownTimer(time, 1000) {
                override fun onFinish() {
                    setNewPhoneCountDown.value = 0
                    AccountInfoViewModel.countValue = -1L
                }

                override fun onTick(millisUntilFinished: Long) {
                    setNewPhoneCountDown.value = (millisUntilFinished / 1000).toInt()
                    AccountInfoViewModel.countValue = millisUntilFinished
                }
            }.start()
        }
    }

    //结束【设置新手机验证码】倒计时
    fun stopSetNewPhoneCountdown() {
        setNewPhoneCountDownTimeUnit?.cancel()
        setNewPhoneCountDown.value = 0
        setNewPhoneCountValue = -1L
    }


    /**************************************以下是状态判断****************************************/
    fun checkNickNameSubmitBtnEnable() {
        submitEnable.value = !nickName.value.isNullOrEmpty()
    }

    fun nickNameSubmitEnable(): Boolean = when {
        nickName.value.isNullOrEmpty() -> {
            false
        }
        else -> true
    }

    fun checkResetSubmitBtnEnable() {
        resetEnable.value = !oldPwd.value.isNullOrEmpty() && !newPwd.value.isNullOrEmpty() && !confirmPwd.value.isNullOrEmpty()
    }

    fun reSetSubmitEnable(): Boolean = when {
        oldPwd.value.isNullOrEmpty() || newPwd.value.isNullOrEmpty() || confirmPwd.value.isNullOrEmpty() -> {
            false
        }
        else -> true
    }

    fun checkSetupSubmitBtnEnable() {
        setupEnable.value = !setNewPwd.value.isNullOrEmpty() && !confirmNewPwd.value.isNullOrEmpty()
    }

    fun setupSubmitEnable(): Boolean = when {
        setNewPwd.value.isNullOrEmpty() || confirmNewPwd.value.isNullOrEmpty() -> {
            false
        }
        else -> true
    }


    /**************************************以下是网络请求****************************************/

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
        repository.sendSMS(phoneNumber, zoneCode.value, VerifyCodeTypeEnum.BIND_PHONE).observe(owner, Observer { resource ->
            resource?.work(
                    onSuccess = { onSent() },
                    onLoading = { true }
            )
        })
    }

    fun checkVerifyCode(phoneNumber: String, verifyCode: String, owner: LifecycleOwner, onValidate: () -> Unit) {
        //发送验证码接口
        repository.checkVerifyCode(phoneNumber = phoneNumber, verifyCode = verifyCode, zone_code = zoneCode.value, type = VerifyCodeTypeEnum.BIND_PHONE)
                .observe(owner, Observer { resource ->
                    resource?.work(
                            onSuccess = { onValidate() },
                            onLoading = { true }
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

    fun resetPhoneNum(newPhoneNum: String, oldPhoneNum: String, newVerifyCode: String, oldVerifyCode: String, owner: LifecycleOwner, finish: () -> Unit) {
        repository.resetPhoneNum(newPhoneNum, oldPhoneNum, newVerifyCode, oldVerifyCode).observe(owner, Observer {
            it?.work {
                finish()
            }
        })
    }

    fun setPassword(phoneNumber: String?, verifyCode: String?, password: String?, owner: LifecycleOwner, finish: () -> Unit) {
        repository.setPassword(phoneNumber, verifyCode, password).observe(owner, Observer {
            it?.work {
                finish()
            }
        })
    }

}