package com.viet.mine.utils

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.luck.picture.lib.PictureSelectionModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.viet.mine.R

/**
 * @Description 图片选择器
 * @Author Sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 26/04/2018 4:36 PM
 * @Version 1.0.0
 */
fun Activity.addPic(selectList: List<LocalMedia>?) {
    val create = PictureSelector.create(this)
    configuration(create, selectList)//结果回调onActivityResult code
}

fun Fragment.addPic(selectList: List<LocalMedia>?) {
    val create = PictureSelector.create(activity)
    configuration(create, selectList)
}

private fun configuration(create: PictureSelector, selectList: List<LocalMedia>?) {
    create.openGallery(PictureMimeType.ofImage())
            .build(selectList)
            .forResult(PictureConfig.CHOOSE_REQUEST)
}

fun FragmentActivity.updateHeader(selectList: List<LocalMedia>?) {
    // 进入相册 以下是例子：不需要的api可以不写
    PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            .build(selectList)
            .maxSelectNum(1)// 最大图片选择数量
            .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
            .withAspectRatio(3, 3)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
            .enableCrop(true)// 是否裁剪
            .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
}

fun Fragment.updateHeader(selectList: List<LocalMedia>?) {
    // 进入相册 以下是例子：不需要的api可以不写
    PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            .build(selectList)
            .maxSelectNum(1)// 最大图片选择数量
            .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
            .withAspectRatio(3, 3)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
            .enableCrop(true)// 是否裁剪
            .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
}

fun PictureSelectionModel.build(selectList: List<LocalMedia>?): PictureSelectionModel {
    return theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
            .enableCrop(false)// 是否裁剪
            .maxSelectNum(4)// 最大图片选择数量
            .imageSpanCount(4)// 每行显示个数
            .withAspectRatio(4, 3)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
            .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
            .minSelectNum(1)// 最小选择数量
            .previewImage(true)// 是否可预览图片
            .isCamera(true)// 是否显示拍照按钮
            .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
            .compress(true)// 是否压缩
            .synOrAsy(true)//同步true或异步false 压缩 默认同步
            .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
            .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
            .isGif(true)// 是否显示gif图片
            .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
            .circleDimmedLayer(false)// 是否圆形裁剪
            .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
            .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
            .openClickSound(false)// 是否开启点击声音
            .selectionMedia(selectList)// 是否传入已选图片
            .minimumCompressSize(100)// 小于100kb的图片不压缩

    //.cropCompressQuality(90)// 裁剪压缩质量 默认100
    //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
    //.compressSavePath(getPath())//压缩图片保存地址
    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
    //.videoMaxSecond(15)
    //.videoMinSecond(10)
    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
    //.rotateEnabled() // 裁剪是否可旋转图片
    //.scaleEnabled()// 裁剪是否可放大缩小图片
    //.videoQuality()// 视频录制质量 0 or 1
    //.videoSecond()//显示多少秒以内的视频or音频也可适用
    //.recordVideoSecond()//录制视频秒数 默认60s
}

