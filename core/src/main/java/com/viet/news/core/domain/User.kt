package com.viet.news.core.domain

import com.facebook.login.LoginManager
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
    var roleId: String = ""
        set(value) {
            Settings.create().roleId = value
            field = value
        }
    var userName: String = ""
        set(value) {
            Settings.create().userName = value
            field = value
        }
    var avatar: String = ""
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

    var fansCount: Int = 0
        set(value) {
            Settings.create().fansCount = value
            field = value
        }
    var followCount: Int = 0
        set(value) {
            Settings.create().followCount = value
            field = value
        }

    init {
        if (Settings.create().userName.isNotBlank())
            this.userName = Settings.create().userName
        if (Settings.create().avatar.isNotBlank())
            this.avatar = Settings.create().avatar
        if (Settings.create().token.isNotBlank())
            this.accessToken = Settings.create().token
        if (Settings.create().roleId.isNotBlank())
            this.roleId = Settings.create().roleId
        if (Settings.create().telephone.isNotBlank())
            this.telephone = Settings.create().telephone
        if (Settings.create().zoneCode.isNotBlank())
            this.zoneCode = Settings.create().zoneCode
        if (Settings.create().userId.isNotBlank())
            this.userId = Settings.create().userId
        if (Settings.create().avatar.isNotBlank())
            this.avatar = Settings.create().avatar

        this.fansCount = Settings.create().fansCount
        this.fansCount = Settings.create().followCount
    }

    companion object {
        var currentUser: User = User()
    }

    fun isLogin(): Boolean = roleId == "1"  //2:设备登录 1:常规登录

    fun isDeviceLogin(): Boolean = roleId == "2"  //2:设备登录 1:常规登录

    fun login(userLogin: LoginRegisterResponse) {
        //init
        this.telephone = userLogin.phoneNumber?:""
        this.avatar = userLogin.avatar?:""
        this.accessToken = userLogin.token?:""
        this.roleId = userLogin.roleId?:""
        this.userName = userLogin.nickName?:""
        this.userId = userLogin.userId?:""
        this.fansCount = userLogin.fansCount
        this.followCount = userLogin.followCount

        currentUser = this@User
    }

    fun logout() {
        RxBus.get().post(LogoutEvent())
        Settings.create().token = ""
        Settings.create().userName = ""
        Settings.create().avatar = ""
        Settings.create().userId = ""
        Settings.create().roleId = ""
        Settings.create().fansCount = 0
        Settings.create().followCount = 0
        LoginManager.getInstance().logOut()
        currentUser = User()

    }

    /**
     * 获取手机尾数
     */
    fun getLast4Telephone() = if (telephone.isEmpty()) ""
    else telephone.substring(telephone.length - 4, telephone.length)
}