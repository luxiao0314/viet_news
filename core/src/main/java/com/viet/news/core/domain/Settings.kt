package com.viet.news.core.domain

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.safframework.delegate.prefs.initKey
import com.safframework.delegate.prefs.int
import com.safframework.delegate.prefs.string
import com.viet.news.core.ui.App

class Settings(prefs: SharedPreferences = App.instance.getSharedPreferences(SP_KEY_DEFAULT, preferenceMode)) {



    init {
        prefs.initKey("0b1r2a3v4o5o6n78") // 初始化密钥，且密钥是16位的
    }

    var token by prefs.string(SP_TOKEN, isEncrypt = true)

    var roleId by prefs.string(SP_ROLE_ID, isEncrypt = true)

    var userName  by prefs.string(SP_USERNAME, isEncrypt = true)

    //只在User内使用，其他地方调用
    var telephone by prefs.string(SP_TELEPHONE, isEncrypt = true)
    var userId by prefs.string(SP_USER_ID, isEncrypt = true)
    //只在User内使用
    var zoneCode by prefs.string(SP_ZONECODE, isEncrypt = true)
    var avatar by prefs.string(SP_AVATAR_URL, isEncrypt = true)
    var fansCount by prefs.int(SP_FANS_COUNT, isEncrypt = true)
    var followCount by prefs.int(SP_FOLLOW_COUNT, isEncrypt = true)
//    var countryAbbreviation by prefs.string(SP_COUNTRY_ABBREVIATION, isEncrypt = true)


    companion object {


        const val SP_TOKEN = "sp_token"
        const val SP_ROLE_ID = "sp_roleId"
        const val SP_USERNAME = "sp_username"
        const val SP_TELEPHONE = "sp_telephone"
        const val SP_USER_ID = "sp_user_id"
        const val SP_ZONECODE = "sp_zone_code"
        const val SP_AVATAR_URL = "sp_avatar_url"
        const val SP_FANS_COUNT = "sp_fans_count"
        const val SP_FOLLOW_COUNT = "sp_follow_count"


        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var defaultInstance: Settings? = null
        private var preferenceMode = Context.MODE_PRIVATE
        private const val SP_KEY_DEFAULT = "persistent_data"

        fun create(context: Context = App.instance): Settings {
            if (defaultInstance == null) {
                synchronized(Settings::class.java) {
                    if (defaultInstance == null) {
                        defaultInstance = Settings()
                    }
                }
            }
            return defaultInstance!!
        }
    }
}