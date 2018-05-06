package com.library.aaron.module.ui

import android.os.Bundle
import com.library.aaron.core.ui.BaseActivity
import com.library.aaron.module.R

class MainActivity : BaseActivity() {
    private var newsFragment : NewsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showSourceFragment()
    }

    private fun showSourceFragment() {
        newsFragment = NewsFragment()
        addFragment(newsFragment!!, R.id.container, "NewsFragment")
    }

    override fun onBackPressed() {
        if (!newsFragment?.onBackPressed()!!)
            super.onBackPressed()
    }
}
