package com.viet.news.core.domain.request

import com.viet.news.core.utils.EncryptUtils
import java.io.Serializable

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 11/09/2018 10:21 AM
 * @Version
 */
class ResetPwdParams : Serializable {
    var phone_number: String? = null
    var password: String? = null
        set(value) {
            field = EncryptUtils.encryptPayPassword(value?:"")
        }
    var validation_code: String? = null
}