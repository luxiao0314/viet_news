package com.viet.news.dialog

import android.support.v4.app.FragmentActivity
import com.viet.news.core.R

/**
 * @Description 进度条dialog
 * *
 * @Author lucio
 * *
 * @Email xiao.lu@magicwindow.cn
 * *
 * @Date 21/08/2017 6:29 PM
 * *
 * @Version 1.0.0
 */
class ProgressDialogFragment : BaseDialogFragment() {

    var listener: (() -> Unit?)? = null

    companion object {
        fun create(context: FragmentActivity): BaseDialogFragment {
            return DialogBuilder(context, ProgressDialogFragment::class.java)
                    .setCancelableOnTouchOutside(false)
                    .showAllowingStateLoss()
        }
    }

    override fun build(builder: BaseDialogFragment.Builder): BaseDialogFragment.Builder {
        val view = builder.layoutInflater.inflate(R.layout.cus_progress, null, false)
        return builder.setView(view)
    }

    fun setOnCancelListener(listener: () -> Unit): ProgressDialogFragment {
        this.listener = listener
        return this
    }

    override fun onDestroyView() {
        listener?.let { it() }
        super.onDestroyView()
    }
}
