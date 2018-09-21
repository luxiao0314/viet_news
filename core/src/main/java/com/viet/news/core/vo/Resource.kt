/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.viet.news.core.vo

import android.support.v4.app.FragmentActivity
import com.viet.news.core.config.IActivityManager
import com.viet.news.core.ext.toast
import com.viet.news.dialog.ProgressDialogFragment

/**
 * A generic class that holds a value with its loading status.
 *
 * @param <T>
</T> */
class Resource<T>(private var status: Status, var data: T? = null, var message: String? = "") {

    /**
     * lambda会调用最后一个参数，因此成功放最后
     * 例如 this.work { ... }
     * 1,onError:默认使用toast(message),如果重写了onError方法,需要自行写toast
     * 2,onLoading:默认不弹loading,需要则onLoading = { true }
     * 3,resource:更换为只在初始化laoding创建,error和success采用同一对象,否则dialog无法取消
     */
    fun work(onLoading: () -> Boolean = { false }, onError: (message: String?) -> Unit = { toast(message) }, onSuccess: () -> Unit) {
        when (status) {
            Status.LOADING -> if (onLoading()) {
                dialog = ProgressDialogFragment.create(IActivityManager.lastActivity() as FragmentActivity) as ProgressDialogFragment
                dialog?.setOnCancelListener { cancelListener?.let { it() } }
            }
            Status.ERROR -> {
                onError(message)
                dialog?.dismissAllowingStateLoss()
            }
            Status.SUCCESS -> {
                onSuccess()
                dialog?.dismissAllowingStateLoss()
            }
        }
    }

    var dialog: ProgressDialogFragment? = null

    fun success(data: T?): Resource<T> {
        this.status = Status.SUCCESS
        this.data = data
        return this
    }

    fun error(message: String): Resource<T>? {
        this.message = message
        this.status = Status.ERROR
        return this
    }

    fun loading(): Resource<T>? {
        this.status = Status.LOADING
        return this
    }

    override fun toString(): String = "Resource(status=$status, data=$data, message=$message)"

    override fun hashCode(): Int {
        var result = status.hashCode()
        result = 27 * result + (data?.hashCode() ?: 0)
        result = 27 * result + (message?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Resource<*>

        if (status != other.status) return false
        if (data != other.data) return false
        if (message != other.message) return false

        return true
    }

    var cancelListener: (() -> Unit?)? = null
    fun setOnCancelListener(cancelListener: () -> Unit) {
        this.cancelListener = cancelListener
    }
}
