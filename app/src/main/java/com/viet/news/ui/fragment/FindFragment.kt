package com.viet.news.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.magicwindow.channelwidget.AddChannelFragment
import cn.magicwindow.channelwidget.entity.ChannelBean
import com.safframework.ext.click
import com.viet.news.R
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ui.BaseFragment
import com.viet.news.db.SourceEntity
import com.viet.news.viewmodel.FindViewModel
import kotlinx.android.synthetic.main.activity_find.*

/**
 * @Description 任务
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 11:13 AM
 * @Version 1.0.0
 */
class FindFragment : BaseFragment(), AddChannelFragment.DataChangeListener, (SourceEntity) -> Unit {

    private val model: FindViewModel by viewModelDelegate(FindViewModel::class)
    private var myViewPagerAdapter: MyViewPager? = null
    private var mAddChannelFragment: AddChannelFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.activity_find, container, false)
    }

    override fun initView(view: View) {
        id_add_channel_entry_iv.click { mAddChannelFragment?.show(fragmentManager, "addChannel") }
        model.setData()
        initData()
    }

    private fun initData() {
        myViewPagerAdapter = MyViewPager(fragmentManager, model.dataList)
        id_tab_pager_indicator.setDataList(model.dataList)
        id_view_Pager.adapter = myViewPagerAdapter
        id_tab_pager_indicator.setupWithViewPager(id_view_Pager)

        mAddChannelFragment = AddChannelFragment(model.myStrs, model.recStrs)
        mAddChannelFragment?.setOnDataChangeListener(this)
    }

    override fun onDataChanged(list: List<ChannelBean>, position: Int) {
        model.dataList.clear()
        model.dataList.addAll(list)
        myViewPagerAdapter?.notifyDataSetChanged()
        id_tab_pager_indicator.setDataList(model.dataList)
        id_tab_pager_indicator.notifyDataSetChanged()
        id_view_Pager.currentItem = position
    }

    inner class MyViewPager(fm: FragmentManager?, private val mDataList: List<ChannelBean>) : FragmentStatePagerAdapter(fm) {

        private val baseFragmentMap = hashMapOf<Int, LazyFragment>()

        override fun getCount(): Int = mDataList.size

        override fun getItem(position: Int): Fragment? {
            var fragment: LazyFragment? = baseFragmentMap[position]
            if (fragment == null) {
                fragment = if (position % 2 == 0)
                    OneFragment.newInstance()
                else
                    TwoFragment.newInstance(mDataList[position].tabName!!)
                baseFragmentMap[position] = fragment
            }
            return fragment
        }
    }

    //点击事件
    override fun invoke(source: SourceEntity) {
    }

}