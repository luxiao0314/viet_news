package com.viet.news.core.utils

import android.annotation.SuppressLint
import android.util.Base64
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.viet.news.core.utils.EncryptUtils.java
 * @author: Tony Shen
 * @date: 2018-05-21 20:53
 */
object EncryptUtils {

    // Used to load the 'native-lib' library on application startup.
    init {
        System.loadLibrary("codec")
    }

    external fun nativeCheck(): String

    external fun encryptPayPassword(oldString: String): String

    external fun encryptPayPasswordWithSalt(oldString: String, type: Int): String


//    /**
//     * AES128加密
//     * @param plainText 明文
//     * @return
//     */
//    @SuppressLint("GetInstance")
//    fun encryptByAES(plainText: String, key: String): String {
//
//        try {
//            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
//            val keyspec = SecretKeySpec(key.toByteArray(), "AES")
//            cipher.init(Cipher.ENCRYPT_MODE, keyspec)
//            val encrypted = cipher.doFinal(plainText.toByteArray())
//            return Base64.encodeToString(encrypted, Base64.NO_WRAP)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return ""
//        }
//
//    }
//
//    /**
//     * AES128解密
//     * @param cipherText 密文
//     * @return
//     */
//    fun decryptByAES(cipherText: String, key: String): String {
//        try {
//            val encrypted1 = Base64.decode(cipherText, Base64.NO_WRAP)
//            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
//            val keyspec = SecretKeySpec(key.toByteArray(), "AES")
//            cipher.init(Cipher.DECRYPT_MODE, keyspec)
//            val original = cipher.doFinal(encrypted1)
//            return String(original)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return ""
//        }
//    }
//
//    /**
//     * 生成公钥和私钥
//     *
//     * @throws Exception
//     */
//    @Throws(Exception::class)
//    fun genKeyPair(): KeyPair {
//        val keyPairGen = KeyPairGenerator.getInstance("RSA")
//        keyPairGen.initialize(1024)
//        val keyPair = keyPairGen.generateKeyPair()
////        val publicKey = keyPair.public as RSAPublicKey
////        val privateKey = keyPair.private as RSAPrivateKey
////        val publicKeyStr = getPublicKeyStr(publicKey)
////        val privateKeyStr = getPrivateKeyStr(privateKey)
//        return keyPair
//    }

//    @Throws(Exception::class)
//    fun getPrivateKeyStr(privateKey: PrivateKey): String {
//        return Base64Util.encode(privateKey.encoded)
//    }
//
//    @Throws(Exception::class)
//    fun getPublicKeyStr(publicKey: PublicKey): String {
//        return Base64Util.encode(publicKey.encoded)
//    }
}
