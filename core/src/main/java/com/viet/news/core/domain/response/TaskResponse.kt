package com.viet.news.core.domain.response

class TaskResponse {
    companion object {
        const val TITLE = 1
        const val RANK = 2
        const val TASK = 3
    }

    /**
     * currentRange : 111
     * taskGroups : [{"taskGroupName":"MB钻任务","tasks":[{"taskName":"邀请新用户","taskDesc":"更能获得相关收益","taskType":"invite","taskIcon":"http://img.sccnn.com/bimg/338/24556.jpg","taskScore":2,"taskStatus":{"totalScore":0,"currentScore":0,"message":"已邀请100名"}},{"taskName":"首次绑定手机号","taskDesc":"一分耕耘一分收获","taskType":"bind_mobile_phone","taskIcon":"http://img.sccnn.com/bimg/338/24556.jpg","taskScore":20,"taskStatus":{"totalScore":0,"currentScore":0,"message":""}}]},{"taskGroupName":"POWER任务","tasks":[{"taskName":"每天阅读文章","taskDesc":"每天最多获得200power","taskType":"reading","taskIcon":"http://img.sccnn.com/bimg/338/24556.jpg","taskScore":2,"taskStatus":{"totalScore":200,"currentScore":20,"message":""}},{"taskName":"内容创作奖励","taskDesc":"一分耕耘一分收获","taskType":"neirongchuangzuo","taskIcon":"http://img.sccnn.com/bimg/338/24556.jpg","taskScore":-1,"taskStatus":{"totalScore":0,"currentScore":0,"message":"内测中..."}},{"taskName":"点赞文章","taskDesc":"每天最多获得50power","taskType":"dianzan","taskIcon":"http://img.sccnn.com/bimg/338/24556.jpg","taskScore":5,"taskStatus":{"totalScore":50,"currentScore":20,"message":""}},{"taskName":"分享文章给好友","taskDesc":"每天最多获得20power","taskType":"share","taskIcon":"http://img.sccnn.com/bimg/338/24556.jpg","taskScore":5,"taskStatus":{"totalScore":20,"currentScore":20,"message":""}}]}]
     */

    var currentRange: Int = 0
    var taskGroups: List<TaskGroupsBean>? = null

    class TaskGroupsBean {
        /**
         * taskGroupName : MB钻任务
         * tasks : [{"taskName":"邀请新用户","taskDesc":"更能获得相关收益","taskType":"invite","taskIcon":"http://img.sccnn.com/bimg/338/24556.jpg","taskScore":2,"taskStatus":{"totalScore":0,"currentScore":0,"message":"已邀请100名"}},{"taskName":"首次绑定手机号","taskDesc":"一分耕耘一分收获","taskType":"bind_mobile_phone","taskIcon":"http://img.sccnn.com/bimg/338/24556.jpg","taskScore":20,"taskStatus":{"totalScore":0,"currentScore":0,"message":""}}]
         */

        var taskGroupName: String? = null
        var tasks: List<TasksBean>? = null

        class TasksBean {
            /**
             * taskName : 邀请新用户
             * taskDesc : 更能获得相关收益
             * taskType : invite
             * taskIcon : http://img.sccnn.com/bimg/338/24556.jpg
             * taskScore : 2
             * taskStatus : {"totalScore":0,"currentScore":0,"message":"已邀请100名"}
             */

            var taskName: String? = null
            var taskDesc: String? = null
            var taskType: String? = null
            var type: Int = 0
            var taskIcon: String? = null
            var taskScore: Int = 0
            var taskStatus: TaskStatusBean? = null

            class TaskStatusBean {
                /**
                 * totalScore : 0
                 * currentScore : 0
                 * message : 已邀请100名
                 */

                var totalScore: Int = 0
                var currentScore: Int = 0
                var message: String? = null
            }
        }
    }

    fun generateTaskList(): List<TaskGroupsBean.TasksBean> {
        val list = mutableListOf<TaskGroupsBean.TasksBean>()
        taskGroups?.let {
            for (index in it.indices) {
                val taskGroupBean = it[index]
                val taskBean = TaskGroupsBean.TasksBean()
                taskBean.type = TITLE
                taskBean.taskName = taskGroupBean.taskGroupName
                list.add(taskBean)
                if (index == 0 && currentRange > 0) {
                    val taskRankBean = TaskGroupsBean.TasksBean()
                    taskRankBean.type = RANK
                    taskRankBean.taskName = "当前周排名$currentRange"
                    list.add(taskRankBean)
                }
                taskGroupBean.tasks?.let {
                    for (task in it) {
                        task.type = TASK
                        list.add(task)
                    }
                }
            }
        }
        return list

    }
}