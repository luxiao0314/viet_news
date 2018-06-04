package com.library.aaron.rac.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import androidx.navigation.Navigation
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
