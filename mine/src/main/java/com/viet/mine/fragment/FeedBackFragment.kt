package com.viet.mine.fragment

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.viet.mine.R
import com.viet.mine.viewmodel.SettingViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_login_pwd.*
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
        initListener(view)
    }

    @SuppressLint("CheckResult")
    private fun initListener(view: View) {
        RxTextView.textChanges(edit_content)
                .subscribe {
                    model.feedback.value = it.toString()
                    model.count.value = it.toString().trim().length
                    model.checkSubmitBtnEnable()
                }

        confirm_btn.clickWithTrigger {
            val feedback = edit_content.text.toString()
            if (model.submitEnable()) {
                model.feedBack(this, feedback) { isOk ->
                    if (isOk) {
                        Navigation.findNavController(view).navigateUp()
                        Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //注册按钮能否点击更新
        model.submitEnable.observe(this, Observer { confirm_btn.isEnabled = it != null && it })
        model.count.observe(this, Observer { tv_count.text = if (it!!.toInt() > 300) "300/300" else "${it.toString()}/300" })
//        SoftKeyInputVisibleUtils().registerFragment(this) { keyboardVisible ->
//            if (edit_content.hasFocus()) {//焦点在描述输入框上
//                if (keyboardVisible) {//键盘已经弹出
//
//                }
//            }
//        }
    }


}