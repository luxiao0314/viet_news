package com.viet.news.dialog

import android.os.Bundle
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

    var listener: ProgressCancelListener? = null

    companion object {
        fun create(context: FragmentActivity): BaseDialogFragment {
            return DialogBuilder(context, ProgressDialogFragment::class.java)
                    .setCancelableOnTouchOutside(false)
                    .setDimAmount(0F)
                    .showAllowingStateLoss()
        }
    }

    override fun build(builder: BaseDialogFragment.Builder): BaseDialogFragment.Builder {
        val view = builder.layoutInflater.inflate(R.layout.cus_progress, null, false)
        builder.setView(view)
        return builder
    }

    fun setOnCancelListener(listener: ProgressCancelListener): ProgressDialogFragment {
        this.listener = listener
        return this
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments == null) {
            throw IllegalArgumentException("use ProgressDialogBuilder to construct this dialog")
        }
    }

    override fun onDestroyView() {
        listener?.onCancelProgress()
        super.onDestroyView()
    }

    interface ProgressCancelListener {
        fun onCancelProgress()
    }
}
