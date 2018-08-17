package com.viet.news.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.viet.news.core.viewmodel.BaseViewModel

class MainViewModel :BaseViewModel(){

    private val result = MutableLiveData<Boolean>()


    public fun getData() {

    }

}