package com.viet.mine.fragment

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.view.View
import android.widget.Toast
import com.chenenyu.router.annotation.Route
import com.jakewharton.rxbinding2.widget.RxTextView
import com.viet.mine.R
import com.viet.mine.viewmodel.SettingViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ext.finishWithAnim
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_mine_setting_feedback.*

/**
 * @Description 用户反馈
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_SETTING_FEEDBACK_FRAGMENT])
class FeedBackFragment : BaseFragment() {
    private val model by viewModelDelegate(SettingViewModel::class, true)

    override fun isSupportSwipeBack(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine_setting_feedback
    }

    override fun initView(view: View) {
        initListener(view)
    }

    @SuppressLint("CheckResult")
    private fun initListener(view: View) {
        RxTextView.textChanges(edit_content)
                .subscribe {
                    model.feedback.value = it.toString()
                    model.count.value = it.toString().trim().length
                    model.checkFeedBackSubmitBtnEnable()
                }

        confirm_btn.clickWithTrigger {
            val feedback = edit_content.text.toString()
            if (model.feedBackSubmitEnable()) {
                model.feedBack(this, feedback) { isOk ->
                    if (isOk) {
                        (activity as BaseActivity).finishWithAnim()
                        Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "提交失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //注册按钮能否点击更新
        model.submitEnable.observe(this, Observer { confirm_btn.isEnabled = it != null && it })
        model.count.observe(this, Observer { tv_count.text = if (it!!.toInt() > 300) "300/300" else "$it/300" })
    }


}