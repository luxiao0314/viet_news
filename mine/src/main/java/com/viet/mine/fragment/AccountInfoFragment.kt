package com.viet.mine.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chenenyu.router.Router
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
import com.viet.news.core.ext.*
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.ui.widget.CommonItem
import com.viet.news.core.utils.RxBus
import com.viet.news.dialog.NormalDialog
import com.viet.news.dialog.interfaces.IPositiveButtonDialogListener
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
        iv_user_icon.loadCircle(User.currentUser.avatar)
        ll_update_avatar.clickWithTrigger { updateHeader(model.selectList) }

        val changeNameItem = view.findViewById<CommonItem>(R.id.item_change_name)
        val changePhoneNumItem = view.findViewById<CommonItem>(R.id.item_change_phone_num)
        val resetPwdItem = view.findViewById<CommonItem>(R.id.item_reset_pwd)
        val magicBoxItem = view.findViewById<CommonItem>(R.id.item_magicbox)
        changeNameItem.clickWithTrigger { openPage(this@AccountInfoFragment, Config.ROUTER_MINE_EDIT_CHANGE_NICKNAME_FRAGMENT, R.id.container_framelayout) }

        if (User.currentUser.isLogin()) {
            model.getUserInfo(Settings.create(context!!).userId, activity!!) { it ->
                if (false) {//it!!.is_bind
                    changePhoneNumItem.setRightText("已绑定")
                    changePhoneNumItem.clickWithTrigger {
                        routerWithAnim(Config.ROUTER_MINE_EDIT_CHANGE_PHONE_FRAGMENT).goFragment(this@AccountInfoFragment, R.id.container_framelayout)
                    }
                } else {
                    changePhoneNumItem.setRightText("未绑定")
                    changePhoneNumItem.clickWithTrigger {
                        val dialog = NormalDialog.create(activity!!, "未绑定手机号", null, "确定", "取消") as NormalDialog
                    }
                }

                if (false) {//it.is_set_password
                    resetPwdItem.setRightText("已设置")
                    resetPwdItem.clickWithTrigger {
                        openPage(this@AccountInfoFragment, Config.ROUTER_MINE_EDIT_CHANGE_PWD_FRAGMENT, R.id.container_framelayout)
                    }
                } else {
                    resetPwdItem.setRightText("去设置")
                    resetPwdItem.clickWithTrigger {
                        val dialog = NormalDialog.create(activity!!, "未绑定手机号", null, "确定", "取消") as NormalDialog
                        dialog.positiveListener = object : IPositiveButtonDialogListener {
                            override fun onPositiveButtonClicked(requestCode: Int) {
                                val bundle = Bundle()
                                bundle.putInt("change_phone_type", Config.SET_PHONE_NUM)
                                Router.build(Config.ROUTER_MINE_EDIT_VERIFY_CODE_FRAGMENT).with(bundle).goFragment(this@AccountInfoFragment, R.id.container_framelayout)
                            }
                        }
                    }
                }

                magicBoxItem.setRightText(it!!.invite_code.toString())

            }
        }
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
                            User.currentUser.avatar = model.selectList[0].compressPath
                            iv_user_icon.loadCircle(User.currentUser.avatar)
                            // 将头像添加到缓存
//                                    Settings.setAvatar(User.currentUser.avatar)
                            Settings.create().avatar = User.currentUser.avatar
                            RxBus.get().post(RefreshUserInfoEvent())
                        }
//                        dialogFragment?.dismissAllowingStateLoss()
                    }
                }
            }
        }
    }


}