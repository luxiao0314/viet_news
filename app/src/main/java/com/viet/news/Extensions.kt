package com.viet.news

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Description
 */
fun ImageView.load(url: String?) {
    get(url).into(this)
}

fun ImageView.get(url: String?): RequestBuilder<Drawable> = Glide.with(context).load(url)
fun ImageView.get(url: Drawable?): RequestBuilder<Drawable> = Glide.with(context).load(url)

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}
