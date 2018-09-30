package cn.magicwindow.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.content.res.Configuration
import android.graphics.Point
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings.Secure
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.WindowManager
import com.viet.news.core.ui.App
import com.viet.news.core.utils.SPHelper
import java.util.*


/**
 * 获得设备信息
 *
 * @author Aaron.Liu
 */
object DeviceInfoUtils {
    private const val CHINA_CARRIER_UNKNOWN = "0"
    private const val CHINA_MOBILE = "1"
    private const val CHINA_UNICOM = "2"
    private const val CHINA_TELECOM = "3"
    private const val CHINA_TIETONG = "4"
    private const val TAG = "DeviceInfo"
    //    private final static String SHA1_ALGORITHM = "SHA-1";
    //    private final static String CHAR_SET = "iso-8859-1";
    //    private static final String Tag = "DeviceInfoUtils";
    //    private static final String DEVICEINFO = "DeviceInfo";
    const val os = "0"   //此值代表Android，不要动
    private const val FIRST_TAG = "0"   //此值代表首次启动
    private const val NO_FIRST_TAG = "1"   //此值代表非首次启动
    //    private static LocationListener listener;
    private var VERSION_NAME: String = "1.0"
    private var VERSION_CODE: Int = 0

    private//防止fr为空
    //防止str2为空
//    val cpuInfo: String
//        get() {
//            val str1 = "/proc/cpuinfo"
//            var str2 = ""
//            val cpuInfo = arrayOf("", "")
//            var arrayOfString: Array<String>
//            var localBufferedReader: BufferedReader? = null
//            try {
//                val file = File(str1)
//                if (!file.exists()) {
//                    return ""
//                }
//                val fr = FileReader(file)
//                localBufferedReader = BufferedReader(fr, 8192)
//                str2 = localBufferedReader.readLine()
//                if (str2.isBlank()) {
//                    return ""
//                }
//                arrayOfString = str2.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                for (i in 2 until arrayOfString.size) {
//                    cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " "
//                }
//                str2 = localBufferedReader.readLine()
//                arrayOfString = str2.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                cpuInfo[1] += arrayOfString[2]
//                localBufferedReader.close()
//            } catch (ignored: IOException) {
//            } finally {
//                IOUtils.closeQuietly(localBufferedReader)
//            }
//            return Arrays.toString(cpuInfo)
//        }

    /**
     * 获得系统版本
     *
     * @return os version
     */
    val osVersion: String
        get() = Build.VERSION.RELEASE

    /**
     * 获取设备品牌
     *
     * @return branding
     */
    val branding: String
        get() = Build.BRAND

    /**
     * 获得制造商
     *
     * @return manufacturer
     */
    val manufacturer: String
        get() = Build.MANUFACTURER

    /**
     * 设备的名字
     *
     * @return device model
     */
    val device: String
        get() = Build.MODEL


    /**
     * 获得本地语言和国家
     *
     * @return Language +county
     */
    val local: String
        get() {
            val locale = Locale.getDefault()
            return locale.language + "_" + locale.country
        }


    /**
     * 获得Android设备唯一标识：Device_id
     *
     * @param context context
     * @return device id
     */
    @SuppressLint("HardwareIds", "MissingPermission", "PrivateApi")
    fun getDeviceId(context: Context): String {

        //优化device id的策略，尽量减少漂移
        if (!TextUtils.isEmpty(SPHelper.create(context).getString(SPHelper.TRACKING_DEVICE_ID))) {
            return SPHelper.create(context).getString(SPHelper.TRACKING_DEVICE_ID)

        }

        var result: String? = ""
        try {
            if (Util.checkPermissions(context, "android.permission.READ_PHONE_STATE")) {
                var deviceId = ""
                if (context.getSystemService(Context
                                .TELEPHONY_SERVICE) != null) {
                    val telephonyManager = context.getSystemService(Context
                            .TELEPHONY_SERVICE) as TelephonyManager
                    deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        telephonyManager.imei
                    } else {
                        telephonyManager.deviceId
                    }
                }
                var backId = ""
                if (!TextUtils.isEmpty(deviceId) && !deviceId.contains("00000000000")) {
                    backId = deviceId
                    backId = backId.replace("0", "")
                }

                if (TextUtils.isEmpty(deviceId) || TextUtils.isEmpty(backId)) {
                    deviceId = try {
                        val c = Class.forName("android.os.SystemProperties")
                        val get = c.getMethod("get", String::class.java, String::class.java)
                        get.invoke(c, "ro.serialno", "unknown") as String
                    } catch (t: Exception) {
                        ""
                    }

                }

                result = if (deviceId.isNotBlank()) {
                    deviceId
                } else {
                    Secure.getString(context.contentResolver, Secure.ANDROID_ID)
                }
            } else {
                result = Secure.getString(context.contentResolver, Secure.ANDROID_ID)
            }
        } catch (ignored: Exception) {

        }

