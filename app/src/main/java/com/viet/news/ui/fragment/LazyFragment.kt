package com.viet.news.ui.fragment

import android.support.v4.app.Fragment
import android.view.View


/**
 * @author null.
 */
abstract class LazyFragment : Fragment() {
    val targetView: View?
        get() = view

    abstract fun initNet()

}
