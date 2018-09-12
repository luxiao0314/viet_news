package com.viet.news.core.domain.request

import java.io.Serializable

class ListParams : Serializable {
    var page_number: Int = 0
    var page_size: Int = 0
    var user_id: String? = ""
    var channel_id: String? = ""
}