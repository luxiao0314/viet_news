package com.viet.news.webview

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.PopupMenu
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.LinearLayout
import android.widget.Toast
import com.google.gson.Gson
import com.just.agentweb.*
import com.safframework.log.L
import com.viet.news.core.R
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.http.URLParser
import com.viet.news.core.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_agentweb.*
import java.util.*

/**
 * @Description
 * @Author lucio
 * @Email xiao.lu@magicwindow.cn
 * @Date 10/04/2018 9:13 PM
 * @Version
 */
open class AgentWebFragment : BaseFragment(), FragmentKeyDown {

    protected val model by viewModelDelegate(WebviewViewModel::class, true)
    var mAgentWeb: AgentWeb? = null
    private var mPopupMenu: PopupMenu? = null
    private var mTitle: String? = null
    protected var mUrl: String = ""
        get() = URLParser(field).addParam("os", "0").toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUrl = arguments?.getString(WebActivity.URL) ?: ""
        mTitle = arguments?.getString(WebActivity.TITLE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_agentweb, container, false)
    }

    override fun initView(view: View) {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(view as LinearLayout, -1, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
                .useDefaultIndicator(-1, 2)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .setAgentWebWebSettings(settings)//设置 IAgentWebSettings。
                .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                .setWebChromeClient(mWebChromeClient) //WebChromeClient
                .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                .setMainFrameErrorView(View.inflate(context, R.layout.view_error, null))
                .useMiddlewareWebChrome(middlewareWebChrome) //设置WebChromeClient中间件，支持多个WebChromeClient，AgentWeb 3.0.0 加入。
                .useMiddlewareWebClient(middlewareWebClient) //设置WebViewClient中间件，支持多个WebViewClient， AgentWeb 3.0.0 加入。
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(mUrl)

        if (BuildConfig.DEBUG) AgentWebConfig.debug()
        // AgentWeb 没有把WebView的功能全面覆盖 ，所以某些设置 AgentWeb 没有提供 ， 请从WebView方面入手设置。
        mAgentWeb?.webCreator?.webView?.overScrollMode = WebView.OVER_SCROLL_NEVER
        initListener()
    }

    var mPermissionInterceptor: PermissionInterceptor = PermissionInterceptor { url, permissions, action ->
        L.i(tag, "mUrl:" + url + "  permission:" + Gson().toJson(permissions) + " action:" + action)
        false
    }

    /**
     * @return IAgentWebSettings
     */
    val settings: IAgentWebSettings<*> = object : AbsAgentWebSettings() {
        override fun bindAgentWebSupport(agentWeb: AgentWeb) {
            this.mAgentWeb = agentWeb
        }
    }

    var mWebChromeClient: WebChromeClient = object : WebChromeClient() {

        override fun onReceivedTitle(view: WebView, title: String) {
            var title = title
            super.onReceivedTitle(view, title)
            if (toolbar_title != null) {
                if (!TextUtils.isEmpty(mTitle)) {
//                    L.d("title1:$mTitle")
                    if (mTitle!!.length > 25) {
                        mTitle = mTitle!!.substring(0, 25) + "..."
                    }
                    toolbar_title.text = mTitle
                } else if (!TextUtils.isEmpty(title)) {
                    if (title.length > 25) {
                        title = title.substring(0, 25) + "..."
                    }
//                    L.d("title2:$title")
                    toolbar_title.text = title
                }
            }
        }

        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (view.canGoBack()) {
                pageNavigator(View.VISIBLE)
            } else {
                pageNavigator(View.GONE)
            }
        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            L.d("console", consoleMessage?.message())
            return super.onConsoleMessage(consoleMessage)
        }
    }

    var mWebViewClient: WebViewClient = object : WebViewClient() {

        private val timer = HashMap<String, Long>()

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return shouldOverrideUrlLoading(view, request.url.toString() + "")
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            //intent:// scheme的处理 如果返回false ， 则交给 DefaultWebClient 处理 ， 默认会打开该Activity  ， 如果Activity不存在则跳到应用市场上去.  true 表示拦截
            //例如优酷视频播放 ，intent://play?...package=com.youku.phone;end;
            //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
            return url.startsWith("intent://") && url.contains("com.youku.phone")
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            timer[url] = System.currentTimeMillis()
            if (url != mUrl && view.canGoBack()) {
                pageNavigator(View.VISIBLE)
            } else {
                pageNavigator(View.GONE)
            }
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            if (timer[url] != null) {
                val overTime = System.currentTimeMillis()
                val startTime = timer[url]
                L.i(tag, " page mUrl1:" + url + "  used time:" + (overTime - startTime!!))
            } else {
                L.i(tag, " page mUrl2:$url")
            }
        }

