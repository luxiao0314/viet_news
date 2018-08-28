package com.viet.news.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import cn.magicwindow.channelwidget.entity.ChannelBean
import cn.magicwindow.channelwidget.AddChannelFragment
import cn.magicwindow.channelwidget.widget.ChannelTabLayout
import com.viet.news.R
import com.viet.news.ui.fragment.LazyFragment
import com.viet.news.ui.fragment.OneFragment
import com.viet.news.ui.fragment.TwoFragment


import java.util.ArrayList
import java.util.HashMap

class ChannelActivity : AppCompatActivity(), View.OnClickListener, AddChannelFragment.DataChangeListener {

    private var tabLayout: ChannelTabLayout? = null
    private var mViewPager: ViewPager? = null
    private var myViewPagerAdapter: MyViewPager? = null
    private val myStrs = arrayOf("推荐", "热点", "军事", "图片", "社会", "娱乐", "科技", "体育", "深圳", "财经")
    private var mDataList: ArrayList<ChannelBean>? = null
    private var mAddChannelEntryIv: ImageView? = null
    private var mAddChannelFragment: AddChannelFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)
        initView()
        initData()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.id_add_channel_entry_iv -> if (!mAddChannelFragment!!.isAdded) {
                mAddChannelFragment!!.show(supportFragmentManager, "addChannel")
            }
        }
    }

    private fun initView() {
        tabLayout = findViewById(R.id.id_tab_pager_indicator)
        mViewPager = findViewById(R.id.id_view_Pager)
        mAddChannelEntryIv = findViewById(R.id.id_add_channel_entry_iv)
        mAddChannelEntryIv!!.setOnClickListener(this)
    }

    private fun initData() {
        mDataList = ArrayList<ChannelBean>()
        for (i in 0..9) {
            val channelBean = ChannelBean()
            channelBean.tabName = myStrs[i]
            channelBean.tabType = if (i == 0) 0 else if (i == 1) 1 else 2
            mDataList!!.add(channelBean)
        }
        myViewPagerAdapter = MyViewPager(supportFragmentManager, mDataList!!)
        tabLayout!!.setDataList(mDataList!!)
        mViewPager!!.adapter = myViewPagerAdapter
        tabLayout!!.setupWithViewPager(mViewPager)
        val myStrs = arrayOf("热门", "关注", "技术", "科技", "商业", "互联网", "涨知识", "时尚").toList()
        val recStrs = arrayOf("设计", "天文", "美食", "星座", "历史", "消费维权", "体育", "明星八卦").toList()
        mAddChannelFragment = AddChannelFragment(myStrs, recStrs)
        mAddChannelFragment!!.setOnDataChangeListener(this)
    }

    override fun onDataChanged(list: List<ChannelBean>, position: Int) {
        mDataList!!.clear()
        mDataList!!.addAll(list)
        myViewPagerAdapter!!.notifyDataSetChanged()
        tabLayout!!.setDataList(mDataList!!)
        tabLayout!!.notifyDataSetChanged()
        mViewPager!!.currentItem = position
    }

    inner class MyViewPager(fm: FragmentManager, private val mDataList: List<ChannelBean>) : FragmentStatePagerAdapter(fm) {
        @SuppressLint("UseSparseArrays")
        private val baseFragmentMap = HashMap<Int, LazyFragment>()

        override fun getCount(): Int = mDataList.size

        override fun getItem(position: Int): Fragment? {
            var fragment: LazyFragment? = baseFragmentMap[position]
            if (fragment == null) {
                if (position % 2 == 0)
                    fragment = OneFragment.newInstance()
                else
                    fragment = TwoFragment.newInstance(mDataList[position].tabName!!)
                baseFragmentMap.put(position, fragment!!)
            }
            return fragment
        }
    }
}
