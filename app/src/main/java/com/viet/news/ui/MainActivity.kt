package com.viet.news.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.viet.news.R
import com.viet.news.core.domain.Config
import com.viet.news.core.ui.BaseActivity
import com.viet.news.core.utils.LanguageUtil
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), HasSupportFragmentInjector {
    private var newsFragment: NewsFragment? = null
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        setContentView(R.layout.activity_main)
        setBottomBar()
    }

    //    override fun onBackPressed() {
//        if (!newsFragment?.onBackPressed()!!)
//            super.onBackPressed()
//    }
    private fun setBottomBar() {
        navigation.setupWithNavController(Navigation.findNavController(this, R.id.nav_host_fragment))
//        NavigationUI.setupActionBarWithNavController(this, Navigation.findNavController(this, R.id.nav_host_fragment))
    }

    /*
    * 当配置信息改变而不需要重建activity时调用，配置信息在manifest文件中
    * 例如切换系统语言，针对我们的App，主页也没用keyboard事件，基本上就是切换系统语言才会调用此方法
    */
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        LanguageUtil.onConfigurationChanged(this)
        reLoadView()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val languageChanged = intent?.extras?.getBoolean(Config.LANGUAGE_CHANGED)
        languageChanged?.let {
            if (it) {
                LanguageUtil.onConfigurationChanged(this)
                reLoadView()
            }
        }
    }

    private fun reLoadView() {
        //TODO tsing 如果到时候要在App内切换语言 这里可能需要刷新一下当前界面
    }


    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
    }

}
