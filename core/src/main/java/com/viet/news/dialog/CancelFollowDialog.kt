package com.viet.news.dialog

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import com.viet.news.core.R
import com.viet.news.core.ext.clickWithTrigger
import kotlinx.android.synthetic.main.dialog_cancle_follow.view.*

/**
 * @Description 取消关注对话框
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 17/09/2018 11:21 AM
 * @Version
 */
class CancelFollowDialog : BaseDialogFragment() {

    companion object {
        fun create(context: FragmentActivity?) =
                DialogBuilder(context, CancelFollowDialog::class.java)
                        .setCancelableOnTouchOutside(false)
                        .setAnimStyle(R.style.AnimScaleCenter)
                        .showAllowingStateLoss()

        fun create(fragment: Fragment) =
                DialogBuilder(fragment.activity, CancelFollowDialog::class.java)
                        .setCancelableOnTouchOutside(false)
                        .setTargetFragment(fragment,1000)
                        .setAnimStyle(R.style.AnimScaleCenter)
                        .showAllowingStateLoss()
    }

    override fun build(builder: Builder): Builder {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_cancle_follow, null)
        view.tv_cancel.clickWithTrigger {
            cancelListeners?.onCancelled(1)
            dismiss()
        }
        view.tv_confirm.clickWithTrigger {
            positiveListeners?.onPositiveButtonClicked(1)
            dismiss()
        }
        return builder.setView(view)
    }
}