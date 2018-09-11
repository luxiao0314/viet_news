package com.viet.mine.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.safframework.ext.clickWithTrigger
import com.viet.follow.activity.PersonalPageActivity
import com.viet.mine.R
import com.viet.mine.activity.*
import com.viet.news.core.domain.LoginEvent
import com.viet.news.core.domain.LogoutEvent
import com.viet.news.core.domain.User
import com.viet.news.core.ext.loadCircle
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine, container, false)
        return mContainerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        initData()
    }

    private fun initData() {
        tv_nickname.text = User.currentUser.userName
    }

    private fun initEvent() {
        compositeDisposable.add(RxBus.get().register(LogoutEvent::class.java) { refresh() })
        compositeDisposable.add(RxBus.get().register(LoginEvent::class.java) { refresh() })
    }

    private fun refresh() {
        if (!User.currentUser.isLogin()) {
            btn_login.visibility = View.VISIBLE
            iv_user_icon.visibility = View.GONE
            rl_user.visibility = View.GONE
            edit.visibility = View.GONE
            btn_login.clickWithTrigger { context?.startActivity(Intent(activity, LoginActivity::class.java)) }
        } else {
            btn_login.visibility = View.GONE
            iv_user_icon.visibility = View.VISIBLE
            rl_user.visibility = View.VISIBLE
            edit.visibility = View.VISIBLE
            iv_user_icon.loadCircle("")
        }
    }

    override fun initView(view: View) {
        refresh()

        iv_user_icon.clickWithTrigger {
            startActivity(Intent(activity, PersonalPageActivity::class.java))
        }

        edit.clickWithTrigger {
            context?.startActivity(Intent(activity, AccountInfoActivity::class.java))
        }

        mine_settings.setClickDelegate {
            onItemClick = {
                startActivity(SettingActivity::class.java)
            }
        }

        mine_collection.setClickDelegate {
            onItemClick = {
                startActivity(CollectionActivity::class.java)
            }
        }

        mine_invite.setClickDelegate {
            onItemClick = {
                startActivity(InviteFriendActivity::class.java)
            }
        }

        mine_wallet.setClickDelegate {
            onItemClick = {
                startActivity(MineWalletActivity::class.java)
            }
        }

    }

    private fun startActivity(cls: Class<*>) {
        if (User.currentUser.isLogin()) {
            context?.startActivity(Intent(activity, cls))
        } else {
            context?.startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}
