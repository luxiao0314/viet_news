package com.library.aaron.rac.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.library.aaron.core.ui.BaseFragment
import com.library.aaron.rac.R
import com.library.aaron.rac.adapter.NewsArticleAdapter
import com.library.aaron.rac.adapter.NewsSourceAdapter
import com.library.aaron.rac.db.SourceEntity
import com.library.aaron.rac.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_news.*

/**
 * Created by abhinav.sharma on 01/11/17.
 */
class NewsFragment : BaseFragment(), (SourceEntity) -> Unit {


    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsSourceAdapter: NewsSourceAdapter
    private lateinit var newsArticleAdapter: NewsArticleAdapter
    private val sourceList = ArrayList<SourceEntity>()
//    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_news, container, false)
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)
//        progressDialog = ProgressDialog.show(activity, "News API", "Loading News Source from Web-Service")
//        progressDialog.show()
        return view
    }

    override fun initView(view: View) {
        Log.e("", "initView")

        newsSourceAdapter = NewsSourceAdapter(this, sourceList)
        recyclerView.adapter = newsSourceAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        newsViewModel.getNewsSource(null, null, null)
                .observe(this, Observer { newsSource ->
                    if (newsSource?.data != null && newsSource.data!!.isNotEmpty()) {
                        multiStatusView.showContent()

                        newsSourceAdapter.updateDataSet(newsSource.data!!)
                    } else {
                        multiStatusView.showEmpty()
                    }

                })
    }

    //点击事件
    override fun invoke(source: SourceEntity) {
        Log.e("News", "invoke source:$source.id")
        newsViewModel.getNewsArticles(source.id, null)
                .observe(this, Observer {
                    if (it?.data != null) {
                        newsArticleAdapter = NewsArticleAdapter(it.data!!.articles!!)
                        recyclerView.adapter = newsArticleAdapter
                    }
                })
    }

    fun onBackPressed(): Boolean {
        return when {
            recyclerView.adapter is NewsArticleAdapter -> {
                recyclerView.adapter = newsSourceAdapter
                true
            }
            else -> false
        }
    }
}