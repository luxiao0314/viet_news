package com.viet.news.core.config

object Config {

    const val NETWORK_OK = 200
    const val NETWORK_RESPONSE_OK = 0

    val SHARE_REQUEST_CODE_FACEBOOK = 1001
    val SHARE_REQUEST_CODE_MESSENGER = 1002
    val SHARE_REQUEST_CODE_COPY = 1003
    //倒计时1分钟
    val COUNT_DOWN_TIMER = 60*1000L



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

    // Router Interceptor
    const val LOGIN_INTERCEPTOR = "LoginInterceptor"
    // Router URI
    const val ROUTER_LOGIN_ACTIVITY = "/login/activity"
    const val ROUTER_MAIN_ACTIVITY = "/main/activity"
    const val ROUTER_PERSONAL_PAGE_ACTIVITY = "/personal_page/activity"
    //我的->activity
    const val ROUTER_MINE_WALLET_ACTIVITY = "/mine/wallet/activity"
    const val ROUTER_MINE_INVITE_ACTIVITY = "/mine/invite/activity"
    const val ROUTER_MINE_COLLECTION_ACTIVITY = "/mine/collection/activity"
    const val ROUTER_MINE_SETTING_ACTIVITY = "/mine/setting/activity"
    const val ROUTER_MINE_EDIT_INFO_ACTIVITY = "/mine/edit_info/activity"
    const val ROUTER_FUNS_AND_FOLLOW_ACTIVITY = "/mine/funs_and_follow/activity"

    //我的->fragment
    const val ROUTER_MINE_SETTING_FRAGMENT = "/mine/setting/fragment"
    const val ROUTER_MINE_SETTING_LANGUAGE_FRAGMENT = "/mine/setting/language/fragment"
    const val ROUTER_MINE_SETTING_HELP_FRAGMENT = "/mine/setting/help/fragment"
    const val ROUTER_MINE_SETTING_FEEDBACK_FRAGMENT = "/mine/setting/feedback/fragment"

    const val ROUTER_MINE_EDIT_INFO_FRAGMENT = "/mine/edit_info/fragment"
    const val ROUTER_MINE_EDIT_CHANGE_NICKNAME_FRAGMENT = "/mine/edit/change/nickname/fragment"
    const val ROUTER_MINE_EDIT_CHANGE_PHONE_FRAGMENT = "/mine/edit/change/phone/fragment"
    const val ROUTER_MINE_EDIT_CHANGE_PWD_FRAGMENT = "/mine/edit/change/pwd/fragment"
    const val ROUTER_MINE_EDIT_VERIFY_CODE_FRAGMENT = "/mine/edit/verify_code/fragment"

    const val DB_NAME = "db_news_app"
    const val T_SOURCE = "t_source"
    const val T_ARTICLE = "t_article"


}
