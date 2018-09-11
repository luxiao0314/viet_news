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
import com.viet.news.core.domain.LoginEvent
import com.viet.news.core.domain.LogoutEvent
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.ui.InjectFragment
import com.viet.news.core.utils.RxBus
import kotlinx.android.synthetic.main.activity_find.*
import javax.inject.Inject

/**
 * @Description 发现
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 11:13 AM
 * @Version 1.0.0
 */
class FindFragment : InjectFragment(), AddChannelFragment.DataChangeListener {

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
        initEvent()
    }

    private fun initEvent() {
        compositeDisposable.add(RxBus.get().register(LogoutEvent::class.java) { initData() })
        compositeDisposable.add(RxBus.get().register(LoginEvent::class.java) { initData() })
    }

    private fun initData() {
        model.getChannelAllList(this){}
        model.getChannelList(this) {
            id_tab_pager_indicator.setDataList(model.normalList)
            pagerAdapter.setData(model.normalList)
        }
    }

    override fun initView(view: View) {
        id_view_Pager.offscreenPageLimit = 5
        id_view_Pager.adapter = pagerAdapter
        id_tab_pager_indicator.setupWithViewPager(id_view_Pager)

        id_add_channel_entry_iv.click {
            mAddChannelFragment = AddChannelFragment(model.followList, model.unFollowList)
            mAddChannelFragment?.setOnDataChangeListener(this)
            mAddChannelFragment?.show(fragmentManager, "addChannel")
        }
    }

    override fun onDataChanged(list: List<ChannelBean>, position: Int) {
        model.normalList.clear()
        model.normalList.addAll(list)
        pagerAdapter.notifyDataSetChanged()
        id_tab_pager_indicator.setDataList(model.normalList)
        id_tab_pager_indicator.notifyDataSetChanged()
        if (position != 100000) {
            id_view_Pager.currentItem = position
        }
    }

    override fun moveMyToOther(list: List<ChannelBean>, position: Int, function: () -> Unit) {
        model.channelAdd(this, list, position) { function() }
    }

    override fun moveOtherToMy(list: List<ChannelBean>, position: Int, function: () -> Unit) {
        model.channelRemove(this, list, position) { function() }
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
            fragment = NewsFragment.newInstance(mDataList[position].channelName!!)
            baseFragmentMap[position] = fragment
        }
        return fragment
    }
}