package com.viet.news.core.ui.code

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.viet.news.core.R
import kotlinx.android.synthetic.main.layout_pwd_keyboard.view.*


/**
 * @Description 数字输入键盘
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 22/08/2018 3:59 PM
 * @Version
 */
class KeyboardView : FrameLayout, View.OnClickListener {

    private var codeView: CodeView? = null
    private var listener: Listener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    fun setCodeView(codeView: CodeView) {
        this.codeView = codeView
    }

    private fun init() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_pwd_keyboard, null)
        view.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        addView(view)
        keyboard_0.setOnClickListener(this)
        keyboard_1.setOnClickListener(this)
        keyboard_2.setOnClickListener(this)
        keyboard_3.setOnClickListener(this)
        keyboard_4.setOnClickListener(this)
        keyboard_5.setOnClickListener(this)
        keyboard_6.setOnClickListener(this)
        keyboard_7.setOnClickListener(this)
        keyboard_8.setOnClickListener(this)
        keyboard_9.setOnClickListener(this)
        keyboard_hide.setOnClickListener(this)
        keyboard_delete.setOnClickListener(this)
    }

    fun hide() {
        visibility = View.GONE
    }

    fun show() {
        visibility = View.VISIBLE
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    override fun onClick(v: View) {
        val tag = v.tag as String
        when (tag) {
            "hide" -> hide()
            "delete" -> {
                codeView?.delete()
                listener?.onDelete()
            }
            else -> {
                codeView?.input(tag)
                listener?.onInput(tag)
            }
        }
    }

    interface Listener {
        fun onInput(s: String)
        fun onDelete()
    }
}
