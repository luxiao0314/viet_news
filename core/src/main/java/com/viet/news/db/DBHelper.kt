package com.viet.news.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.viet.news.core.config.Config

/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Date 2018/9/12
 * @Description
 */
@Database(entities = [FavoriteEntity::class], version = 1)
abstract class DBHelper : RoomDatabase() {
    abstract fun getSourceDao(): SourceDao

    companion object {
        private var db: DBHelper? = null

        fun getInstance(context: Context): DBHelper {
            if (db == null) {
                db = Room.databaseBuilder(context, DBHelper::class.java, Config.DB_NAME).build()
            }
            return db!!
        }
    }

}