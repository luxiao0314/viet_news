package com.viet.news.core.domain.request

import cn.magicwindow.core.utils.DeviceInfoUtils
import com.viet.news.core.utils.EncryptUtils
import com.viet.news.core.ui.App
import java.io.Serializable

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 11/09/2018 10:21 AM
 * @Version
 */
class SignInParams : Serializable {
    private var device_id: String? = DeviceInfoUtils.getDeviceId(App.instance)
    private var os_type: String? =  DeviceInfoUtils.os
    var invite_code: String? = ""
    var oauth_type: String? = ""
    var oauth_user_id: String? = ""
    var password: String? = ""
        set(value) {
            field = EncryptUtils.encryptPayPassword(value?:"")
        }
    var phone_number: String? = ""
    var verify_code: String? = ""
//    var email: String? = ""
//    var key: String? = ""
}