package com.viet.news.core.ui.widget

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.TextViewCompat
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import android.view.Gravity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.safframework.ext.dp2px
import com.safframework.log.L
import com.viet.news.core.R

/**
 * @author Tsing
 * @email shuqing.li@magicwindow.cn
 */
class FacebookLoginButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {
    companion object {
        private const val CONTENT_PADDING_LEFT_RIGHT = 32
        private const val CONTENT_PADDING_TOP_BOTTOM = 7
        private const val TEXT_PADDING = 17
    }

    init {
        //背景
        setBackgroundResource(R.drawable.selector_facebook_button_bg)
        //字体色
        setTextColor(ContextCompat.getColor(context, R.color.white))
        //文案
        setText(R.string.login_with_facebook)
        //左侧图标
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_facebook_svg)
        drawable?.let {
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(this, it, null, null, null)
            compoundDrawablePadding = context.dp2px(TEXT_PADDING)
        }
        //padding
        setPadding(context.dp2px(CONTENT_PADDING_LEFT_RIGHT)
                , context.dp2px(CONTENT_PADDING_TOP_BOTTOM)
                , context.dp2px(CONTENT_PADDING_LEFT_RIGHT + TEXT_PADDING)
                , context.dp2px(CONTENT_PADDING_TOP_BOTTOM))
        //居中
        gravity = Gravity.CENTER_VERTICAL
    }

    /**
     * callbackManager = CallbackManager.Factory.create()
     * 使用时 必须在相应activity/fragment的onActivityResult方法中调用 callbackManager.onActivityResult 否则无法接收到回调
     */
    fun setOnLoginListener(fragment: Fragment, callbackManager: CallbackManager, needChangeLoginStatus: Boolean = false, init: OnFacebookCallbackWrapper.() -> Unit) {
        setListener(fragment = fragment, callbackManager = callbackManager, needChangeLoginStatus = needChangeLoginStatus, init = init)
    }

    /**
     * val callbackManager = CallbackManager.Factory.create()
     * 使用时 必须在相应activity/fragment的onActivityResult方法中调用 callbackManager.onActivityResult 否则无法接收到回调
     */
    fun setOnLoginListener(activity: Activity, callbackManager: CallbackManager, needChangeLoginStatus: Boolean = false, init: OnFacebookCallbackWrapper.() -> Unit) {
        setListener(activity = activity, callbackManager = callbackManager, needChangeLoginStatus = needChangeLoginStatus, init = init)
    }

    private fun setListener(activity: Activity? = null, fragment: Fragment? = null, callbackManager: CallbackManager, needChangeLoginStatus: Boolean = false, init: OnFacebookCallbackWrapper.() -> Unit) {
        val callback = OnFacebookCallbackWrapper {
            if (needChangeLoginStatus) {
                text = context.getString(R.string.logout)
            }
        }
        callback.init()
        setOnClickListener {
            val accessToken = AccessToken.getCurrentAccessToken()
            val isLoggedIn = accessToken != null && !accessToken.isExpired
            if (isLoggedIn) {
                //登出
                LoginManager.getInstance().logOut()
                if (needChangeLoginStatus) {
                    text = context.getString(R.string.login_with_facebook)
                }
            } else {
                activity?.let {
                    LoginManager.getInstance().logInWithReadPermissions(activity, listOf("public_profile"))
                }
                fragment?.let {
                    LoginManager.getInstance().logInWithReadPermissions(fragment, listOf("public_profile"))
                }
            }
        }
        LoginManager.getInstance().registerCallback(callbackManager, callback)
    }

    class OnFacebookCallbackWrapper(private val loginSuccess: () -> Unit) : FacebookCallback<LoginResult> {

        var onSuccess: ((result: LoginResult?) -> Unit)? = null
        var onCancel: (() -> Unit)? = null
        var onError: ((error: FacebookException?) -> Unit)? = null
        override fun onSuccess(result: LoginResult?) {
            L.e("onSuccess")
            loginSuccess()
            onSuccess?.let { it(result) }
        }

        override fun onCancel() {
            L.e("onSuccess")
            onCancel?.let { it() }
        }

        override fun onError(error: FacebookException?) {
            L.e("onSuccess")
            onError?.let { it(error) }
        }
    }

}