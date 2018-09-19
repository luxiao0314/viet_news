package com.viet.news.core.domain.request

import cn.magicwindow.core.utils.EncryptUtils

class ResetPwdWithOldpwdParams{
    var new_password: String? = null
        set(value) {
            field = EncryptUtils.encryptPayPassword(value?:"")
        }
    var old_password: String? = null
        set(value) {
            field = EncryptUtils.encryptPayPassword(value?:"")
        }
}