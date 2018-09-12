package com.viet.news.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.viet.news.core.config.Config

/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Date 2018/9/12
 * @Description
 */
@Entity(tableName = Config.FAVORITE_TABLE_NAME)
class FavoriteEntity(
        @PrimaryKey()
        var id: String = "",
        var name: String? = "",
        var description: String? = "",
        var url: String? = "",
        var avatarUrl: String? = "",
        var img1Url: String? = "",
        var img2Url: String? = "",
        var img3Url: String? = "",
        var readNo: String? = "",
        var likeNo: String? = ""
)