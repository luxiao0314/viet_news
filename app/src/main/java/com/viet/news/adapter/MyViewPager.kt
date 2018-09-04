package com.viet.news.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import cn.magicwindow.channelwidget.entity.ChannelBean
import com.viet.news.core.ui.RealVisibleHintBaseFragment
import com.viet.news.ui.fragment.NewsFragment
import javax.inject.Inject

class MyViewPager @Inject constructor(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    private val baseFragmentMap = hashMapOf<Int, RealVisibleHintBaseFragment>()

    private var mDataList = listOf<ChannelBean>()

    fun setData(list: List<ChannelBean>) {
        mDataList = list
    }

    override fun getCount(): Int = mDataList.size

    override fun getItem(position: Int): Fragment? {
        var fragment: RealVisibleHintBaseFragment? = baseFragmentMap[position]
        if (fragment == null) {
            fragment = NewsFragment.newInstance(mDataList[position].tabName!!)
            baseFragmentMap[position] = fragment
        }
        return fragment
    }
}