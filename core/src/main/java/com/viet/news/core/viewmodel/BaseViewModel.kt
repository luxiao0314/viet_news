package com.viet.news.core.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.viet.news.core.config.Config
import com.viet.news.core.domain.ErrorEnvelope
import com.viet.news.core.domain.ServiceException
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    protected val error = MutableLiveData<ErrorEnvelope>()
    protected val progress = MutableLiveData<Boolean>()
    protected var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        cancel()
    }

    protected fun cancel() {
        compositeDisposable.clear()
    }

    fun error(): LiveData<ErrorEnvelope> {
        return error
    }

    fun progress(): LiveData<Boolean> {
        return progress
    }

    protected fun onError(throwable: Throwable) {
        if (throwable is ServiceException) {
            error.postValue(throwable.error)
        } else {
            error.postValue(ErrorEnvelope(Config.ErrorCode.UNKNOWN, null, throwable))
            // TODO: Add dialog with offer send error log to developers: notify about error.
            Log.d("SESSION", "Err", throwable)
        }
    }
}
