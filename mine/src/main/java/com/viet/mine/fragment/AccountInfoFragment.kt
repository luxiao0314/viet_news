package com.viet.mine.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chenenyu.router.annotation.Route
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import com.viet.mine.R
import com.viet.mine.utils.updateHeader
import com.viet.mine.viewmodel.AccountInfoViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.RefreshUserInfoEvent
import com.viet.news.core.domain.Settings
import com.viet.news.core.domain.User
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ext.finishWithAnim
import com.viet.news.core.ext.loadCircle
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.ui.widget.CommonItem
import com.viet.news.core.utils.RxBus
import kotlinx.android.synthetic.main.fragment_mine_account_info.*
import java.util.*

/**
 * @Description 用户信息
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
@Route(value = [Config.ROUTER_MINE_EDIT_INFO_FRAGMENT])
class AccountInfoFragment : BaseFragment() {

    private var mContainerView: View? = null
    private val model by viewModelDelegate(AccountInfoViewModel::class)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_account_info, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
        iv_user_icon.loadCircle(User.currentUser.avatarUrl)
        ll_update_avatar.clickWithTrigger { updateHeader(model.selectList) }

        val changeNameItem = view.findViewById<CommonItem>(R.id.item_change_name)
        val changePhoneNumItem = view.findViewById<CommonItem>(R.id.item_change_phone_num)
        val resetPwdItem = view.findViewById<CommonItem>(R.id.item_reset_pwd)
        changeNameItem.clickWithTrigger {  openPage(this@AccountInfoFragment, Config.ROUTER_MINE_EDIT_CHANGE_NICKNAME_FRAGMENT, R.id.container_framelayout) }
        changePhoneNumItem.clickWithTrigger { openPage(this@AccountInfoFragment, Config.ROUTER_MINE_EDIT_CHANGE_PHONE_FRAGMENT, R.id.container_framelayout) }
        resetPwdItem.clickWithTrigger { openPage(this@AccountInfoFragment, Config.ROUTER_MINE_EDIT_CHANGE_PWD_FRAGMENT, R.id.container_framelayout) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择结果回调
                    model.selectList = PictureSelector.obtainMultipleResult(data) as ArrayList<LocalMedia>
//                    dialogFragment = ProgressDialogFragment.create(this)
//                            .setCancelableOnTouchOutside(false) //点击屏幕不取消
//                            .showAllowingStateLoss()

                    model.uploadFile(this) {
                        if (it) {
                            User.currentUser.avatarUrl = model.selectList[0].compressPath
                            iv_user_icon.loadCircle(User.currentUser.avatarUrl)
                            // 将头像添加到缓存
//                                    Settings.setAvatarUrl(User.currentUser.avatarUrl)
                            Settings.create().avatar = User.currentUser.avatarUrl
                            RxBus.get().post(RefreshUserInfoEvent())
                        }
//                        dialogFragment?.dismissAllowingStateLoss()
                    }
                }
            }
        }
    }


}