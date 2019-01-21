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

package com.viet.news.core.utils


import android.arch.lifecycle.LiveData
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.api.HttpResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A Retrofit adapter that converts the Call into a LiveData of ApiResponse.
 * @param <R>
</R> */
class LiveDataCallAdapter<R>(private val responseType: Type) :
    CallAdapter<HttpResponse<R>, LiveData<ApiResponse<R>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<HttpResponse<R>>): LiveData<ApiResponse<R>> {
        return object : LiveData<ApiResponse<R>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {   //当remove掉LiveData的时候,下面的对象被直接销毁,所以网络被中断
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<HttpResponse<R>> {
                        override fun onResponse(call: Call<HttpResponse<R>>, response: Response<HttpResponse<R>>) {
                            postValue(ApiResponse.create(response))
                        }

                        override fun onFailure(call: Call<HttpResponse<R>>, throwable: Throwable) {
                            postValue(ApiResponse.create(throwable))
                        }
                    })
                }
            }

            /**
             * 上面的onActive似乎不会执行,当remove掉LiveData的时候mActive=false,未验证
             *  if (wasInactive && mActive) {
                    onActive();
                }
             * 取消的时候需要手动cancel retrofit的call,底层会cancel okhttp的call
             */
            override fun onInactive() {
                super.onInactive()
                call.cancel()
            }
        }
    }
}
