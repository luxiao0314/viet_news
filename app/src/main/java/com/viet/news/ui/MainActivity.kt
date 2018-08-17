package com.viet.news.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import androidx.navigation.Navigation
import com.viet.news.core.ui.BaseActivity
import com.viet.news.R
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
    }

//    override fun onBackPressed() {
//        if (!newsFragment?.onBackPressed()!!)
//            super.onBackPressed()
//    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this,R.id.nav_host_fragment).navigateUp()
    }
}
