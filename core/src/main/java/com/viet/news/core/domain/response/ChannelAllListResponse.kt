package com.viet.news.core.domain.response

import java.io.Serializable

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 2018/9/10 上午10:49
 * @Version
 */
class ChannelAllListResponse(var followChannelList: ArrayList<ChannelList>,
                             var unFollowChannelList: ArrayList<ChannelList>) : Serializable
