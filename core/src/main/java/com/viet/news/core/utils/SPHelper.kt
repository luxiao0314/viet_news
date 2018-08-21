package com.viet.news.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.viet.news.core.BaseApplication
import java.util.*


/**
* @author Aaron
* @email aaron@magicwindow.cn
* @date 14/12/25 7:49 PM
* @description
*/
class SPHelper private constructor(private val context: Context?) {
    private val SP_KEY_DEFAULT = "persistent_data"
    private val TAG = "SPHelper"

    private val sp: SharedPreferences?
        get() {
            if (context == null) {
                return null
            }

            try {
                return context.getSharedPreferences(SP_KEY_DEFAULT, preferenceMode)
            } catch (ignored: OutOfMemoryError) {

            }

            return null
        }


    init {
        preferenceMode = Context.MODE_MULTI_PROCESS

    }

    fun putBoolean(key: String, value: Boolean) {
        if (sp == null) {
            return
        }
        val editor = sp!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        var value = defaultValue
        if (sp != null) {
            value = sp!!.getBoolean(key, defaultValue)

        }
        return value
    }

    fun getBoolean(key: String): Boolean {
        var value = false
        if (sp != null) {
            value = sp!!.getBoolean(key, false)

        }
        return value
    }

    fun putInt(key: String, value: Int) {
        if (sp == null) {
            return
        }
        val editor = sp!!.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        var value = defaultValue
        if (sp != null) {
            value = sp!!.getInt(key, defaultValue)

        }
        return value
    }

    fun getInt(key: String): Int {
        var value = 0
        if (sp != null) {
            value = sp!!.getInt(key, 0)

        }
        return value
    }

    fun put(key: String, value: String?) {
        if (sp == null) {
            return
        }
        val editor = sp!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun remove(key: String) {
        if (sp == null) {
            return
        }
        val editor = sp!!.edit()
        editor.remove(key)
        editor.apply()
    }

    operator fun get(key: String, defaultValue: String): String {
        var value: String = defaultValue
        if (sp != null) {
            value = sp!!.getString(key, defaultValue)

        }
        return value
    }

    operator fun get(key: String): String {
        var value: String = ""
        if (sp != null) {
            value = sp!!.getString(key, "")

        }
        return value
    }

    fun putString(key: String, value: String?) {
        if (sp == null) {
            return
        }
        val editor = sp!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getSet(key: String): Set<String> {
        var value: Set<String> = HashSet()
        if (sp != null) {
            value = sp!!.getStringSet(key, value)

        }
        return value
    }

    fun getSet(key: String, defaultValue: Set<String>): Set<String> {
        var value: Set<String> = defaultValue
        if (sp != null) {
            value = sp!!.getStringSet(key, defaultValue)

        }
        return value
    }

    fun putSet(key: String, set: Set<String>) {
        if (sp == null) {
            return
        }
        val editor = sp!!.edit()
        editor.putStringSet(key, set)
        editor.apply()
    }

    fun addSet(key: String, setValue: String) {
        if (sp == null) {
            return
        }
        val editor = sp!!.edit()

        val set = sp!!.getStringSet(key, HashSet())
        set!!.add(setValue)
        editor.putStringSet(key, set)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String): String {
        var value: String = defaultValue
        if (sp != null) {
            value = sp!!.getString(key, defaultValue)

        }
        return value
    }

    fun getString(key: String): String {
        var value: String = ""
        if (sp != null) {
            value = sp!!.getString(key, "")
        }
        return value
    }

    fun putLong(key: String, value: Long?) {
        if (sp == null) {
            return
        }
        val editor = sp!!.edit()
        editor.putLong(key, value!!)
        editor.apply()
    }

    fun getLong(key: String): Long? {
        var value: Long? = 0L
        if (sp != null) {
            value = sp!!.getLong(key, 0L)

        }
        return value
    }

    fun getLong(key: String, defaultValue: Long?): Long? {
        var value = defaultValue
        if (sp != null) {
            value = sp!!.getLong(key, defaultValue!!)

        }
        return value
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var defaultInstance: SPHelper? = null
        private var preferenceMode = Context.MODE_PRIVATE

        val TRACKING_DEVICE_ID = "device_id"         //device_id

        fun create(context: Context= BaseApplication.instance): SPHelper {
            if (defaultInstance == null) {
                synchronized(SPHelper::class.java) {
                    if (defaultInstance == null) {
                        defaultInstance = SPHelper(context.applicationContext ?:context)
                    }
                }
            }
            return defaultInstance!!
        }
    }
}
