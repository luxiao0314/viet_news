package com.viet.news.dialog

import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import com.viet.news.core.R
import com.viet.news.core.domain.Config
import com.viet.news.core.ext.clickWithTrigger
import kotlinx.android.synthetic.main.dialog_share.view.*


/**
 * @Description
 * @Author lucio
 * @Email xiao.lu@magicwindow.cn
 * @Date 18/01/2018 7:23 PM
 * @Version
 */
class ShareDialog : BaseDialogFragment() {

    companion object {
        fun createBuilder(context: FragmentActivity) =
                DialogBuilder(context, ShareDialog::class.java)
                        .setCancelableOnTouchOutside(false)
                        .setFullScreen(true)
                        .setShowButtom(true)
                        .showAllowingStateLoss()
    }

    override fun build(builder: Builder): Builder {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_share, null)
        initView(view)
        return builder.setView(view)
    }

    private fun initView(view: View) {
        view.tv_cancel.clickWithTrigger { dismiss() }
        view.tv_messenger.clickWithTrigger {
            positiveListeners?.onPositiveButtonClicked(Config.SHARE_REQUEST_CODE_MESSENGER)
            dismiss()
        }
        view.tv_facebook.clickWithTrigger {
            positiveListeners?.onPositiveButtonClicked(Config.SHARE_REQUEST_CODE_FACEBOOK)
            dismiss()
        }
        view.tv_copy.clickWithTrigger {
            positiveListeners?.onPositiveButtonClicked(Config.SHARE_REQUEST_CODE_COPY)
            dismiss()
        }
    }
}
