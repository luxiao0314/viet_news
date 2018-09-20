package com.viet.news.core.config

object Config {

    const val NETWORK_OK = 200
    const val NETWORK_RESPONSE_OK = 0
    const val NETWORK_RESPONSE_HAS_NO_NETWORK = 1234321

    val SHARE_REQUEST_CODE_FACEBOOK = 1001
    val SHARE_REQUEST_CODE_MESSENGER = 1002
    val SHARE_REQUEST_CODE_COPY = 1003
    //倒计时1分钟
    val COUNT_DOWN_TIMER = 60 * 1000L
    //更换手机号短信标志
    val CHANGE_PHONE_NUM = 0
    //设置新手机手机号短信标志
    val SET_PHONE_NUM = 1
    val BIND_CHANGE_PHONE_NUM = 0
    val BIND_SET_PHONE_NUM = 1

    //对话框请求码
    const val DIALOG_OK_REQUEST_CODE = 100
    const val DIALOG_CANCEL_REQUEST_CODE = 1000
    const val DIALOG_SMS_REQUEST_CODE = 1001
    const val DIALOG_PAY_WITH_PWD_REQUEST_CODE = 1002
    const val DIALOG_CAN_NOT_PAY_REQUEST_CODE = 1003
    const val DIALOG_PAY_ERR_REQUEST_CODE = 1004
    const val DIALOG_PAY_FAIL_REQUEST_CODE = 1005
    const val DIALOG_ACCOUNT_FROZEN_REQUEST_CODE = 1006
    const val DIALOG_PAY_WITH_PWD_REQUEST_CODE_OTHER = 1007
    const val DIALOG_PAY_WITH_PWD_COME_WITH_MARKET = 1008
    const val DIALOG_PAY_WITH_PWD_COME_WITH_RED_POCKET = 1009
    const val DIALOG_PAY_FAIL_RESET_REQUEST_CODE = 1010// chulongxu新增payfail 重置密码按钮
    const val DIALOG_SHARE_FAIL_GO_TO_SEE = 2000
    const val DIALOG_SHARE_FAIL_SHARE = 2001
    const val DIALOG_WITHDRAW_REQUEST_CODE = 1011//shuqing 提现按钮点击时的确认弹窗
    const val DIALOG_SELECT_WX_GROUP = 2002

    //dialog默认屏幕占比
    const val DIALOG_DEFAULT_SCALE = 0.75
    //新的UI中dialog在屏幕上的宽度占比
    const val DIALOG_BIG_SCALE = 329.0 / 375


    //语言切换
    const val LANGUAGE_CHANGED = "language_changed"
    const val LAST_LANGUAGE = "lastLanguage"
    const val SELECTED_LANGUAGE = "selected_language"

    interface ErrorCode {
        companion object {
            val UNKNOWN = 1
            val NETWORK_RESPONSE_LOGIN_FORBIDDEN = 403
            val NETWORK_RESPONSE_LOGIN_UNAUTHORIZED = 401
        }
    }

    const val page_size = 10

    //bundle
    const val COUNTRY = "country_abbreviation"
    const val USER_NAME = "user_name"
    const val TEL_PHONE = "tel_phone"
    const val BUNDLE_ID = "bundle_id"
    const val BUNDLE_USER_ID = "bundle_user_id"

    const val PACT_URL = "http://magic.merculet.io/pact/zh-cn"

    //登录-用户协议
    const val login_userProtocol = "login_userProtocol"

    // Router Interceptor
    const val LOGIN_INTERCEPTOR = "RouterLoginInterceptor"
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
    const val ROUTER_WEBVIEW_ACTIVITY = "/webview/activity"

    //我的->fragment
    const val ROUTER_MINE_SETTING_FRAGMENT = "/mine/setting/fragment"
    const val ROUTER_MINE_SETTING_LANGUAGE_FRAGMENT = "/mine/setting/language/fragment"
    const val ROUTER_MINE_SETTING_HELP_FRAGMENT = "/mine/setting/help/fragment"
    const val ROUTER_MINE_SETTING_FEEDBACK_FRAGMENT = "/mine/setting/feedback/fragment"

    const val ROUTER_MINE_EDIT_INFO_FRAGMENT = "/mine/edit_info/fragment"
    const val ROUTER_MINE_EDIT_CHANGE_NICKNAME_FRAGMENT = "/mine/edit/change/nickname/fragment"
    const val ROUTER_MINE_EDIT_CHANGE_PHONE_FRAGMENT = "/mine/edit/change/phone/fragment"
    const val ROUTER_MINE_EDIT_BIND_PHONE_FRAGMENT = "/mine/edit/bind/phone/fragment"
    const val ROUTER_MINE_EDIT_CHANGE_PWD_FRAGMENT = "/mine/edit/change/pwd/fragment"
    const val ROUTER_MINE_EDIT_SETUP_PWD_FRAGMENT = "/mine/edit/setup/pwd/fragment"
    const val ROUTER_MINE_EDIT_VERIFY_CODE_FRAGMENT = "/mine/edit/verify_code/fragment"

    const val DB_NAME = "db_news_app"
    const val FAVORITE_TABLE_NAME = "favorite_table"
    const val NULL_TABLE_NAME = "null_table"
    const val T_ARTICLE = "t_article"


}
