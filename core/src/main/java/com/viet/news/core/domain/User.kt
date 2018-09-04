package com.viet.news.core.domain

import com.viet.news.core.domain.response.LoginRegisterResponse
import com.viet.news.core.utils.RxBus
import java.io.Serializable

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 16/11/2017 2:34 PM
 * @description
 */
class User private constructor() : Serializable {

    var accessToken: String = ""
        set(value) {
            Settings.create().token = value
            field = value
        }
    var userName: String = ""
        set(value) {
            Settings.create().userName = value
            field = value
        }
    var avatarUrl: String = ""
        set(value) {
            Settings.create().avatar = value
            field = value
        }
    var telephone: String = ""
        set(value) {
            Settings.create().telephone = value
            field = value
        }

    var userId: String = ""
        set(value) {
            Settings.create().userId = value
            field = value
        }

    var zoneCode: String = ""
        set(value) {
            Settings.create().zoneCode = value
            field = value
        }

    init {
        if (Settings.create().userName.isNotBlank())
            this.userName = Settings.create().userName
        if (Settings.create().avatar.isNotBlank())
            this.avatarUrl = Settings.create().avatar
        if (Settings.create().token.isNotBlank())
            this.accessToken = Settings.create().token
        if (Settings.create().telephone.isNotBlank())
            this.telephone = Settings.create().telephone
        if (Settings.create().zoneCode.isNotBlank())
            this.zoneCode = Settings.create().zoneCode
        if (Settings.create().userId.isNotBlank())
            this.userId = Settings.create().userId
    }

    companion object {
        var currentUser: User = User()
    }

    fun isLogin(): Boolean = accessToken.isNotBlank()

    fun login(userLogin: LoginRegisterResponse) {

        //init
        this.telephone = userLogin.phoneNo
        this.avatarUrl = userLogin.imageUrl
        this.accessToken = userLogin.token
        this.userName = userLogin.nick
        this.userId = userLogin.walletUserId
        currentUser = this@User
    }

    fun logout() {
        RxBus.get().post(LogoutEvent())
        Settings.create().token = ""
        Settings.create().userName = ""
        Settings.create().avatar = ""
        Settings.create().userId = ""

        currentUser = User()

    }

//    fun setAvatarUrl(url: String) {
//        Settings.create().avatar = url
//        this.avatarUrl = url
//    }

    /**
     * 获取手机尾数
     */
    fun getLast4Telephone() = if (telephone.isEmpty()) ""
    else telephone.substring(telephone.length - 4, telephone.length)
}