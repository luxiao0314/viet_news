package io.merculet.core.ui.dialog

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import com.viet.news.core.R
import com.viet.news.core.ext.click
import com.viet.news.dialog.BaseDialogFragment
import com.viet.news.dialog.DialogBuilder
import kotlinx.android.synthetic.main.dialog_normal.view.*

/**
 * @Description
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Date 18/01/2018 7:23 PM
 * @Version
 */
class NormalDialog : BaseDialogFragment() {

    companion object {
        const val EXTRA_TITLE = "extra_title"    //提示框标题
        const val EXTRA_CONTENT = "extra_content"    //提示框内容
        const val EXTRA_CANCEL = "extra_cancel"    //取消按钮的文本
        const val EXTRA_CONFIRM = "extra_confirm"    //确定按钮文本

        fun create(context: FragmentActivity, title: String? = null, content: String? = null, confirmText: String? = null, cancelText: String? = null) =
                NormalBuilder(context, title, content, cancelText, confirmText)
                        .setCancelableOnTouchOutside(false)
                        .setAnimStyle(R.style.DialogAnimScaleCenter)
                        .setShowButtom(false)
                        .showAllowingStateLoss()
    }

    /**
     * 执行回调只用实现对应的listener即可

     * @param builder
     * *
     * @return
     */
    override fun build(initialBuilder: Builder): Builder {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_normal, null)
        val title = arguments?.getString(EXTRA_TITLE)

        //title
        if (title != null && title.isNotEmpty()) {
            view.dialog_title.text = title
        } else {
            view.dialog_title.visibility = View.INVISIBLE
            view.v_title_divider.visibility = View.INVISIBLE
        }

        //content
        view.dialog_content.text = arguments?.getString(EXTRA_CONTENT)

        //confirmText
        val confirmText = arguments?.getString(EXTRA_CONFIRM)
        confirmText?.apply {
            view.btn_dialog_ok.text = this
        }

        //cancel button
        val cancelText = arguments?.getString(EXTRA_CANCEL)
        if (cancelText != null && cancelText.isNotEmpty()) {
            view.btn_dialog_cancel.visibility = View.VISIBLE
            view.btn_dialog_cancel.text = cancelText
            view.btn_dialog_ok.textSize = 14f
        }

        view.btn_dialog_ok.click {
            positiveListeners?.onPositiveButtonClicked(DIALOG_POSITIVE_REQUEST_CODE)
            dismiss()
        }
        view.btn_dialog_cancel.click {
            negativeListeners?.onNegativeButtonClicked(DIALOG_NEGATIVE_REQUEST_CODE)
            dismiss()
        }
        return initialBuilder.setView(view)
    }


    class NormalBuilder(context: FragmentActivity, var title: String? = null, var content: String? = null, private val cancelText: String? = null, private val confirmText: String? = null) : DialogBuilder(context, NormalDialog::class.java) {

        fun setTitle(t: String): NormalBuilder {
            this.title = t
            return this
        }

        fun setMessage(content: String): NormalBuilder {
            this.content = content
            return this
        }


        override fun prepareArguments(): Bundle {
            val args = Bundle()
            args.putSerializable(EXTRA_TITLE, title)
            args.putString(EXTRA_CONTENT, content)
            args.putString(EXTRA_CANCEL, cancelText)
            args.putString(EXTRA_CONFIRM, confirmText)
            return args
        }
    }
}