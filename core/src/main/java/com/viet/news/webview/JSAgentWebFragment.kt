package com.viet.news.webview

import android.os.Bundle
import android.view.View

/**
 * @Description js交互fragment
 * @Author lucio
 * @Email xiao.lu@magicwindow.cn
 * @Date 31/01/2018 2:28 PM
 * @Version 1.0.0
 */
class JSAgentWebFragment : AgentWebFragment() {

    private var androidInterface: AndroidInterface? = null


    override fun initView(view: View) {
        initView(view)
        if (mAgentWeb != null && model.injectedName.value != null) {
            androidInterface = AndroidInterface(mAgentWeb!!, context!!, this, model)    //注入对象
            mAgentWeb?.jsInterfaceHolder?.addJavaObject(model.injectedName.value, androidInterface)
        }
    }

    /**
     * h5跳登录之后,回来再reload页面
     */
    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if (model.tokenParams.value?.activityId.isNullOrEmpty()) {
                mAgentWeb?.urlLoader?.reload()
            } else {
                model.activityRedirect(activity!!) { mAgentWeb?.urlLoader?.reload() }
            }
        }
    }*/

    override fun onDestroy() {
        androidInterface?.onDestroy()
        super.onDestroy()
    }

    companion object {
        fun getInstance(bundle: Bundle?): JSAgentWebFragment {
            val mJSAgentWebFragment = JSAgentWebFragment()
            if (bundle != null)
                mJSAgentWebFragment.arguments = bundle
            return mJSAgentWebFragment
        }
    }
}
