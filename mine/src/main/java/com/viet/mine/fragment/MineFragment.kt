package com.viet.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.safframework.ext.clickWithTrigger
import com.viet.mine.R
import com.viet.news.core.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_mine.*


/**
 * @author null
 */
class MineFragment : BaseFragment() {

    private var mContainerView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
        click_to_login.clickWithTrigger { Navigation.findNavController(it).navigate(R.id.loginActivity) }
    }
}
