package com.viet.news.core.viewmodel

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    protected var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        cancel()
    }

    protected fun cancel() {
        compositeDisposable.clear()
    }
}
