package com.viet.task.fragment

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.safframework.log.L
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.BaseFragment
import com.viet.task.R
import com.viet.task.adapter.TaskAdapter
import com.viet.task.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_task.*


/**
 * @Description 任务
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 11:13 AM
 * @Version 1.0.0
 */
class TaskFragment : BaseFragment() {

    private val model: TaskViewModel by viewModelDelegate(TaskViewModel::class)

    private val adapter: TaskAdapter = TaskAdapter()

    private val callbackManager: CallbackManager = CallbackManager.Factory.create()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun initView(view: View) {
        recycler_view_task.adapter = adapter
        recycler_view_task.layoutManager = LinearLayoutManager(activity, OrientationHelper.VERTICAL, false)
        initData()

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                result?.let {
                    it.accessToken.toString()
                    L.e("onSuccess,userId = ${it.accessToken.userId}" +
                            ",token = ${it.accessToken.token}" +
                            ",applicationId = ${it.accessToken.applicationId}" +
                            ",isExpired = ${it.accessToken.isExpired}")
                    toast("onSuccess,result = ${it.accessToken}").show()
                }
            }

            override fun onCancel() {
                L.e("onCancel")
                toast("onCancel").show()
            }

            override fun onError(error: FacebookException?) {
                L.e("onError , error=$error")
                toast("onError , error=$error").show()
            }
        })

        btn_test.setOnClickListener {
            val accessToken = AccessToken.getCurrentAccessToken()
            val isLoggedIn = accessToken != null && !accessToken.isExpired
            if (isLoggedIn) {
                LoginManager.getInstance().logOut()
            } else {
                LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"))
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initData() {
        model.getTaskGroupList().observe(this, Observer {
            adapter.setData(it?.generateTaskList())
        })
    }
}
