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

package com.viet.news.core.api

import android.util.Log
import com.viet.news.core.config.Config
import com.viet.news.core.domain.GlobalNetworkException
import com.viet.news.core.utils.RxBus
import retrofit2.Response
import java.util.regex.Pattern

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            //网络请求无响应
            RxBus.get().post(GlobalNetworkException(Config.NETWORK_RESPONSE_HAS_NO_NETWORK, null))
            return ApiErrorResponse(error.message ?: "unknown error")
        }

        fun <T> create(response: Response<HttpResponse<T>>): ApiResponse<T> {
            //网络请求有有响应
            if (response.isSuccessful) {
                val body = response.body()
                return when {
                //请求无结果
                    body == null -> apiErrorResponse(response)
                //204
                    response.code() == 204 -> ApiEmptyResponse()
                //code!=0
                    body.code != Config.NETWORK_RESPONSE_OK -> {
                        RxBus.get().post(GlobalNetworkException(body.code, body))
                        ApiErrorResponse(body.message ?: "unknown error")
                    }
                //成功
                    else -> ApiSuccessResponse(
                            body = body.data,
                            linkHeader = response.headers()?.get("link")
                    )
                }
            } else {
                //网络请求失败
                return apiErrorResponse(response)
            }
        }

        private fun <T> apiErrorResponse(response: Response<HttpResponse<T>>): ApiErrorResponse<T> {
            val msg = response.errorBody()?.string()
            val errorMsg = if (msg.isNullOrEmpty()) {
                response.message()
            } else {
                msg
            }
            RxBus.get().post(GlobalNetworkException(Config.NETWORK_RESPONSE_HAS_NO_NETWORK, null))
            return ApiErrorResponse(errorMsg ?: "unknown error")
        }
    }
}

/**
 * separate class for HTTP 204 resposes so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T?, val links: Map<String, String>) : ApiResponse<T>() {

    constructor(body: T?, linkHeader: String?) : this(body = body, links = linkHeader?.extractLinks() ?: emptyMap())

    val nextPage: Int? by lazy(LazyThreadSafetyMode.NONE) {
        links[NEXT_LINK]?.let { next ->
            val matcher = PAGE_PATTERN.matcher(next)
            if (!matcher.find() || matcher.groupCount() != 1) {
                null
            } else {
                try {
                    Integer.parseInt(matcher.group(1))
                } catch (ex: NumberFormatException) {
                    Log.w("ApiSuccessResponse", "cannot parse next page from $next")
                    null
                }
            }
        }
    }

    companion object {
        private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        private const val NEXT_LINK = "next"

        private fun String.extractLinks(): Map<String, String> {
            val links = mutableMapOf<String, String>()
            val matcher = LINK_PATTERN.matcher(this)

            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    links[matcher.group(2)] = matcher.group(1)
                }
            }
            return links
        }

    }
}