//        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
//            CertifiUtils.onCertificateOfVerification(handler, view.url)
//        }
    }

    private val mOnClickListener = View.OnClickListener { v ->
        val i = v.id
        if (i == R.id.iv_back) {// true表示AgentWeb处理了该事件
            if (mAgentWeb != null && !mAgentWeb!!.back()) {
                this@AgentWebFragment.activity?.finish()
            }
        } else if (i == R.id.iv_finish) {
            this@AgentWebFragment.activity?.finish()
        } else {
        }
    }

    /**
     * MiddlewareWebClientBase 是 AgentWeb 3.0.0 提供一个强大的功能，
     * 如果用户需要使用 AgentWeb 提供的功能， 不想重写 WebClientView方
     * 法覆盖AgentWeb提供的功能，那么 MiddlewareWebClientBase 是一个
     * 不错的选择 。
     *
     * @return
     */
    protected val middlewareWebClient: MiddlewareWebClientBase = object : MiddlewareWebClientBase() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith("agentweb")) {
                L.i(tag, "agentweb scheme ~")
                return true
            }
            return super.shouldOverrideUrlLoading(view, url)
        }

    }

    protected val middlewareWebChrome: MiddlewareWebChromeBase = object : MiddlewareWebChromeBase() {
        override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
            L.i("Info", "onJsAlert:$url")
            return super.onJsAlert(view, url, message, result)
        }
    }

    protected fun initListener() {
        iv_back.setOnClickListener(mOnClickListener)
        iv_finish.setOnClickListener(mOnClickListener)
        pageNavigator(View.GONE)
    }

    private fun pageNavigator(tag: Int) {
        iv_back.visibility = tag
        view_line.visibility = tag
        view_line2.visibility = if (tag == View.GONE) View.VISIBLE else View.GONE
    }

    /**
     * 打开浏览器
     *
     * @param targetUrl 外部浏览器打开的地址
     */
    private fun openBrowser(targetUrl: String) {
        if (TextUtils.isEmpty(targetUrl) || targetUrl.startsWith("file://")) {
            Toast.makeText(this.context, "$targetUrl 该链接无法使用浏览器打开。", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val mUri = Uri.parse(targetUrl)
        intent.data = mUri
        startActivity(intent)
    }

    /**
     * 清除 WebView 缓存
     */
    private fun toCleanWebCache() {
        //清理所有跟WebView相关的缓存 ，数据库， 历史记录 等。
        this.mAgentWeb?.clearWebCache()
//            Toast.makeText(activity, "已清理缓存", Toast.LENGTH_SHORT).show()
        //清空所有 AgentWeb 硬盘缓存，包括 WebView 的缓存 , AgentWeb 下载的图片 ，视频 ，apk 等文件。
        //            AgentWebConfig.clearDiskCache(this.getContext());
    }

    /**
     * 复制字符串
     *
     * @param context
     * @param text
     */
    private fun toCopy(context: Context, text: String) {
        val mClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        mClipboardManager.primaryClip = ClipData.newPlainText(null, text)
    }


    override fun onResume() {
        super.onResume()
        mAgentWeb?.webLifeCycle?.onResume()//恢复
    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause() //暂停应用内所有WebView ， 调用mWebView.resumeTimers();/mAgentWeb.getWebLifeCycle().onResume(); 恢复。
        super.onPause()
    }

    override fun onFragmentKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (mAgentWeb != null) {
            mAgentWeb!!.handleKeyEvent(keyCode, event)
        } else {
            false
        }
    }


    override fun onDestroy() {
        toCleanWebCache()   //TODO:目前没找到哪里给webview添加了缓存,暂时关闭就清楚缓存,解决error_view不显示bug
        mAgentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

    companion object {
        val tag: String = AgentWebFragment::class.java.simpleName

        fun getInstance(bundle: Bundle?): AgentWebFragment {
            val mAgentWebFragment = AgentWebFragment()
            if (bundle != null) {
                mAgentWebFragment.arguments = bundle
            }
            return mAgentWebFragment
        }
    }
}
