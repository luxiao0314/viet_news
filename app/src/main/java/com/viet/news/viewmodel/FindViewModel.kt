package com.viet.news.viewmodel

import cn.magicwindow.channelwidget.entity.ChannelBean
import com.viet.news.core.viewmodel.BaseViewModel

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 1:37 PM
 * @Version
 */
class FindViewModel : BaseViewModel() {
    var dataList = arrayListOf<ChannelBean>()
    val myStrs = listOf("推荐", "热点", "军事", "图片", "社会", "娱乐", "科技", "体育", "深圳", "财经")
    val recStrs = listOf("设计", "天文", "美食", "星座", "历史", "消费维权", "体育", "明星八卦")

    init {
        for (i in 0..9) {
            val channelBean = ChannelBean()
            channelBean.tabName = myStrs[i]
            channelBean.tabType = if (i == 0) 0 else if (i == 1) 1 else 2
            dataList.add(channelBean)
        }
    }
}