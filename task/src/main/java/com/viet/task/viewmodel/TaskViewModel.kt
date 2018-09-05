package com.viet.task.viewmodel

import android.arch.lifecycle.LiveData
import com.viet.news.core.domain.response.NewsResponse
import com.viet.news.core.domain.response.TaskResponse
import com.viet.news.core.utils.FileUtils
import com.viet.news.core.viewmodel.BaseViewModel

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 1:37 PM
 * @Version
 */
class TaskViewModel: BaseViewModel() {


    fun getTaskGroupList(): LiveData<TaskResponse> {
        return FileUtils.handleVirtualData(TaskResponse::class.java)
    }
}