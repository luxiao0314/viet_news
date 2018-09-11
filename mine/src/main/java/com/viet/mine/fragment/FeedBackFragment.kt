package com.viet.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.viet.mine.R
import com.viet.mine.viewmodel.SettingViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_mine_setting_feedback.*

/**
 * @Description 用户反馈
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
class FeedBackFragment : BaseFragment() {
    private var mContainerView: View? = null
    //    lateinit var scrollView:ScrollView
    private val model by viewModelDelegate(SettingViewModel::class, true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_setting_feedback, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
//         scrollView = view.findViewById(R.id.scrollView)
        initListener()
    }

    private fun initListener() {
        confirm_btn.clickWithTrigger {
            model.feedBack(this,"213132123132")
//                    .bindLifecycle(this)
//                    .subscribe(object:ProgressMaybeObserver<HttpResponse<Any>>(this))
        }
//        SoftKeyInputVisibleUtils().registerFragment(this) { keyboardVisible ->
//            if (edit_content.hasFocus()) {//焦点在描述输入框上
//                if (keyboardVisible) {//键盘已经弹出
//
//                }
//            }
//        }
    }
}