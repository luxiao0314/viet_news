package com.viet.task.adapter

import android.annotation.SuppressLint
import com.bumptech.glide.Glide
import com.viet.news.core.domain.response.TaskResponse
import com.viet.news.core.ui.BaseAdapter
import com.viet.task.R
import kotlinx.android.synthetic.main.cell_task_item.view.*
import kotlinx.android.synthetic.main.cell_task_ranking.view.*
import kotlinx.android.synthetic.main.cell_task_title.view.*
import javax.inject.Inject

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 5:49 PM
 * @Version
 */
class TaskAdapter @Inject constructor() : BaseAdapter<TaskResponse.TaskGroupsBean.TasksBean>() {


    override fun getItemViewType(position: Int): Int {
        return getData()[position].type

    }

    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            TaskResponse.TITLE -> R.layout.cell_task_title
            TaskResponse.RANK -> R.layout.cell_task_ranking
            else -> R.layout.cell_task_item
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolderImpl(holder: BaseViewHolder, position: Int, t: TaskResponse.TaskGroupsBean.TasksBean) {
        with(holder.itemView) {
            when (getItemViewType(position)) {
                TaskResponse.TITLE -> {
                    //任务组名称
                    tv_task_title_name.text = t.taskName
                }
                TaskResponse.RANK -> {
                    //排名
                    tv_rank.text = t.taskName
                }
                else -> {
                    //具体任务
                    Glide.with(context).load(t.taskIcon).into(iv_task_icon)
                    tv_task_name.text = t.taskName
                    if (t.taskScore > 0) {
                        tv_task_score.text = "+${t.taskScore}"
                    }
                    tv_task_desc.text = t.taskDesc
                    if (t.taskStatus?.message.isNullOrEmpty().not()) {
                        tv_task_msg.text = t.taskStatus?.message
                    }

                    btn_right.text = t.taskType

                }
            }
        }
    }
}