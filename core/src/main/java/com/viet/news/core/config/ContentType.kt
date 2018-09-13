package com.viet.news.core.config

import com.luck.picture.lib.entity.LocalMedia
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URLConnection
import java.util.*

/**
 * @Description retrofit 上传管理类
 * @Author lucio
 * @Email xiao.lu@magicwindow.cn
 * @Date 06/09/2017 7:56 PM
 * @Version 1.0.0
 */
object ContentType {
    private val MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8")
    private val MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8")
    private val MEDIA_TYPE_HTML = MediaType.parse("application/xml;charset=utf-8")
    private val MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream")

    fun postJson(json: String): RequestBody {
        return RequestBody.create(MEDIA_TYPE_JSON, json)
    }

    fun postString(content: String): RequestBody {
        return RequestBody.create(MEDIA_TYPE_PLAIN, content)
    }

    fun getRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value)
    }

    fun getMultipart(paths: List<String>): HashMap<String, RequestBody> {
        val files = HashMap<String, RequestBody>()
        for (path in paths) {
            val file = File(path)
            val body = RequestBody.create(guessMimeType(file.name), file)
            files["file[]\"; filename=\"" + file.name] = body
        }
        return files
    }

    /**
     * 单文件上传
     * @param path
     * @return
     */
    fun getPart(path: String): MultipartBody.Part {
        val file = File(path)
        //构建requestbody
        val requestFile = RequestBody.create(guessMimeType(file.name), file) //Content-Type:multipart/form-data
        //RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file); //Content-Type:multipart/form-data
        //将resquestbody封装为MultipartBody.Part对象,既定格式
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    /**
     * 多图片上传 传string:路径
     *
     * @param paths
     * @return
     */
    fun getMultiPart(paths: List<String>): List<MultipartBody.Part> {
        val files = ArrayList<MultipartBody.Part>()
        for (path in paths) {
            val file = File(path)
            //构建requestbody
            val requestFile = RequestBody.create(guessMimeType(file.name), file) //Content-Type:multipart/form-data
            //将resquestbody封装为MultipartBody.Part对象,既定格式
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)   //KEY:file
            files.add(body)
        }
        return files
    }

    /**
     * 多图片上传
     *
     * @param paths
     * @return
     */
    fun getMultiParts(paths: List<LocalMedia>): List<MultipartBody.Part> {
        val files = ArrayList<MultipartBody.Part>()
        for (i in paths.indices) {
            val path = paths[i]
            val file = File(path.compressPath)
            //构建requestbody
            val requestFile = RequestBody.create(guessMimeType(file.name), file) //Content-Type:multipart/form-data
            //将resquestbody封装为MultipartBody.Part对象,既定格式
            val body = MultipartBody.Part.createFormData("files$i", file.name, requestFile)   //KEY:file
            files.add(body)
        }
        return files
    }

    /**
     * 根据路径自己去匹配对应的content-type
     *
     * @param path
     * @return
     */
    private fun guessMimeType(path: String): MediaType? {
        var path = path
        val fileNameMap = URLConnection.getFileNameMap()
        path = path.replace("#", "")   //解决文件名中含有#号异常的问题
        var contentType: String? = fileNameMap.getContentTypeFor(path)
        if (contentType == null) {
            contentType = "application/octet-stream"
        }
        return MediaType.parse(contentType)
    }
}
