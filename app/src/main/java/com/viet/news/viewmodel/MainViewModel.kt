package com.viet.news.viewmodel

import com.flyco.tablayout.listener.CustomTabEntity
import com.viet.mine.fragment.MineFragment
import com.viet.news.R
import com.viet.news.core.domain.TabEntity
import com.viet.news.core.ui.App
import com.viet.news.core.viewmodel.BaseViewModel
import com.viet.news.ui.fragment.FindFragment
import com.viet.news.ui.fragment.NewsFragment
import com.viet.task.fragment.TaskFragment
import java.util.*

class MainViewModel :BaseViewModel(){

    val titles = mutableListOf(App.instance.getString(R.string.title_find), App.instance.getString(R.string.title_follow), App.instance.getString(R.string.title_task), App.instance.getString(R.string.title_mine))
    val fragments = mutableListOf(FindFragment(), NewsFragment(), TaskFragment(), MineFragment())
    private val mIconUnselectIds = arrayOf(R.drawable.ic_find_unselect, R.drawable.ic_follow_unselect, R.drawable.ic_task_unselect, R.drawable.ic_mine_unselect)
    private val mIconSelectIds = arrayOf(R.drawable.ic_find_select, R.drawable.ic_follow_select, R.drawable.ic_task_select, R.drawable.ic_mine_select)
    val tabEntities = ArrayList<CustomTabEntity>()

    init {
        for (i in titles.indices) {
            tabEntities.add(TabEntity(titles[i], mIconSelectIds[i], mIconUnselectIds[i]))
        }
    }

}