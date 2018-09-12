package cn.magicwindow.core.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import com.viet.news.core.ui.App
import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.regex.Pattern
import kotlin.experimental.and


/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 16/11/2017 9:09 PM
 * @description
 */
object Util {

    private val COLOR_PATTEN = Pattern.compile("([0-9A-Fa-f]{8})|([0-9A-Fa-f]{6})")
    private val PHONE_NUM_PATTERN = Pattern.compile("^((\\+?86)?\\s?-?)1[0-9]{10}")
    private val PHONE_NUM_FORMAT_PATTERN = Pattern.compile("^((\\+?86)?)\\s?-?")
    private val EMAIL_PATTERN = Pattern.compile("^\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]+$", Pattern.CASE_INSENSITIVE)

    @JvmStatic
    fun getCurrentTimeSecond(): Long {
        return System.currentTimeMillis() / 1000
    }

    @JvmStatic
    fun getCurrentTimeSecondStr(): String {
        return (System.currentTimeMillis() / 1000).toString()
    }

//    /**
//     * 获取颜色类自定义属性的颜色值
//     *
//     * @param context 上下文
//     * @param attrRes 自定义属性
//     * @return A single color value in the form 0xAARRGGBB
//     */
//    @JvmStatic
//    fun getAttrColor(context: Context, @VolumeProviderCompat.ControlType attrRes: Int): Int {
//        return if (Build.VERSION.SDK_INT >= 23) {
//            ContextCompat.getColor(context, attrRes)
//        } else {
//            context.resources.getColor(attrRes)
//        }
//    }
    //color end

    private fun filterColor(colorString: String): String {
        //        Pattern p = Pattern.compile("([0-9A-Fa-f]{8})|([0-9A-Fa-f]{6})");
        val m = COLOR_PATTEN.matcher(colorString)

        while (m.find()) {
            return m.group()
        }
        return ""
    }

    @JvmStatic
    fun parseColor(colorString: String): Int {
        val filterColor = filterColor(colorString)
        val parsedColorString: String
        if (!"#".equals(filterColor[0].toString(), ignoreCase = true)) {
            parsedColorString = "#" + filterColor
        } else {
            parsedColorString = filterColor
        }
        return Color.parseColor(parsedColorString)
    }

    @JvmStatic
    fun isColor(colorString: String): Boolean {
        val m = COLOR_PATTEN.matcher(colorString)

        while (m.find()) {
            return true
        }
        return false
    }

    /**
     * @param
     */
    @JvmStatic
    fun createRoundCornerShapeDrawable(radius: Float, borderColor: Int): ShapeDrawable {
        val outerR = floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
        val roundRectShape = RoundRectShape(outerR, null, null)  //  构造一个圆角矩形,可以使用其他形状，这样ShapeDrawable 就会根据形状来绘制。
        //RectShape rectShape = new RectShape();  // 如果要构造直角矩形可以
        val shapeDrawable = ShapeDrawable(roundRectShape) // 组合圆角矩形和ShapeDrawable
        shapeDrawable.paint.color = borderColor         // 设置形状的颜色
        shapeDrawable.paint.style = Paint.Style.FILL   //  设置绘制方式为填充
        return shapeDrawable
    }

