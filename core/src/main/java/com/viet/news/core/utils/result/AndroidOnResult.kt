package com.viet.news.core.utils.result

import android.app.Activity
import android.content.Intent
import io.reactivex.Observable

/**
 * @Description 简化onActivityresult回调
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 25/09/2018 11:12 AM
 * @Version
 */
class AndroidOnResult(activity: Activity?) {

    private val androidOnResultFragment: AndroidOnResultFragment

    init {
        androidOnResultFragment = getAndroidOnResultFragment(activity)
    }

    private fun getAndroidOnResultFragment(activity: Activity?): AndroidOnResultFragment {
        val tag = "AndroidOnResult"
        var resultFragment: AndroidOnResultFragment? = activity?.fragmentManager?.findFragmentByTag(tag) as AndroidOnResultFragment
        if (resultFragment == null) {
            resultFragment = AndroidOnResultFragment()
            val fragmentManager = activity.fragmentManager
            fragmentManager.beginTransaction().add(resultFragment, tag).commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
        return resultFragment
    }

    fun startForResult(intent: Intent): Observable<ActivityResultInfo> = androidOnResultFragment.startForResult(intent)

    fun startForResult(clazz: Class<*>): Observable<ActivityResultInfo> = startForResult(Intent(androidOnResultFragment.activity, clazz))

    fun startForResult(intent: Intent, callback: (resultCode: Int, data: Intent?) -> Unit) {
        androidOnResultFragment.startForResult(intent, callback)
    }

    fun startForResult(clazz: Class<*>, callback: (resultCode: Int, data: Intent?) -> Unit) {
        val intent = Intent(androidOnResultFragment.activity, clazz)
        startForResult(intent, callback)
    }
}

class ActivityResultInfo(var resultCode: Int, var data: Intent?)