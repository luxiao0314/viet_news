package com.viet.news.core.http

import android.content.Context
import android.text.TextUtils
import android.util.Log
import okhttp3.OkHttpClient
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URLEncoder
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Date 2018/7/23
 * @Description
 */
object CertifyUtils {

    //此处不要优化代码,BuildConfig.SALT_TYPE等于3或者4时为国际版
    private var certifyAssetName = "merculet.crt"

    /**
     * 验证系统中信任的根证书 && 本地证书 主体的标识名/发布方标识名 && 本地证书忽略过期时间
     */
    fun getSSLClientIgnoreExpire(client: OkHttpClient, context: Context, assetsSSLFileName: String = certifyAssetName): OkHttpClient {

        getStream(context, assetsSSLFileName).use {

            //Certificate
            val certificateFactory = CertificateFactory.getInstance("X.509")
            var certificate: Certificate? = null
            val pubSub: String
            val pubIssuer: String
            certificate = certificateFactory.generateCertificate(it)
            val pubSubjectDN = (certificate as X509Certificate).subjectX500Principal
            val pubIssuerDN = certificate.issuerX500Principal
            pubSub = pubSubjectDN.name
            pubIssuer = pubIssuerDN.name

//            L.e("aaron pubSubjectDN:${pubSubjectDN.name}")
//            L.e("aaron pubIssuerDN:${pubIssuerDN.name}")

            // Create an SSLContext that uses our TrustManager
            val trustManagers = arrayOf<X509TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                    try {
                        val tmf = TrustManagerFactory.getInstance("X509")
                        tmf.init(null as KeyStore?)
                        for (trustManager in tmf.trustManagers) {
                            (trustManager as X509TrustManager).checkClientTrusted(chain, authType)
                        }
                    } catch (e: Exception) {
                        throw CertificateException(e)
                    }
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                    //1、判断证书是否是本地信任列表里颁发的证书
                    try {
                        val tmf = TrustManagerFactory.getInstance("X509")
                        tmf.init(null as KeyStore?)
                        for (trustManager in tmf.trustManagers) {
                            (trustManager as X509TrustManager).checkServerTrusted(chain, authType)
                        }
                    } catch (e: Exception) {
                        throw CertificateException(e)
                    }

                    //2、判断服务器证书 发布方的标识名  和 本地证书 发布方的标识名 是否一致
                    //3、判断服务器证书 主体的标识名  和 本地证书 主体的标识名 是否一致
                    //getIssuerDN()  获取证书的 issuer（发布方的标识名）值。
                    //getSubjectDN()  获取证书的 subject（主体的标识名）值。
//                    L.e("aaron server pubSubjectDN:${chain[0].subjectX500Principal.name}")
//                    L.e("aaron server pubIssuerDN:${chain[0].issuerX500Principal.name}")
                    if (chain[0].subjectX500Principal.name != pubSub) {
                        throw CertificateException("server's SubjectDN is not equals to client's SubjectDN")
                    }
                    if (chain[0].issuerX500Principal.name != pubIssuer) {
                        throw CertificateException("server's IssuerDN is not equals to client's IssuerDN")
                    }
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            })

            //SSLContext  and SSLSocketFactory
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustManagers, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            //okhttpclient
            val builder = client.newBuilder()
            builder.sslSocketFactory(sslSocketFactory, trustManagers[0])
            return builder.build()
        }
    }


    /**
     * 忽略所有https证书
     */
    fun getTrustAllSSLClient(): SSLContext? {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }
        })
        return try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            sslContext
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 忽略所有https证书
     */
    fun getTrustAllSSLClient(client: OkHttpClient): OkHttpClient {
        try {
            //Certificate

            //keystore

            // Create a trust manager that does not validate certificate chains
            val trustManagers = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustManagers, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            val builder = client.newBuilder()
            builder.sslSocketFactory(sslSocketFactory)
            builder.hostnameVerifier { hostname, session ->
                Log.e("verify", hostname)
                true
            }

            return builder.build()
        } catch (e: Exception) {
            return client
        }

    }

    /**
     * 验证本地证书（certificate pinning）
     * @param client OkHttpClient
     * @param context Context
     * @param assetsSSLFileName String
     * @return OkHttpClient
     */
    fun getSSLClient(client: OkHttpClient, context: Context, assetsSSLFileName: String): OkHttpClient {
        val inputStream = getStream(context, assetsSSLFileName)
        return getSSLClientByInputStream(client, inputStream)
    }

    fun getSSLClientByCertificateString(client: OkHttpClient, certificate: String): OkHttpClient {
        val inputStream = getStream(certificate)
        return getSSLClientByInputStream(client, inputStream)
    }

    private fun getStream(context: Context, assetsFileName: String): InputStream? {
        try {
            return context.assets.open(assetsFileName)
        } catch (var3: Exception) {
            return null
        }

    }

    private fun getStream(certificate: String): InputStream? {
        try {
            return ByteArrayInputStream(certificate.toByteArray(charset("UTF-8")))
        } catch (var3: Exception) {
            return null
        }

    }

    private fun getSSLClientByInputStream(client: OkHttpClient, inputStream: InputStream?): OkHttpClient {
        var client = client
        if (inputStream != null) {
            val sslSocketFactory = setCertificates(inputStream)
            if (sslSocketFactory != null) {
                client = client.newBuilder().sslSocketFactory(sslSocketFactory).build()
            }
        }
        return client
    }

    private fun setCertificates(vararg certificates: InputStream): SSLSocketFactory? {
        try {
            val certificateFactory = CertificateFactory.getInstance("X.509")

            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null)

            var index = 0
            for (certificate in certificates) {
                val certificateAlias = Integer.toString(index++)
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate))
                try {
                    certificate.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagerFactory.trustManagers, SecureRandom())

            return sslContext.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 由于okhttp header 中的 value 不支持 null, \n 和 中文这样的特殊字符,所以encode字符串
     *
     * @param value
     * @return
     */
    fun getHeaderValueEncoded(value: String): String {
        if (TextUtils.isEmpty(value)) return " "
        var i = 0
        val length = value.length
        while (i < length) {
            val c = value[i]
            if (c <= '\u001f' && c != '\t' || c >= '\u007f') {//根据源码okhttp允许[0020-007E]+\t的字符
                try {
                    return URLEncoder.encode(value, "UTF-8")
                } catch (e: Exception) {
                    e.printStackTrace()
                    return " "
                }

            }
            i++
        }
        return value
    }

    /**
     * 由于okhttp header 中的 name 不支持 null,空格、\t、 \n 和 中文这样的特殊字符,所以encode字符串
     */
    fun getHeaderNameEncoded(name: String): String {
        if (TextUtils.isEmpty(name)) return "null"
        var i = 0
        val length = name.length
        while (i < length) {
            val c = name[i]
            if (c <= '\u0020' || c >= '\u007f') {//根据源码okhttp允许[0021-007E]的字符
                try {
                    return URLEncoder.encode(name, "UTF-8")
                } catch (e: Exception) {
                    e.printStackTrace()
                    return " "
                }

            }
            i++
        }
        return name
    }
//    val isInternationalHost :Boolean = BuildConfig.SALT_TYPE == 3 || BuildConfig.SALT_TYPE == 4
    val isInternationalHost :Boolean = true

}