    /**
     * 图片编码 bitmap->string
     *
     * @param bm
     * @return
     */
    @JvmStatic
    fun bitmap2Base64String(bm: Bitmap): String {
        val bos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 0, bos)// 参数100表示不压缩
        val bytes = bos.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun dip2px(dpValue: Float, context: Context = App.instance): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dip(pxValue: Float, context: Context = App.instance): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun dip2pxFloat(dpValue: Float, context: Context = App.instance): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.resources.displayMetrics)
    }

    fun sp2pxFloat(spValue: Float, context: Context = App.instance): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.resources.displayMetrics)
    }

    /**
     * 检查权限是否开启
     *
     * @param permission
     * @return true or false
     */
    @JvmStatic
    fun checkPermissions(context: Context?, permission: String): Boolean {

        val localPackageManager = context!!.applicationContext.packageManager
        return localPackageManager.checkPermission(permission, context.applicationContext.packageName) == PackageManager.PERMISSION_GRANTED
    }

    @JvmStatic
    fun addBackSplash(input: String): String {
        var i: Int
        val result = StringBuilder()
        val chars = input.toCharArray()
        i = 0
        while (i < chars.size) {
            result.append(chars[i])
            if (i > 0 && i < chars.size && "\\" == chars[i].toString() && "\\" != chars[i - 1].toString() && "\\" != chars[i + 1].toString()) {
                result.append("\\").append("\\")
            }
            i++
        }

        Log.d("addBackSplash:", result.toString())
        return result.toString()
    }

    /**
     * MD5 encrypt做过处理，取的是中间16位。
     */
    @JvmStatic
    fun md5with16Byte(str: String): String {
        try {
            val localMessageDigest = MessageDigest.getInstance("MD5")
            localMessageDigest.update(str.toByteArray())
            val arrayOfByte = localMessageDigest.digest()
            val stringBuffer = StringBuilder()
            for (anArrayOfByte in arrayOfByte) {
                val j = 0xFF and anArrayOfByte.toInt()
                if (j < 16) {
                    stringBuffer.append("0")
                }
                stringBuffer.append(Integer.toHexString(j))
            }
            return stringBuffer.toString().toLowerCase().substring(8, 24)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""
    }

    @JvmStatic
    fun md5(str: String): String {
        var reStr = ""
        try {
            val md5 = MessageDigest.getInstance("MD5")
            val bytes = md5.digest(str.toByteArray())
            val stringBuffer = StringBuilder()
            for (b in bytes) {
                val bt = b and 0xff.toByte()
                if (bt < 16) {
                    stringBuffer.append(0)
                }
                stringBuffer.append(Integer.toHexString(bt.toInt()))
            }
            reStr = stringBuffer.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return reStr
    }

    @JvmStatic
    fun formatPhoneNum(phoneNum: String): String {

        val m1 = PHONE_NUM_PATTERN.matcher(phoneNum)
        if (m1.matches()) {
            val m2 = PHONE_NUM_FORMAT_PATTERN.matcher(phoneNum)
            val sb = StringBuffer()
            while (m2.find()) {
                m2.appendReplacement(sb, "")
            }
            m2.appendTail(sb)
            return sb.toString()
        } else {
            return ""
        }
    }

    /**
     * @param a
     * @return
     */
//    @JvmStatic
//    fun equals(a: String, b: String): Boolean {
//        return TextUtils.isEmpty(a) && TextUtils.isEmpty(b) || Preconditions.isNotBlank(a) && a == b
//    }

    /**
     * user java reg to check phone number and replace 86 or +86
     * only check start with "+86" or "86" ex +8615911119999 13100009999 replace +86 or 86 with ""
     *
     * @param phoneNum 被检测值
     * @return
     */
    @JvmStatic
    fun checkPhoneNum(phoneNum: String): Boolean {

        val m1 = PHONE_NUM_PATTERN.matcher(phoneNum)
        return m1.matches()
    }

    @JvmStatic
    fun checkEmail(email: String): Boolean {
        val matcher = EMAIL_PATTERN.matcher(email)
        return matcher.matches()
    }


    //匹配C类地址的IP
    const val regexCIp = "^192\\.168\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)$"
    //匹配A类地址
    const val regexAIp = "^10\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)$"
    //匹配B类地址
    const val regexBIp = "^172\\.(1[6-9]|2\\d|3[0-1])\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)$"

    /*判断字符串是否为一个合法的IPv4地址*/
    private fun isIPv4RealAddress(address: String): Boolean {
        val p = Pattern.compile("($regexAIp)|($regexBIp)|($regexCIp)")
        val m = p.matcher(address)
        return m.matches()
    }

    @JvmStatic
    fun getHostIp(): String? {
        var networkInterfaces: Enumeration<NetworkInterface>? = null
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces()
        } catch (e: SocketException) {
            e.printStackTrace()
        }

        var address: InetAddress
        while (networkInterfaces!!.hasMoreElements()) {
            val networkInterface = networkInterfaces.nextElement()
            val inetAddresses = networkInterface.inetAddresses
            while (inetAddresses.hasMoreElements()) {
                address = inetAddresses.nextElement()
                val hostAddress = address.hostAddress
//                val matcher = ip.matcher(hostAddress)
                if (!address.isLoopbackAddress && isIPv4RealAddress(hostAddress)) {
                    return hostAddress
                }

            }
        }
        return null
    }

    fun mul(v1: String?, v2: String?): Double {
        val b1 = BigDecimal(if (v1.isNullOrEmpty()) "0" else v1)
        val b2 = BigDecimal(if (v2.isNullOrEmpty()) "0" else v2)
        return b1.multiply(b2).toDouble()
    }

    /**
     * 根据UnicodeBlock方法判断是否是中文或者是中文标点
     * @param c Char
     * @return Boolean
     */
    fun isChineseOrPunctuation(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                    || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                    || ub === Character.UnicodeBlock.VERTICAL_FORMS)
        } else {
            (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                    || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS)
        }
    }

    /**
     * 根据UnicodeBlock方法判断是否是中文
     * @param c Char
     * @return Boolean
     */
    fun isChinese(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
    }

    /**
     *根据UnicodeBlock方法判断中文标点符号
     * @param c Char
     * @return Boolean
     */
    fun isChinesePunctuation(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            (ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                    || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                    || ub === Character.UnicodeBlock.VERTICAL_FORMS)
        } else {
            (ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                    || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS)
        }
    }

    //此处不要优化代码,BuildConfig.SALT_TYPE等于3或者4时为国际版
