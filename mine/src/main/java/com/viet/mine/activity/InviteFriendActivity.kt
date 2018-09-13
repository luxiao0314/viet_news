package com.viet.mine.activity

import android.os.Bundle
import android.view.View
import com.chenenyu.router.annotation.Interceptor
import com.chenenyu.router.annotation.Route
import com.jaeger.library.StatusBarUtil
import com.safframework.ext.dp2px
import com.viet.mine.R
import com.viet.news.core.config.Config
import com.viet.news.core.ui.InjectActivity
import com.viet.news.core.utils.QRCodeUtil
import com.viet.news.dialog.ShareDialog
import kotlinx.android.synthetic.main.activity_mine_invite.*

@Route(value = [Config.ROUTER_MINE_INVITE_ACTIVITY], interceptors = [Config.LOGIN_INTERCEPTOR])
class InviteFriendActivity : InjectActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        setContentView(R.layout.activity_mine_invite)
        initView()
        initListener()
        initData()
    }

    private fun initView() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null)
        val qrCode = QRCodeUtil.createQRCodeBitmap("今晚打老虎", dp2px(100))
        qrCode?.let {
            iv_qr_code.setImageBitmap(it)
        }
    }

    private fun initListener() {
        iv_qr_code.setOnLongClickListener {
            ShareDialog.createBuilder(this@InviteFriendActivity)
            true
        }

    }

    private fun initData() {

    }

}