package com.library.aaron.core.domain

import com.library.aaron.core.domain.response.LoginResponse
import java.io.Serializable

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 16/11/2017 2:34 PM
 * @description
 */
class User private constructor() : Serializable {

    var access_token: String = ""
    //todo: mock
//    var access_token: String = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTIzMjQ0ODgzLCJleHAiOjE1MjM0MTc2ODMsInBob25lX25vIjoiMTIzNDU2Nzg5MDEiLCJvcyI6IjAiLCJvc192ZXJzaW9uIjoiNi4wLjEiLCJhcHBfdmVyc2lvbiI6IjEuMCIsImNoYW5uZWwiOiIifQ.RKT-W3eYH2yvnuIeBQezs-nShYJRrAkF9RD0zCSbFdE"
    var userName: String = "User"
    var telephone: String = ""
    var country: String = ""
    var zondCode: String = ""

    init {
        if (Settings.getUserName().isNotBlank())
            this.userName = Settings.getUserName()
        if (Settings.getToken().isNotBlank())
            this.access_token = Settings.getToken()
        if (Settings.getTelephone().isNotBlank())
            this.telephone = Settings.getTelephone()
        if (Settings.getCountry().isNotBlank())
            this.country = Settings.getCountry()
        if (Settings.getCountry().isNotBlank())
            this.zondCode = Settings.getZoneCode()
    }

    companion object {
        var currentUser: User = User()
    }

    fun isLogin(): Boolean = access_token.isNotBlank()

    fun login(userLogin: LoginResponse.LoginItem) {
        Settings.setToken(userLogin.token)
        Settings.setUserName("User")

        //init
        this.access_token = userLogin.token
        this.userName = "User"
        currentUser = this@User
    }

    fun logout() {
        Settings.setToken("")
        Settings.setUserName("")
        currentUser = User()

    }

}