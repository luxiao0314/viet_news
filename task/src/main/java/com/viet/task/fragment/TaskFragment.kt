package com.viet.task.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ui.RealVisibleHintBaseFragment
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
class TaskFragment : RealVisibleHintBaseFragment() {

    private val model: TaskViewModel by viewModelDelegate(TaskViewModel::class)

    private val adapter: TaskAdapter = TaskAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun initView(view: View) {
        recycler_view_task.adapter = adapter
        recycler_view_task.layoutManager = LinearLayoutManager(activity, OrientationHelper.VERTICAL, false)
        loadTaskListData()
        initEvent()

    }

    private fun initEvent() {
        refreshLayout.setOnRefreshListener {
            loadTaskListData()
            it.finishRefresh()
        }
    }

    override fun onFragmentFirstVisible() {
        refreshLayout.autoRefresh()
    }

    private fun loadTaskListData() {
        model.getTaskGroupList().observe(this, Observer {
            adapter.setData(it?.generateTaskList())
        })
    }
}
