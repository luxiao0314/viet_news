package com.library.aaron.rac.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import com.library.aaron.core.ui.BaseActivity
import com.library.aaron.rac.R
import com.library.aaron.rac.db.SourceEntity
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : BaseActivity(), HasSupportFragmentInjector {
    private var newsFragment: NewsFragment? = null
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
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

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}
