package com.viet.news.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.viet.news.core.config.Config

/**
 * Created by abhinav.sharma on 04/11/17.
 */
@Database(entities = [SourceEntity::class], version = 1)
abstract class NewsDBHelper : RoomDatabase() {
    abstract fun getSourceDao(): SourceDao

    companion object {
        private var db: NewsDBHelper? = null

        fun getInstance(context: Context): NewsDBHelper {
            if (db == null) {
                db = Room.databaseBuilder(context, NewsDBHelper::class.java, Config.DB_NAME).build()
            }
            return db!!
        }
    }

}