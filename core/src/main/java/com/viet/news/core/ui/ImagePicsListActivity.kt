package com.viet.news.core.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.github.chrisbanes.photoview.OnViewTapListener
import com.github.chrisbanes.photoview.PhotoView
import com.viet.news.core.R
import com.viet.news.core.dsl.setOnPageChangeListener
import com.viet.news.core.ui.progress.CircleProgressView
import com.viet.news.core.ui.progress.GlideImageLoader
import kotlinx.android.synthetic.main.image_pics_list_layout.*
import java.util.*

/**
 * 相册方式浏览图片
 */
class ImagePicsListActivity : BaseActivity() {
    private var mUrls: ArrayList<String>? = null
    private var mPosition: Int = 0
    private var urlistsize: Int = 0
    private var mTitle: String? = null
    private var mContent: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_pics_list_layout)
        makeStatusBarTransparent()
        // 没有任何url时，直接return跳走，UI交互上是用户根本进不来
        val intent = intent
        mUrls = intent.getStringArrayListExtra(KEY_INTENT_DATA_URL)
        mPosition = intent.getIntExtra(KEY_INTENT_DATA_POS, 0)
        mTitle = intent.getStringExtra(KEY_INTENT_DATA_TITLE)
        mContent = intent.getStringExtra(KEY_INTENT_DATA_CONTENT)
        if (!checkIllegal()) return
        urlistsize = mUrls!!.size
        initViews()
    }

    private fun makeStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = null
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    private fun initViews() {
        pager.adapter = MyPagerAdapter(this, mUrls)
        pager.setOnPageChangeListener { onPageSelected = { refreshCurrentPosition(it) } }
        pager.currentItem = mPosition
        pager.offscreenPageLimit = 3
        refreshCurrentPosition(mPosition)

        if (!TextUtils.isEmpty(mContent)) {
            tv_title.visibility = View.VISIBLE
            advert_tv.visibility = View.GONE
            tv_msg_title.text = mTitle
            tv_content.text = mContent
        } else {
            tv_title.visibility = View.GONE
            advert_tv.visibility = View.VISIBLE
        }
    }

    private val mOnPhotoTapListener = OnViewTapListener { view, x, y ->
        if (view is PhotoView) {
            if (view.scale > view.minimumScale) {
                view.setScale(view.minimumScale, true)
            } else {
                finish()
                overridePendingTransition(0, R.anim.scale_in)
            }
        }
    }

    private fun checkIllegal(): Boolean {
        return if (mUrls != null && mUrls!!.size > 0) {
            true
        } else {
            finish()
            overridePendingTransition(0, R.anim.scale_in)
            false
        }
    }

    private inner class MyPagerAdapter(var context: Context, private val mUrls: ArrayList<String>?) : PagerAdapter() {

        override fun getCount(): Int {
            return mUrls?.size ?: 0
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val url = mUrls!![position]
            val contentview = LayoutInflater.from(context).inflate(R.layout.gallery_item, container, false)
            val photoView = contentview.findViewById<PhotoView>(R.id.photoview)
            val progressView = contentview.findViewById<CircleProgressView>(R.id.progressView)
            photoView.setOnViewTapListener(mOnPhotoTapListener)

            val imageLoader = GlideImageLoader.create(photoView)
            imageLoader.setOnGlideImageViewListener(url) { percent, isDone, exception ->
                progressView.progress = percent
                progressView.visibility = if (isDone) View.GONE else View.VISIBLE
            }
            val requestOptions = imageLoader.requestOptions(R.color.color_bg_trans)
                    .centerCrop()
                    .placeholder(R.drawable.image_placeholder)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)

            val requestBuilder = imageLoader.requestBuilder(url, requestOptions).transition(DrawableTransitionOptions.withCrossFade())
            requestBuilder.into(object : SimpleTarget<Drawable>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    if (resource.intrinsicHeight > getScreenHeight(context)) {
                        photoView.scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                    requestBuilder.into(photoView)
                }
            })
            container.addView(contentview)
            return contentview
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        finish()
        overridePendingTransition(0, R.anim.scale_in)
        return super.onKeyDown(keyCode, event)
    }
    private fun refreshCurrentPosition(mPosition: Int) {
        advert_tv.text = (mPosition + 1).toString() + "/$urlistsize"
    }

    fun getScreenHeight(context: Context?): Int {
        val dm = context!!.resources.displayMetrics
        return dm.heightPixels
    }

    companion object {
        const val KEY_INTENT_DATA_URL = "keyImgurls"
        const val KEY_INTENT_DATA_POS = "keyImgposi"
        const val KEY_INTENT_DATA_TITLE = "keyTextTitle"
        const val KEY_INTENT_DATA_CONTENT = "keyTextContent"

        /**
         * 显示图片列表
         *
         * @param context
         * @param urls
         * @param position
         */
        fun entryGallery(context: Context, urls: ArrayList<String>, position: Int) {
            val intent = Intent(context, ImagePicsListActivity::class.java)
            intent.putStringArrayListExtra(KEY_INTENT_DATA_URL, urls)
            intent.putExtra(KEY_INTENT_DATA_POS, position)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.scale_out, 0)
        }

        /**
         * 显示当前图片
         *
         * @param context
         * @param url
         */
        fun entryGallery(context: Context, url: String) {
            val urls = ArrayList<String>()
            urls.add(url)
            val intent = Intent(context, ImagePicsListActivity::class.java)
            intent.putStringArrayListExtra(KEY_INTENT_DATA_URL, urls)
            intent.putExtra(KEY_INTENT_DATA_POS, 0)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.scale_out, 0)
        }

        /**
         * @param context
         * @param url
         * @param title     文章title
         * @param content   文章content
         */
        fun entryGallery(context: Context, url: String, title: String, content: String) {
            val urls = ArrayList<String>()
            urls.add(url)
            val intent = Intent(context, ImagePicsListActivity::class.java)
            intent.putStringArrayListExtra(KEY_INTENT_DATA_URL, urls)
            intent.putExtra(KEY_INTENT_DATA_POS, 0)
            intent.putExtra(KEY_INTENT_DATA_TITLE, title)
            intent.putExtra(KEY_INTENT_DATA_CONTENT, content)
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(R.anim.scale_out, 0)
        }

        fun getUriFromNet(url: String): Uri {
            return if (TextUtils.isEmpty(url)) {
                Uri.parse("")
            } else {
                if (url.contains("https://7xpvcw")) {//七牛的图片,直接用http加载
                    Uri.parse(url.replaceFirst("https".toRegex(), "http"))
                } else {
                    Uri.parse(url)
                }
            }
        }
    }
}
