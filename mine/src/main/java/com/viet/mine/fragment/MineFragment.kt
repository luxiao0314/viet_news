package com.viet.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.safframework.ext.clickWithTrigger
import com.viet.mine.R
import com.viet.mine.viewmodel.MineViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.LoginEvent
import com.viet.news.core.domain.LogoutEvent
import com.viet.news.core.domain.RefreshUserInfoEvent
import com.viet.news.core.domain.User
import com.viet.news.core.ext.loadCircle
import com.viet.news.core.ext.routerWithAnim
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.utils.RxBus
import kotlinx.android.synthetic.main.fragment_mine.*


/**
 * @Description 我的
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
class MineFragment : BaseFragment() {

    private var mContainerView: View? = null
    private val model: MineViewModel by viewModelDelegate(MineViewModel::class)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine, container, false)
        return mContainerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    override fun initView(view: View) {
        refresh()
        edit.clickWithTrigger { routerWithAnim(Config.ROUTER_MINE_EDIT_INFO_ACTIVITY).go(this) }
        mine_settings.clickWithTrigger { routerWithAnim(Config.ROUTER_MINE_SETTING_ACTIVITY).go(context) }
        mine_collection.clickWithTrigger { routerWithAnim(Config.ROUTER_MINE_COLLECTION_ACTIVITY).go(context) }
        mine_invite.clickWithTrigger { routerWithAnim(Config.ROUTER_MINE_INVITE_ACTIVITY).go(context) }
        mine_wallet.clickWithTrigger { routerWithAnim(Config.ROUTER_MINE_WALLET_ACTIVITY).go(context) }
    }

    private fun initEvent() {
        compositeDisposable.add(RxBus.get().register(LogoutEvent::class.java) { refresh() })
        compositeDisposable.add(RxBus.get().register(LoginEvent::class.java) { refresh() })
        compositeDisposable.add(RxBus.get().register(RefreshUserInfoEvent::class.java) { refresh() })
    }

    private fun refresh() {
        if (!User.currentUser.isLogin()) {
            btn_login.visibility = View.VISIBLE
            iv_user_icon.visibility = View.GONE
            rl_user.visibility = View.GONE
            edit.visibility = View.GONE
            btn_login.clickWithTrigger { routerWithAnim(Config.ROUTER_LOGIN_ACTIVITY).anim(R.anim.dialog_push_bottom_in, R.anim.dialog_push_bottom_out).go(this) }
        } else {
            btn_login.visibility = View.GONE
            iv_user_icon.visibility = View.VISIBLE
            rl_user.visibility = View.VISIBLE
            edit.visibility = View.VISIBLE

            iv_user_icon.loadCircle(User.currentUser.avatarUrl)
            tv_nickname.text = User.currentUser.userName
            tv_fans_count.text =  User.currentUser.fansCount.toString()
            tv_follow_count.text = User.currentUser.followCount.toString()
        }
    }
}
