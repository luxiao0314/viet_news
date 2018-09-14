package com.viet.news.core.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.viet.news.core.api.ApiEmptyResponse
import com.viet.news.core.api.ApiErrorResponse
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.api.ApiSuccessResponse
import com.viet.news.core.vo.Resource
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Description
 */
abstract class NetworkBoundResource<ResultType, RequestType> @MainThread constructor() {

    private val result = MediatorLiveData<Resource<ResultType>>()

    fun asLiveData(): LiveData<Resource<ResultType>> = result

    init {
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        result.addSource(dbSource) { resultType ->
            result.removeSource(dbSource)
            if (shouldFetch(resultType)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { rT -> result.value = Resource.success(rT) }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        result.addSource(dbSource) { newData -> setValue(Resource.loading(newData)) }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    if (response.body == null) {
                        result.value = Resource.success(null)
                        return@addSource
                    }
                    processResponse(response)?.let { Observable.fromCallable { saveCallResult(it) }.subscribeOn(Schedulers.io()).subscribe() }
                    result.addSource(loadFromDb()) { resultType -> result.value = Resource.success(resultType) }
                }
                is ApiEmptyResponse -> result.addSource(loadFromDb()) { newData -> result.value = Resource.success(newData) }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) { resultType ->
                        result.value = response.errorMessage.let { Resource.error(resultType, it) }
                    }
                }
            }
        }
    }

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    // 将网络获取的数据存储到db
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    // 是否需要从网络重新获取
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    // 从db内获取cache数据
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    // 从网络中获取
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    // 网络获取失败
    @MainThread
    protected open fun onFetchFailed() {
    }

}