        SPHelper.create(context).putString(SPHelper.TRACKING_DEVICE_ID, result!!)
        return result
    }

    @SuppressLint("HardwareIds", "MissingPermission", "PrivateApi")
    fun getIMEI(context: Context): String? {
        return if (Util.checkPermissions(context, "android.permission.READ_PHONE_STATE")) {
            if (context.getSystemService(Context.TELEPHONY_SERVICE) != null) {
                val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    telephonyManager.imei
                } else {
                    telephonyManager.deviceId
                }
            } else {
                null
            }
        } else {
            null
        }


    }

    @SuppressLint("ObsoleteSdkInt")
//这个可获取到类似1080*1920
    fun getScreenSize(context: Context): String {

        var result = ""
        try {
            //first method
            if (Build.VERSION.SDK_INT in 13..16) {
                val windowManager = context.getSystemService(Context
                        .WINDOW_SERVICE) as WindowManager
                val display = windowManager.defaultDisplay
                val size = Point()
                display.getSize(size)

                val screenWidth = size.x
                val screenHeight = size.y
                result = if (context.resources.configuration.orientation == Configuration
                                .ORIENTATION_PORTRAIT) {
                    screenWidth.toString() + "x" + screenHeight
                } else {
                    screenHeight.toString() + "x" + screenWidth
                }
            } else if (Build.VERSION.SDK_INT >= 17) {
                val windowManager = context.getSystemService(Context
                        .WINDOW_SERVICE) as WindowManager
                val display = windowManager.defaultDisplay
                val size = Point()

                display.getRealSize(size)

                val screenWidth = size.x
                val screenHeight = size.y
                result = if (context.resources.configuration.orientation == Configuration
                                .ORIENTATION_PORTRAIT) {
                    screenWidth.toString() + "x" + screenHeight
                } else {
                    screenHeight.toString() + "x" + screenWidth
                }
            } else {
                val dm2 = context.resources.displayMetrics
                // 竖屏
                result = if (context.resources.configuration.orientation == Configuration
                                .ORIENTATION_PORTRAIT) {
                    dm2.widthPixels.toString() + "x" + dm2.heightPixels
                } else {// 横屏
                    dm2.heightPixels.toString() + "x" + dm2.widthPixels
                }
            }
        } catch (ignored: Exception) {

        }

        return result
    }

    /**
     * 获得应用的包名
     *
     * @param context context
     * @return package name
     */
    fun getPackageName(context: Context): String {
        return context.packageName
    }


    /**
     * 获得应用的包名
     *
     * @param context context
     * @return package name
     */
    fun getAppName(context: Context): String {
        var appName = ""
        try {
            appName = context.packageManager.getApplicationLabel(context.applicationInfo) as String
        } catch (ignored: Exception) {
        }

        return appName
    }


    //    @SuppressLint("MissingPermission")
