package com.viet.news.core.ext

import android.support.annotation.IdRes
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.chenenyu.router.IRouter
import com.chenenyu.router.Router
import com.viet.news.core.R
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.ui.BaseFragment


/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 2018/4/7 17:44
 * @description Ui切换动画
 */

/**
 * 带Anim的build
 * @param path String
 * @return IRouter
 */
fun routerWithAnim(path: String): IRouter {
    return Router.build(path).anim(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
}

/**
 * 进入Fragment
 * @receiver IRouter
 * @param activity BaseActivity
 * @param containerViewId Int
 * @param addToBackStack Boolean 默认为false
 * @param backStackName String?
 */
fun IRouter.goFragment(activity: BaseActivity, @IdRes containerViewId: Int, addToBackStack: Boolean = false, backStackName: String? = null) {
    val fm = activity.supportFragmentManager
    val transaction = fm.beginTransaction()
    val fragment = getFragment(activity) as BaseFragment
    transaction.replace(containerViewId, fragment)
    if (addToBackStack) transaction.addToBackStack(backStackName)
    transaction.commit()
}

/**
 * 进入Fragment
 * @receiver IRouter
 * @param targetFragment BaseFragment
 * @param containerViewId Int
 * @param addToBackStack Boolean 默认为true
 * @param backStackName String?
 */
fun IRouter.goFragment(targetFragment: BaseFragment, @IdRes containerViewId: Int, addToBackStack: Boolean = true, backStackName: String? = null) {
    val fm = targetFragment.fragmentManager
    val transaction = fm?.fragmentTransaction()
    val fragment = getFragment(targetFragment.context!!) as BaseFragment
    transaction?.replace(containerViewId, fragment)
    if (addToBackStack) transaction?.addToBackStack(backStackName)
    transaction?.commit()
}

/**
 * 带Anim的FragmentTransaction
 * @receiver FragmentManager
 * @return FragmentTransaction
 */
fun FragmentManager.fragmentTransaction(): FragmentTransaction {
    return beginTransaction().setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right)
}

/**
 * 带Anim的结束finish
 * @receiver BaseActivity
 */
fun BaseActivity.finishWithAnim(enterAnim: Int = R.anim.slide_in_from_left, exitAnim: Int = R.anim.slide_out_to_right) {
    finish()
    overridePendingTransition(enterAnim, exitAnim)
}
