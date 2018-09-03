package com.viet.news.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.viet.news.R
import com.viet.news.core.ui.BaseFragment
import com.viet.news.db.SourceEntity
import com.viet.news.viewmodel.NewsViewModel

/**
 * @Description 任务
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 11:13 AM
 * @Version 1.0.0
 */
class FollowFragment : BaseFragment(), (SourceEntity) -> Unit {

    private lateinit var newsViewModel: NewsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_follow, container, false)
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)
//        progressDialog = ProgressDialog.show(activity, "News API", "Loading News Source from Web-Service")
//        progressDialog.show()
        return view
    }

    override fun initView(view: View) {
        Log.e("", "initView")
    }

    //点击事件
    override fun invoke(source: SourceEntity) {
        Log.e("News", "invoke source:$source.id")
    }

}