//    fun getNetGeneration(context: Context): String {
//        if (!Util.checkPermissions(context, "android.permission.ACCESS_NETWORK_STATE")) {
//            return ""
//        }
//        var result = cn.magicwindow.core.config.Config.NO_NETWORK
//        try {
//            var networkInfo: NetworkInfo? = null
//            if (context.getSystemService(Context.CONNECTIVITY_SERVICE) != null) {
//                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//                networkInfo = connectivityManager.activeNetworkInfo
//                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state == NetworkInfo.State.CONNECTED) {
//                    return cn.magicwindow.core.config.Config.NETWORK_WIFI
//                }
//            }
//
//
//            if (networkInfo != null && networkInfo.isAvailable) {
//
//                if (context.getSystemService(
//                                Context.TELEPHONY_SERVICE) != null) {
//                    val telephonyManager = context.getSystemService(
//                            Context.TELEPHONY_SERVICE) as TelephonyManager
//                    when (telephonyManager.networkType) {
//                        TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> result = cn.magicwindow.core.config.Config.NETWORK_2G
//                        TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> result = cn.magicwindow.core.config.Config.NETWORK_3G
//                        TelephonyManager.NETWORK_TYPE_LTE -> result = cn.magicwindow.core.config.Config.NETWORK_4G
//                        TelephonyManager.NETWORK_TYPE_UNKNOWN -> result = cn.magicwindow.core.config.Config.NETWORK_4G
//                        else -> result = cn.magicwindow.core.config.Config.NETWORK_2G
//                    }
//                }
//            }
//        } catch (ignored: Exception) {
//
//        }
//
//        return result
//    }

    /**
     * 获得当前应用的版本号
     *
     * @param context context
     * @return App Version
     */

    @Synchronized
    fun getAppVersionName(context: Context): String {
        if (!TextUtils.isEmpty(VERSION_NAME)) {
            return VERSION_NAME
        }

        var info: PackageInfo? = null
        try {
            info = context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (ignored: NameNotFoundException) {
        }

        if (info != null) {
            VERSION_NAME = info.versionName
            VERSION_CODE = info.versionCode
        }
        return VERSION_NAME
    }

    @Synchronized
    fun getAppVersionCode(context: Context): Int {
        if (VERSION_CODE != 0) {
            return VERSION_CODE
        }
        var info: PackageInfo? = null
        try {
            info = context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (ignored: NameNotFoundException) {
        }

        if (info != null) {
            VERSION_CODE = info.versionCode
            VERSION_NAME = info.versionName
        }
        return VERSION_CODE
    }


    private fun carryByOperator(manager: TelephonyManager?): String {
        val operatorString = manager!!.simOperator

        return if ("46000" == operatorString || "46002" == operatorString || "46007" == operatorString) {
            //中国移动
            CHINA_MOBILE
        } else if ("46001" == operatorString || "46006" == operatorString) {
            //中国联通
            CHINA_UNICOM
        } else if ("46003" == operatorString || "46005" == operatorString) {
            //中国电信
            CHINA_TELECOM
        } else if ("46020" == operatorString) {
            CHINA_TIETONG
        } else {
            CHINA_CARRIER_UNKNOWN
        }
    }

    /**
     * 获得网管硬件地址
     *
     * @param context context
     * @return Mac Address
     */
//    @SuppressLint("HardwareIds", "WifiManagerPotentialLeak")
//    fun getMacAddress(context: Context): String {
//        if (!Util.checkPermissions(context, "android.permission.ACCESS_WIFI_STATE")) {
//            return ""
//        }
//        var result = ""
//
//        try {
//            if (context.getSystemService(Context.WIFI_SERVICE) != null) {
//                val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
//                val wifiInfo = wifiManager.connectionInfo
//                if (wifiInfo != null) {
//                    result = wifiInfo.macAddress
//                }
//            }
//        } catch (ignored: Exception) {
//
//        }
//
//        // Log.i("MAC Address", "macAdd:" + result);
//        return result
//    }

    /**
     * 获得注册运营商的名字
     *
     * @param context context
     * @return Carrier
     */
//    @SuppressLint("HardwareIds", "MissingPermission")
//    fun getCarrier(context: Context): String {
//        var result = ""
//        try {
//            if (context.getSystemService(Context.TELEPHONY_SERVICE) == null) {
//                return result
//            }
//            val manager = context.getSystemService(Context
//                    .TELEPHONY_SERVICE) as TelephonyManager
//            if (!Util.checkPermissions(context, "android.permission.READ_PHONE_STATE")) {
//                result = carryByOperator(manager)
//            } else {
//                var imsi = ""
//                imsi = manager.subscriberId
//
//                if (TextUtils.isEmpty(imsi)) {
//                    result = carryByOperator(manager)
//                }
//
//                result = if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
//                    //中国移动
//                    cn.magicwindow.core.config.Config.CHINA_MOBILE
//                } else if (imsi.startsWith("46001") || imsi.startsWith("46006")) {
//                    //中国联通
//                    cn.magicwindow.core.config.Config.CHINA_UNICOM
//                } else if (imsi.startsWith("46003") || imsi.startsWith("46005")) {
//                    //中国电信
//                    cn.magicwindow.core.config.Config.CHINA_TELECOM
//                } else if (imsi.startsWith("46020")) {
//                    cn.magicwindow.core.config.Config.CHINA_TIETONG
//                } else {
//                    carryByOperator(manager)
//                }
//            }
//        } catch (ignored: Exception) {
//
//        }
//
//        return result
//
//    }

    /**
     * 判断当前设备是手机还是平板
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    private fun isTablet(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration
                .SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    fun getDeviceType(context: Context): Int {
        return if (isTablet(context)) {
            1
        } else {
            0
        }
    }

    private var MWChannel: String = ""

//    fun getMWChannel(): String {
//
//        if (Preconditions.isBlank(MWChannel)) {
//            MWChannel = getMetaDataFromApplication(Config.MW_CHANNEL)
//        }
//
//        return MWChannel
//    }

    /**
     * 读取application 节点  meta-data 信息
     */
//    fun getMetaDataFromApplication(tag: String): String {
//        var metaData: String = ""
//        if (Preconditions.isBlank(tag)) {
//            return ""
//        }
//
//        try {
//            val appInfo = App.instance.packageManager
//                    .getApplicationInfo(App.instance.packageName,
//                            PackageManager.GET_META_DATA)
//            if (appInfo.metaData != null && appInfo.metaData.getString(tag) != null) {
//                metaData = appInfo.metaData.getString(tag)
//                if (Preconditions.isNotBlank(metaData)) {
//                    metaData = metaData.trim { it <= ' ' }
//                }
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//        }
//
//        return metaData
//    }

    /**
     * 检查网络是否可用
     *
     * @param context
     * @return
     */
    fun isNetworkAvailable(): Boolean {
        val manager = App.instance.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false
        val networkinfo = manager.activeNetworkInfo
        return !(networkinfo == null || !networkinfo.isAvailable)
    }

    /**
     * 首先检查手机是否安装了某一项APP
     */
    fun isAPPInstalled(packageName: String): Boolean {
        val pm = App.instance.packageManager
        val pinfo = pm.getInstalledPackages(0)
        pinfo.forEachIndexed { index, _ ->
            if (pinfo[index].packageName == packageName) {
                return true
            }
        }
        return false
    }
}
