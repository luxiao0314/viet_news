package com.viet.news.core.ext

import com.viet.news.core.utils.EncryptUtils
import io.reactivex.Maybe
import org.json.JSONObject

/***
 *@package cn.magicwindow.core.ext
 *@useage
 *@author chlong.xu
 *@date 2018-07-03
 **/

/***
 * sign 签名
 * @receiver String  需要签名字符串
 * @return String 签名后字符串
 */
fun String.sign(type: Int = 1): Maybe<String> = Maybe.fromCallable { EncryptUtils.encryptPayPasswordWithSalt(this, type) }

fun <T> isBlank(var0: T?): Boolean {
    when (var0) {
        null -> return true
        is String -> return (var0 as String).length == 0
        else -> {
            if (var0 is List<*>) {
                if ((var0 as List<*>).size == 0) {
                    return true
                }
            } else if (var0 is Map<*, *>) {
                if ((var0 as Map<*, *>).size == 0) {
                    return true
                }
            } else if (var0 is JSONObject) {
                if ((var0 as JSONObject).length() == 0) {
                    return true
                }
            } else if (var0 is Array<*> && (var0 as Array<*>).size == 0) {
                return true
            }

            return false
        }
    }
}

fun <T> isNotBlank(var0: T): Boolean = !isBlank(var0)

fun <T> isNotBlanks(vararg var0: Any): Boolean {
    if (var0 == null) {
        return false
    } else {
        val var2 = var0.size

        for (var3 in 0 until var2) {
            val var4 = var0[var3]
            if (isBlank(var4)) {
                return false
            }
        }
        return true
    }
}

fun checkNotNull(var0: Any?, var1: String) {
    if (var0 == null) {
        throw NullPointerException(var1)
    }
}

fun isJsonString(var0: String): Boolean = isNotBlank(var0) && var0.startsWith("{")