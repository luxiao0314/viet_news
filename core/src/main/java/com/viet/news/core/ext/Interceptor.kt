package com.viet.news.core.ext

import android.support.annotation.IdRes

/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Description
 */
abstract class Interceptor {

    /**
     * 是否满足检验器的要求
     * @return
     */
    abstract fun skip(): Boolean

    @IdRes
    abstract fun getResId():Int
}