//    val isInternationalHost: Boolean = BuildConfig.SALT_TYPE == 3 || BuildConfig.SALT_TYPE == 4

    fun getStatusBarHeight(context: Context = App.instance): Int {

        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")

        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }

        return result

    }

    /**
     * 规则1：至少包含大小写字母及数字中的一种
     *
     * @param str
     * @return
     */
    fun isLetterOrDigit(str: String): Boolean {
        var isLetterOrDigit = false//定义一个boolean值，用来表示是否包含字母或数字
        for (i in 0 until str.length) {
            if (Character.isLetterOrDigit(str[i])) {   //用char包装类中的判断数字的方法判断每一个字符
                isLetterOrDigit = true
            }
        }
        val regex = "^[a-zA-Z0-9]+$"
        return isLetterOrDigit && str.matches(regex.toRegex())
    }

    /**
     * 规则2：至少包含字母及数字中的两种
     *
     * @param str
     * @return
     */
    fun isLetterDigit(str: String): Boolean {
        var isDigit = false//定义一个boolean值，用来表示是否包含数字
        var isLetter = false//定义一个boolean值，用来表示是否包含字母
        for (i in 0 until str.length) {
            if (Character.isDigit(str[i])) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true
            } else if (Character.isLetter(str[i])) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true
            }
        }
        val regex = "^[a-zA-Z0-9]+$"
        return isDigit && isLetter && str.matches(regex.toRegex())
    }

    /**
     * 规则3：必须同时包含大写字母、小写字母及数字
     *
     * @param str
     * @return
     */
    fun isContainAll(str: String): Boolean {
        var isDigit = false//定义一个boolean值，用来表示是否包含数字
        var isLowerCase = false//定义一个boolean值，用来表示是否包含字母
        var isUpperCase = false
        for (i in 0 until str.length) {
            if (Character.isDigit(str[i])) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true
            } else if (Character.isLowerCase(str[i])) {  //用char包装类中的判断字母的方法判断每一个字符
                isLowerCase = true
            } else if (Character.isUpperCase(str[i])) {
                isUpperCase = true
            }
        }
        val regex = "^[a-zA-Z0-9]+$"
        return isDigit && isLowerCase && isUpperCase && str.matches(regex.toRegex())
    }

    /**
     * 规则5：只包含大小写字母
     *
     * @param str
     * @return
     */
    fun isLetter(str: String): Boolean {
        val regex = "^[a-zA-Z]+$"
        return str.matches(regex.toRegex())
    }

    /**
     * 规则6：有且只有一个大小写字母
     *
     * @param str
     * @return
     */
    fun isOneLetter(str: String): Boolean {
        val regex = "^[a-zA-Z]$"
        return str.matches(regex.toRegex())
    }
}