package com.viet.news.core.domain.request

import java.io.Serializable

/**
 * @Description
 * @Author lucio
 * @Email xiao.lu@magicwindow.cn
 * @Date 12/09/2018 12:10 AM
 * @Version
 */
class List4ChannelParams:Serializable {
    var page_number:Int = 0
    var page_size:Int = 0
    var channel_id:String? = ""
}