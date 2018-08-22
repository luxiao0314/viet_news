package com.viet.news.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import cn.magicwindow.utils.GridItemDecoration


import cn.magicwindow.channelwidget.callback.APPConst
import cn.magicwindow.channelwidget.callback.IChannelType
import cn.magicwindow.channelwidget.entity.ChannelBean


import java.util.ArrayList
import cn.magicwindow.channelwidget.adapter.ChannelAdapter
import com.viet.news.R
import com.viet.news.ui.ChannelActivity

/**
 * @author null
 */
class AddChannelFragment : DialogFragment(), ChannelAdapter.ChannelItemClickListener {
    private var mContainerView: View? = null
    private var mRecyclerView: RecyclerView? = null
    private var mRecyclerAdapter: ChannelAdapter? = null
    private var channelActivity: ChannelActivity? = null
    private val myStrs = arrayOf("热门", "关注", "技术", "科技", "商业", "互联网", "涨知识", "时尚")
    private val recStrs = arrayOf("设计", "天文", "美食", "星座", "历史", "消费维权", "体育", "明星八卦")
    private var mMyChannelList: MutableList<ChannelBean>? = null
    private var mRecChannelList: MutableList<ChannelBean>? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ChannelActivity)
            this.channelActivity = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.layout_tab_edit, container, false)
        return mContainerView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.window!!.setWindowAnimations(R.style.inandoutAnimation)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView = mContainerView!!.findViewById<View>(R.id.id_tab_recycler_view) as RecyclerView
        val gridLayout = GridLayoutManager(context, 4)
        gridLayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val isHeader = mRecyclerAdapter!!.getItemViewType(position) == IChannelType.TYPE_MY_CHANNEL_HEADER || mRecyclerAdapter!!.getItemViewType(position) == IChannelType.TYPE_REC_CHANNEL_HEADER
                return if (isHeader) 4 else 1
            }
        }
        mRecyclerView!!.layoutManager = gridLayout
        mRecyclerView!!.addItemDecoration(GridItemDecoration(APPConst.ITEM_SPACE))
        initData()
        mRecyclerAdapter = ChannelAdapter(context!!, mRecyclerView!!, mMyChannelList!!, mRecChannelList!!, 1, 1)
        mRecyclerAdapter!!.setChannelItemClickListener(this)
        mRecyclerView!!.adapter = mRecyclerAdapter
    }

    private fun initData() {
        mMyChannelList = ArrayList()
        for (i in 0..7) {
            val channelBean = ChannelBean()
            channelBean.tabName = myStrs[i]
            channelBean.tabType = if (i == 0) 0 else if (i == 1) 1 else 2
            mMyChannelList!!.add(channelBean)
        }
        mRecChannelList = ArrayList()
        for (i in 0..7) {
            val channelBean = ChannelBean()
            channelBean.tabName = recStrs[i]
            channelBean.tabType = 2
            mRecChannelList!!.add(channelBean)
        }

    }

    override fun onChannelItemClick(list: List<ChannelBean>, position: Int) {
        if (channelActivity != null) {
            channelActivity!!.notifyTabDataChange(list, position)
            dismiss()
        }
    }
}
