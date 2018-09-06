package com.viet.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import cn.magicwindow.core.utils.SoftKeyInputVisibleUtils
import com.viet.mine.R
import com.viet.news.core.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_mine_setting_feedback.*

class FeedBackFragment : BaseFragment() {
    private var mContainerView: View? = null
//    lateinit var scrollView:ScrollView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_setting_feedback, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
//         scrollView = view.findViewById(R.id.scrollView)
        initListener()
    }

    private fun initListener() {
//        SoftKeyInputVisibleUtils().registerFragment(this) { keyboardVisible ->
//            if (edit_content.hasFocus()) {//焦点在描述输入框上
//                if (keyboardVisible) {//键盘已经弹出
//
//                }
//            }
//        }
    }
}