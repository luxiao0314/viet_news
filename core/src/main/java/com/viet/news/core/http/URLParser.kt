package com.viet.news.core.http

import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.*

/**
 * Created by tony on 2018/2/3.
 */
class URLParser
/**
 * http://user:password@host:port/aaa/bbb;xxx=xxx?eee=ggg&fff=ddd&fff=lll
 *
 * @throws MalformedURLException
 */
@Throws(MalformedURLException::class)
@JvmOverloads constructor(url: String?, charset: String? = "utf-8") {

    var host: String? = null
        private set

    var port: Int? = null
        private set

    var protocol: String? = null
        private set

    // use LinkedHashMap to keep the order of items
    private var params = LinkedHashMap<String, MutableList<String>>()

    var path: String? = null
        private set

    var userInfo: String? = null
        private set

    var query: String? = null
        private set

    var charset: String? = null
        private set

    private var hasDomain = true

//    val simple: Map<String, String>
//        get() {
//            val map = HashMap<String, String>()
//            for (name in this.params.keys) {
//                val value = this.params[name]?.get(0)
//                map[name] = encode(value)
//            }
//            return map
//        }

    init {
        if (charset != null && !Charset.isSupported(charset)) {
            throw IllegalArgumentException("charset is not supported: $charset")
        }
        val u: URL
        when {
            url == null -> {
                hasDomain = false
                u = URL("http://dummy")
            }
            url.matches("\\w+[:][/][/].*".toRegex()) -> {
                hasDomain = true
                u = URL(url)
            }
            else -> {
                hasDomain = false
                u = URL("http://dummy" + if (url.startsWith("/")) url else "/$url")
            }
        }

        this.charset = charset
        url?.let {
            if (hasDomain) {
                this.protocol = u.protocol
                this.host = u.host
                this.port = u.port
                if (this.port != null && this.port == -1) {
                    this.port = null
                }
                this.userInfo = u.userInfo
            }
            this.path = u.path
            this.query = u.query
            this.params = parseQueryString(substringAfter(it, "?"))
        }


    }

    fun addPath(pathAdded: String?, encode: Boolean = true): URLParser {
        if (pathAdded != null) {
            var pathTemp: String = pathAdded
            if (pathAdded.startsWith("/"))
                pathTemp = pathAdded.substring(1)

            if (pathAdded.endsWith("/"))
                pathTemp = pathAdded.substring(0, pathAdded.length - 1)

            //encode路径
            if (encode) {
                pathTemp = encode(pathTemp)
            }

            if (path != null && path!!.endsWith("/")) {
                path = "$path$pathTemp/"
            } else if (path != null) {
                path = "$path/$pathTemp"
            } else {
                path = pathTemp
            }
        }

        return this
    }

    fun addParam(name: String?, value: String?, encode: Boolean = true): URLParser {
        addParams(name, Arrays.asList(value ?: ""), encode)
        return this
    }

    fun addParams(name: String?, values: List<String>?, encode: Boolean = true): URLParser {
        if (name == null || values == null) {
            return this
        }
        val list = getOrCreate(params, name)
        list?.let {
            for (value in values) {
                it.add(if (encode) encode(value) else value)
            }
        }

        return this
    }

    fun removeParams(name: String?): URLParser {
        if (name == null) {
            return this
        }
        this.params.remove(name)
        return this
    }

    fun updateParams(name: String?, vararg values: String, encode: Boolean = true) {
        if (name == null)
            return
        else {
            if (values.isEmpty()) {
                throw IllegalArgumentException("values should not be empty")
            }

            val list = getOrCreate(params, name)

            list?.let {
                it.clear()
                for (value in values) {
                    it.add(if (encode) encode(value) else value)
                }
            }

        }

    }

    fun getRawParams(name: String?): List<String>? {
        if (name != null) {
            return this.params[name]

        } else {
            return null
        }
    }

    fun getRawParam(name: String): String? {
        val params = getRawParams(name)
        return if (params == null) null else params[0]
    }

    @Throws(UnsupportedEncodingException::class)
    fun getParam(name: String): String? {
        val value = getRawParam(name)
        return if (value == null) null else decode(value)
    }

    fun getParams(name: String): List<String>? {
        val rawParams = getRawParams(name) ?: return null
        val params = ArrayList<String>()
        for (value in rawParams) {
            params.add(decode(value))
        }
        return params
    }

    fun createQueryString(): String {
        if (this.params.isEmpty()) {
            return ""
        }
        val sb = StringBuilder()
        for (name in this.params.keys) {
            val values = this.params[name]
            if (values != null)
                for (value in values) {
                    if (sb.isNotEmpty()) {
                        sb.append("&")
                    }
                    sb.append(name).append("=").append(value)
                }
        }
        return sb.toString()
    }

    override fun toString(): String {
        val sb = StringBuilder()
        if (this.protocol != null) {
            sb.append(this.protocol).append("://")
        }
        if (this.userInfo != null) {
            sb.append(this.userInfo).append("@")
        }
        if (this.host != null) {
            sb.append(host)
        }
        if (this.port != null) {
            sb.append(":").append(this.port!!)
        }
        sb.append(this.path)
        val query = createQueryString()
        if (query.trim { it <= ' ' }.isNotEmpty()) {
            sb.append("?").append(query)
        }

        return sb.toString()
    }

    private fun decode(value: String?): String {
        return if (value != null) {
            try {
                if (charset == null) value else URLDecoder.decode(value, charset)
            } catch (e: UnsupportedEncodingException) {
                throw RuntimeException(e)
            }
        } else {
            ""
        }
    }

    private fun encode(value: String?): String {
        return if (value != null) {
            try {
                if (charset == null) value else URLEncoder.encode(value, charset)
            } catch (e: UnsupportedEncodingException) {
                throw RuntimeException(e)
            }
        } else {
            ""
        }

    }

    private fun getOrCreate(map: MutableMap<String, MutableList<String>>, name: String?): MutableList<String>? {
        if (name != null) {
            var list: MutableList<String>? = map[name]
            if (list == null) {
                list = ArrayList()
                map[name] = list
            }
            return list
        } else {
            return null
        }

    }


    private fun parseQueryString(query: String): LinkedHashMap<String, MutableList<String>> {
        val params = LinkedHashMap<String, MutableList<String>>()
        if (query.isBlank()) {
            return params
        }
        val items = query.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (item in items) {
            val name = substringBefore(item, "=")
            val value = substringAfter(item, "=")
            val values = getOrCreate(params, name)
            values?.add(value)
        }
        return params
    }

    private fun substringBefore(str: String, sep: String): String {
        val index = str.indexOf(sep)
        return if (index == -1) "" else str.substring(0, index)
    }

    private fun substringAfter(str: String, sep: String): String {
        val index = str.indexOf(sep)
        return if (index == -1) "" else str.substring(index + 1)
    }
}