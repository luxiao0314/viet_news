package com.viet.news.core.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.viet.news.core.R;
import com.viet.news.core.ui.progress.CircleProgressView;
import com.viet.news.core.ui.progress.GlideImageLoader;
import com.viet.news.core.ui.progress.OnGlideImageViewListener;

import java.util.ArrayList;

/**
 * 相册方式浏览图片
 */
public class ImagePicsListActivity extends BaseActivity {
    static final String TAG = ImagePicsListActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private ArrayList<String> mUrls;
    private TextView mAdvertv;
    private int mPosition, urlistsize;
    public static final String KEY_INTENT_DATA_URL = "keyImgurls";
    public static final String KEY_INTENT_DATA_POS = "keyImgposi";
    public static final String KEY_INTENT_DATA_TITLE = "keyTextTitle";
    public static final String KEY_INTENT_DATA_BRAND_NAME = "keyTextBrandName";
    public static final String KEY_INTENT_DATA_CONTENT = "keyTextContent";
    private String mTitle;
    private String mContent;
    private TextView mTvMsgTitle;
    private TextView mTvContent;
//    private TitleView mTvTitle;
    private String mBrandName;

    /**
     * 显示图片列表
     *
     * @param context
     * @param urls
     * @param position
     */
    public static void entryGallery(Context context, ArrayList<String> urls, int position) {
        Intent intent = new Intent(context, ImagePicsListActivity.class);
        intent.putStringArrayListExtra(KEY_INTENT_DATA_URL, urls);
        intent.putExtra(KEY_INTENT_DATA_POS, position);
        context.startActivity(intent);
//        ((Activity) context).overridePendingTransition(R.anim.a5, 0);
    }

    /**
     * 显示当前图片
     *
     * @param context
     * @param url
     */
    public static void entryGallery(Context context, String url) {
        ArrayList<String> urls = new ArrayList<>();
        urls.add(url);
        Intent intent = new Intent(context, ImagePicsListActivity.class);
        intent.putStringArrayListExtra(KEY_INTENT_DATA_URL, urls);
        intent.putExtra(KEY_INTENT_DATA_POS, 0);
        context.startActivity(intent);
//        ((Activity) context).overridePendingTransition(R.anim.a5, 0);
    }

    /**
     * @param context
     * @param url
     * @param brandName 品牌名
     * @param title     文章title
     * @param content   文章content
     */
    public static void entryGallery(Context context, String url, String brandName, String title, String content) {
        ArrayList<String> urls = new ArrayList<>();
        urls.add(url);
        Intent intent = new Intent(context, ImagePicsListActivity.class);
        intent.putStringArrayListExtra(KEY_INTENT_DATA_URL, urls);
        intent.putExtra(KEY_INTENT_DATA_POS, 0);
        intent.putExtra(KEY_INTENT_DATA_TITLE, title);
        intent.putExtra(KEY_INTENT_DATA_BRAND_NAME, brandName);
        intent.putExtra(KEY_INTENT_DATA_CONTENT, content);
        context.startActivity(intent);
//        ((Activity) context).overridePendingTransition(R.anim.a5, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_pics_list_layout);
        makeStatusBarTransparent();
        // 没有任何url时，直接return跳走，UI交互上是用户根本进不来
        Intent intent = getIntent();
        mUrls = intent.getStringArrayListExtra(KEY_INTENT_DATA_URL);
        mPosition = intent.getIntExtra(KEY_INTENT_DATA_POS, 0);
        mTitle = intent.getStringExtra(KEY_INTENT_DATA_TITLE);
        mContent = intent.getStringExtra(KEY_INTENT_DATA_CONTENT);
        mBrandName = intent.getStringExtra(KEY_INTENT_DATA_BRAND_NAME);
        if (!checkIllegal()) {
            finish();
//            overridePendingTransition(0, R.anim.a3);
            return;
        }
        urlistsize = mUrls.size();
        initViews();
    }

    private void makeStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(null);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    private void initViews() {
        mAdvertv = (TextView) findViewById(R.id.advert_tv);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mTvMsgTitle = (TextView) findViewById(R.id.tv_msg_title);
//        mTvTitle = (TitleView) findViewById(R.id.tv_title);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        mViewPager.setAdapter(new MyPagerAdapter(this, mUrls));
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.setOffscreenPageLimit(3);
        refreshCurrentPosition(mPosition);

        if (!TextUtils.isEmpty(mContent)) {
//            mTvTitle.setVisibility(View.VISIBLE);
            mAdvertv.setVisibility(View.GONE);
            mTvMsgTitle.setText(mTitle);
            mTvContent.setText(mContent);
//            mTvTitle.setTitleText(mBrandName);
        } else {
//            mTvTitle.setVisibility(View.GONE);
            mAdvertv.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkIllegal() {
        return mUrls != null && mUrls.size() > 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            refreshCurrentPosition(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class MyPagerAdapter extends PagerAdapter {
        private Context mContext = null;
        private ArrayList<String> mUrls;

        public MyPagerAdapter(Context context, ArrayList<String> urls) {
            this.mContext = context;
            this.mUrls = urls;
        }

        public int getCount() {
            return mUrls == null ? 0 : mUrls.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String url = mUrls.get(position);
            View contentview = LayoutInflater.from(mContext).inflate(R.layout.gallery_item, container, false);
            final PhotoView photoView = contentview.findViewById(R.id.photoview);
            final CircleProgressView progressView = contentview.findViewById(R.id.progressView);
            photoView.setOnViewTapListener(mOnPhotoTapListener);

            GlideImageLoader imageLoader = GlideImageLoader.create(photoView);
            imageLoader.setOnGlideImageViewListener(url, new OnGlideImageViewListener() {
                @Override
                public void onProgress(int percent, boolean isDone, GlideException exception) {
                    progressView.setProgress(percent);
                    progressView.setVisibility(isDone ? View.GONE : View.VISIBLE);
                }
            });
            RequestOptions requestOptions = imageLoader.requestOptions(R.color.color_bg_trans)
                    .centerCrop()
                    .placeholder(R.drawable.image_placeholder)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

            final RequestBuilder<Drawable> requestBuilder = imageLoader.requestBuilder(url, requestOptions)
                    .transition(DrawableTransitionOptions.withCrossFade());
            requestBuilder.into(new SimpleTarget<Drawable>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (resource.getIntrinsicHeight() > getScreenHeight(mContext)) {
                        photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    requestBuilder.into(photoView);
                }
            });
            container.addView(contentview);
            return contentview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    public static Uri getUriFromNet(String url) {
        if (TextUtils.isEmpty(url)) {
            return Uri.parse("");
        } else {
            if (url.contains("https://7xpvcw")) {//七牛的图片,直接用http加载
                return Uri.parse(url.replaceFirst("https", "http"));
            } else {
                return Uri.parse(url);
            }
        }
    }

    private OnViewTapListener mOnPhotoTapListener = new OnViewTapListener() {
        @Override
        public void onViewTap(View view, float x, float y) {
            if (view instanceof PhotoView) {
                PhotoView photoView = (PhotoView) view;
                if (photoView.getScale() > photoView.getMinimumScale()) {
                    photoView.setScale(photoView.getMinimumScale(), true);
                } else {
                    finish();
//                    overridePendingTransition(0, R.anim.a3);
                }
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
//        overridePendingTransition(0, R.anim.a3);
        return super.onKeyDown(keyCode, event);
    }

    void refreshCurrentPosition(int mPosition) {
        mAdvertv.setText("" + (mPosition + 1) + "/" + urlistsize);
    }

    public int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
