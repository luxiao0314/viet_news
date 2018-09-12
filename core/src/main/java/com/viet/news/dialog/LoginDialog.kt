package com.viet.news.dialog

import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import com.safframework.ext.clickWithTrigger
import com.viet.news.core.R
import com.viet.news.core.config.Config
import com.viet.news.core.ext.routerWithAnim
import kotlinx.android.synthetic.main.dialog_login.view.*

/**
 * @Description
 * @Author lucio
 * @Email xiao.lu@magicwindow.cn
 * @Date 18/01/2018 7:23 PM
 * @Version
 */
class LoginDialog : BaseDialogFragment() {

    companion object {
        fun create(context: FragmentActivity) =
                DialogBuilder(context, LoginDialog::class.java)
                        .setCancelableOnTouchOutside(false)
                        .setAnimStyle(R.style.AnimScaleCenter)
                        .showAllowingStateLoss()
    }

    override fun build(builder: Builder): Builder {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_login, null)
        view.iv_cancle.clickWithTrigger {
            cancelListeners?.onCancelled(1)
            dismiss()
        }
        view.btn_login.clickWithTrigger {
            routerWithAnim(Config.ROUTER_LOGIN_ACTIVITY).requestCode(0).go(this)
            dismiss()
        }
        return builder.setView(view)
    }
}