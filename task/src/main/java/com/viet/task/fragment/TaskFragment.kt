package com.viet.task.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.viet.news.core.ui.BaseFragment
import com.viet.task.R
import com.viet.task.viewmodel.TaskViewModel

/**
 * @Description 任务
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 11:13 AM
 * @Version 1.0.0
 */
class TaskFragment : BaseFragment() {

    private lateinit var model: TaskViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_task, container, false)
        model = ViewModelProviders.of(this).get(TaskViewModel::class.java)
        return view
    }

    override fun initView(view: View) {
    }
}