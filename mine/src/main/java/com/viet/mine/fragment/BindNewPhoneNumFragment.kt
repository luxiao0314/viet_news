package com.viet.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.viet.mine.R
import com.viet.news.core.ui.BaseFragment

/**
 * @Description
 * @author null
 * @date 2018/9/10
 * @Email zongjia.long@merculet.io
 * @Version
 */
class BindNewPhoneNumFragment : BaseFragment() {
    private var mContainerView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_bind_new_phone_num, container, false)
        return mContainerView
    }
    override fun initView(view: View) {

    }
}