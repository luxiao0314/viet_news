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
import com.jaeger.library.StatusBarUtil
import com.safframework.ext.click
import com.viet.follow.fragment.NewsFragment
import com.viet.follow.viewmodel.FindViewModel
import com.viet.news.R
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.ui.InjectFragment
import com.viet.news.db.SourceEntity
import kotlinx.android.synthetic.main.activity_find.*
import javax.inject.Inject

/**
 * @Description 发现
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 11:13 AM
 * @Version 1.0.0
 */
class FindFragment : InjectFragment(), AddChannelFragment.DataChangeListener, (SourceEntity) -> Unit {

    @Inject
    internal lateinit var pagerAdapter: MyViewPager
    private val model: FindViewModel by viewModelDelegate(FindViewModel::class)

    private var mAddChannelFragment: AddChannelFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.activity_find, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.setTranslucentForImageViewInFragment(activity, 30, null)
        initData()
    }

    override fun initView(view: View) {
        id_view_Pager.offscreenPageLimit = 5
        id_view_Pager.adapter = pagerAdapter
        id_tab_pager_indicator.setupWithViewPager(id_view_Pager)

        mAddChannelFragment = AddChannelFragment(model.myStrs, model.recStrs)
        mAddChannelFragment?.setOnDataChangeListener(this)
        id_add_channel_entry_iv.click { mAddChannelFragment?.show(fragmentManager, "addChannel") }
    }

    private fun initData() {
        pagerAdapter.setData(model.dataList)
        id_tab_pager_indicator.setDataList(model.dataList)
//        model.getChannelList(this) {
//            pagerAdapter.setData(model.dataList)
//            id_tab_pager_indicator.setDataList(model.dataList)
//            pagerAdapter.notifyDataSetChanged()
//        }
    }

    override fun onDataChanged(list: List<ChannelBean>, position: Int) {
        model.dataList.clear()
        model.dataList.addAll(list)
        pagerAdapter.notifyDataSetChanged()
        id_tab_pager_indicator.setDataList(model.dataList)
        id_view_Pager.currentItem = position
    }

    //点击事件
    override fun invoke(source: SourceEntity) {
    }
}

class MyViewPager @Inject constructor(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    private val baseFragmentMap = hashMapOf<Int, BaseFragment>()

    private var mDataList = listOf<ChannelBean>()

    fun setData(list: List<ChannelBean>) {
        mDataList = list
        notifyDataSetChanged()
    }

    override fun getCount(): Int = mDataList.size

    override fun getItem(position: Int): Fragment? {
        var fragment: BaseFragment? = baseFragmentMap[position]
        if (fragment == null) {
            fragment = NewsFragment.newInstance(mDataList[position].tabName!!)
            baseFragmentMap[position] = fragment
        }
        return fragment
    }
}