package com.viet.news.core.config

object Config {

    const val NETWORK_OK = 200
    const val NETWORK_RESPONSE_OK = 0

    val SHARE_REQUEST_CODE_FACEBOOK = 1001
    val SHARE_REQUEST_CODE_MESSENGER = 1002
    val SHARE_REQUEST_CODE_COPY = 1003

    // Router URI
    const val ROUTER_LOGIN_ACTIVITY = "/login/activity"

    //语言切换
    const val LANGUAGE_CHANGED = "language_changed"
    const val LAST_LANGUAGE = "lastLanguage"
    const val SELECTED_LANGUAGE = "selected_language"

    interface ErrorCode {
        companion object {
            val UNKNOWN = 1
            val CANT_GET_STORE_PASSWORD = 2
            val NETWORK_RESPONSE_LOGIN_INVALIDATE = 403
        }
    }

    //bundle
    const val COUNTRY = "country_abbreviation"
    const val USER_NAME = "user_name"
    const val TEL_PHONE = "tel_phone"
    const val BUNDLE_ID = "bundle_id"

    const val PACT_URL = "http://magic.merculet.io/pact/zh-cn"

    //登录-用户协议
    const val login_userProtocol = "login_userProtocol"
}
