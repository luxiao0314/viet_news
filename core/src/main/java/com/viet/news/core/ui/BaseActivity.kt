package com.viet.news.core.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.MenuItem
import com.jaeger.library.StatusBarUtil
import com.safframework.utils.support
import com.viet.news.core.R
import com.viet.news.core.ext.finishWithAnim
import com.viet.news.core.utils.LanguageUtil


abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageUtil.checkLocalLanguage(this)
        setStatusBar()
    }

    protected open fun setStatusBar() {
        //沉浸式菜单栏的lib 国外版可以不设置状态栏 通过背景实现沉浸式菜单栏效果
        support(Build.VERSION_CODES.M, {
            //大于M才能修改为亮底黑字的状态栏
            StatusBarUtil.setLightMode(this)
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.page_bg), 0)
        }, {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.page_bg), 30)
        })
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LanguageUtil.setLocal(newBase))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    fun addFragment(fragment: BaseFragment, layoutResId: Int, tag: String) {
        supportFragmentManager.beginTransaction()
                .add(layoutResId, fragment, tag)
                .commit()
    }

    fun addFragmentWithBackStack(fragment: BaseFragment, layoutResId: Int, tag: String) {
        supportFragmentManager.beginTransaction()
                .add(layoutResId, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean = if (KeyEvent.KEYCODE_BACK == keyCode) {
        if (supportFragmentManager.backStackEntryCount == 0) {
            finishWithAnim()
            true
        } else {
            try {
                supportFragmentManager.popBackStackImmediate()
            } catch (e: Exception) {
            }
            true
        }
    } else {
        super.onKeyDown(keyCode, event)
    }
}
