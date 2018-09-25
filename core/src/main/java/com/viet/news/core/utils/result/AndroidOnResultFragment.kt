package com.viet.news.core.utils.result

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * @Description 通过创建隐藏的fragment来管理生命周期,通rxPrmission中result使用方法
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 25/09/2018 11:16 AM
 * @Version
 */
class AndroidOnResultFragment : Fragment() {
    private val mSubjects = hashMapOf<Int, PublishSubject<ActivityResultInfo>>()
    private val mCallbacks = hashMapOf<Int, (resultCode: Int, data: Intent?) -> Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun startForResult(intent: Intent): Observable<ActivityResultInfo> {
        val subject = PublishSubject.create<ActivityResultInfo>()
        return subject.doOnSubscribe {
            mSubjects[subject.hashCode()] = subject
            startActivityForResult(intent, subject.hashCode())
        }
    }

    fun startForResult(intent: Intent, callback: (resultCode: Int, data: Intent?) -> Unit) {
        mCallbacks[callback.hashCode()] = callback
        startActivityForResult(intent, callback.hashCode())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        //rxjava方式的处理
        val subject = mSubjects.remove(requestCode)
        if (subject != null) {
            subject.onNext(ActivityResultInfo(resultCode, data))
            subject.onComplete()
        }
        //callback方式的处理
        val callback = mCallbacks.remove(requestCode)
        callback?.let { it(resultCode, data) }
    }

}