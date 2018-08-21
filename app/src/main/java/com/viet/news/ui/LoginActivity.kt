package com.viet.news.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.safframework.ext.then
import com.safframework.log.L
import com.viet.news.R
import com.viet.news.core.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*


/**
 * @author Tsing
 * @email shuqing.li@magicwindow.cn
 */
class LoginActivity : BaseActivity() {
    private var callbackManager: CallbackManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        setContentView(R.layout.activity_login)
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        L.e("LoginActivity onSuccess，token = ${loginResult.accessToken.token}")
                    }

                    override fun onCancel() {
                        L.e("LoginActivity onCancel")
                    }

                    override fun onError(exception: FacebookException) {
                        L.e("LoginActivity onError,exception=$exception")
                        // ERR_CONNECTION_REFUSED
                        //ERR_TIMED_OUT
                    }
                })

        btn_check_login.setOnClickListener {
            val msg = AccessToken.getCurrentAccessToken()
                    ?.isExpired
                    ?.not()
                    ?.then("token有效", "token过期")
                    ?: "没获取到token"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}