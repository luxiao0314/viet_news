package com.viet.news.core.utils

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import android.os.StatFs
import android.text.TextUtils
import com.google.gson.Gson
import com.viet.news.core.ui.App
import java.io.*
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 10/03/2018 19:11
 * @description
 */
object FileUtils {
    /**
     * 文件保存路径
     */
    private const val FILE_SAVE_PATH = "mw_cache"
    private val DO_NOT_VERIFY = HostnameVerifier { hostname, session -> true }
    private const val TAG = "FileUtils"


    /**
     * 获取手机内部总的存储空间
     *
     * @return
     */
    //        DebugLog.e("aaron totalBlocks:"+totalBlocks+", blockSize:"+blockSize+", memory:"+totalBlocks * blockSize);
    val totalInternalMemorySize: Long
        get() {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            var blockSize: Long = 0
            var totalBlocks: Long = 0

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.blockSizeLong
                totalBlocks = stat.blockCountLong

            } else {
                blockSize = stat.blockSize.toLong()
                totalBlocks = stat.blockCount.toLong()
            }
            return totalBlocks * blockSize
        }

    fun getMWCachePath(context: Context): String {
        return getDiskCacheDir(context, FILE_SAVE_PATH)!!.toString() + File.separator
    }

    /**
     * 加载系统本地图片
     */
    fun loadImage(url: String, filename: String): Bitmap? {
        try {
            val fis = FileInputStream(url + filename)
            return BitmapFactory.decodeStream(fis)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * Trust every server - dont check for any certificate
     */
    private fun trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }

            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        })

        // Install the all-trusting trust manager
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, java.security.SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun hasExternalCache(context: Context): Boolean {
        return (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
                && checkPermissions(context, "android.permission.WRITE_EXTERNAL_STORAGE")
                && context.externalCacheDir != null)
    }

    @JvmStatic
    fun checkPermissions(context: Context?, permission: String): Boolean {
        val localPackageManager = context!!.applicationContext.packageManager
        return localPackageManager.checkPermission(permission, context.applicationContext.packageName) == PackageManager.PERMISSION_GRANTED
    }

    fun getDiskCacheDir(context: Context, fileDir: String?): File? {

        var cacheDirectory: File?
        if (hasExternalCache(context)) {
            cacheDirectory = context.externalCacheDir
        } else {
            cacheDirectory = context.cacheDir
        }
        if (cacheDirectory == null) {
            cacheDirectory = context.cacheDir
            if (cacheDirectory == null) {
                return null
            }
        }
        if (fileDir != null) {
            val file = File(cacheDirectory, fileDir)
            return if (!file.exists() && !file.mkdir()) {
                cacheDirectory
            } else {
                file
            }
        }
        return cacheDirectory
    }

    /**
     * 删除该目录下的文件
     *
     * @param path
     */
    fun delFile(path: String) {
        if (!TextUtils.isEmpty(path)) {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        }
    }

    /**
     * 把图片压缩到200K
     *
     * @param oldpath 压缩前的图片路径
     * @param newPath 压缩后的图片路径
     * @return
     */
    fun compressFile(oldpath: String, newPath: String): File? {
        var compressBitmap: Bitmap? = decodeFile(oldpath)
        var newBitmap: Bitmap? = ratingImage(oldpath, compressBitmap)
        val os = ByteArrayOutputStream()
        newBitmap!!.compress(Bitmap.CompressFormat.PNG, 60, os)
        val bytes = os.toByteArray()

        var file: File? = null
        try {
            file = getFileFromBytes(bytes, newPath)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (!newBitmap.isRecycled) {
                newBitmap.recycle()
                newBitmap = null
            }
            if (compressBitmap != null) {
                if (!compressBitmap.isRecycled) {
                    compressBitmap.recycle()
                    compressBitmap = null
                }
            }
        }
        return file
    }

    private fun ratingImage(filePath: String, bitmap: Bitmap?): Bitmap {
        val degree = readPictureDegree(filePath)
        return rotatingImageView(degree, bitmap)
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    fun rotatingImageView(angle: Int, bitmap: Bitmap?): Bitmap {
        //旋转图片 动作
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        // 创建新的图片
        return Bitmap.createBitmap(bitmap!!, 0, 0,
                bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
                else -> degree = 0
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return degree
    }

    /**
     * 把字节数组保存为一个文件
     *
     * @param b
     * @param outputFile
     * @return
     */
    fun getFileFromBytes(b: ByteArray, outputFile: String): File? {
        var ret: File? = null
        var stream: BufferedOutputStream? = null
        try {
            ret = File(outputFile)
            val fstream = FileOutputStream(ret)
            stream = BufferedOutputStream(fstream)
            stream.write(b)
        } catch (e: Exception) {
            // log.error("helper:getINSTANCE file from byte process error!");
            e.printStackTrace()
        } finally {
            closeQuietly(stream)
        }
        return ret
    }

    /**
     * 图片压缩
     *
     * @param fPath
     * @return
     */
    fun decodeFile(fPath: String): Bitmap {
        val opts = BitmapFactory.Options()
        opts.inJustDecodeBounds = true
        opts.inDither = false // Disable Dithering mode
        opts.inPurgeable = true // Tell to gc that whether it needs free
        opts.inInputShareable = true // Which kind of reference will be used to
        opts.inPreferredConfig = Bitmap.Config.RGB_565
        BitmapFactory.decodeFile(fPath, opts)
        val requiredSize = 200
        var scale = 1
        if (opts.outHeight > requiredSize || opts.outWidth > requiredSize) {
            val heightRatio = Math.round(opts.outHeight.toFloat() / requiredSize.toFloat())
            val widthRatio = Math.round(opts.outWidth.toFloat() / requiredSize.toFloat())
            scale = Math.min(heightRatio, widthRatio)
        }
        opts.inJustDecodeBounds = false
        opts.inSampleSize = scale
        return BitmapFactory.decodeFile(fPath, opts).copy(Bitmap.Config.ARGB_8888, false)
    }

    fun getFileType(path: String): String? {
        val file = File(path)
        if (file.exists() && file.isFile) {
            val fileName = file.name
            return fileName.substring(fileName.lastIndexOf(".") + 1)
        }

        return null
    }

    fun closeQuietly(closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 读取assets下json文件
     *
     * @param context
     * @param fileName
     * @return
     */
    fun getJson(context: Context, fileName: String): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(InputStreamReader(assetManager.open("$fileName.json"), "UTF-8"))
            var read: String? = null
            while ({ read = bf.readLine();read }() != null) {
                stringBuilder.append(read)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    /**
     * 使用假数据,读取的是assets中的json数据
     */
    @JvmStatic
    fun <T> handleVirtualData(clazz: Class<T>): LiveData<T> {
        val live = MutableLiveData<T>()
        val filepath = "virtualdata" + "/" + clazz.simpleName
        val response = getJson(App.instance, filepath)
        val data = Gson().fromJson(response, clazz)
        live.value = data
        return live
    }
}