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
@Entity(tableName = Config.NULL_TABLE_NAME)
class NullEntity(
        @PrimaryKey()
        var id: String = ""
)