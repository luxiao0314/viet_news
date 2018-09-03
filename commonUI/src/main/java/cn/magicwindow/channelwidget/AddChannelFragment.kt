package cn.magicwindow.channelwidget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import cn.magicwindow.utils.GridItemDecoration


import cn.magicwindow.channelwidget.viewholder.IChannelType
import cn.magicwindow.channelwidget.entity.ChannelBean


import java.util.ArrayList
import cn.magicwindow.channelwidget.adapter.ChannelAdapter
import cn.magicwindow.commonui.R

@SuppressLint("ValidFragment")
/**
 * @author null
 */
class AddChannelFragment(private val myStrs: List<String>, private val recStrs: List<String>) : DialogFragment(), ChannelAdapter.ChannelItemClickListener {
    private var mContainerView: View? = null
    private var mRecyclerView: RecyclerView? = null
    private var mRecyclerAdapter: ChannelAdapter? = null
    private var channelActivity: Activity? = null
    private var mMyChannelList: MutableList<ChannelBean>? = null
    private var mRecChannelList: MutableList<ChannelBean>? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Activity)
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
        mRecyclerView!!.addItemDecoration(GridItemDecoration(5))
        initData()
        mRecyclerAdapter = ChannelAdapter(context!!, mRecyclerView!!, mMyChannelList!!, mRecChannelList!!, 1, 1)
        mRecyclerAdapter!!.setChannelItemClickListener(this)
        mRecyclerView!!.adapter = mRecyclerAdapter
    }


    private fun initData() {
        mMyChannelList = ArrayList()
        for (i in 0..(myStrs.size - 1)) {
            val channelBean = ChannelBean()
            channelBean.tabName = myStrs[i]
            channelBean.tabType = if (i == 0) 0 else if (i == 1) 1 else 2
            mMyChannelList!!.add(channelBean)
        }
        mRecChannelList = ArrayList()
        for (i in 0..(recStrs.size - 1)) {
            val channelBean = ChannelBean()
            channelBean.tabName = recStrs[i]
            channelBean.tabType = 2
            mRecChannelList!!.add(channelBean)
        }

    }

    override fun onChannelItemClick(list: List<ChannelBean>, position: Int) {
        listener?.let {
            it.onDataChanged(list, position)
            dismiss()
        }
    }

    interface DataChangeListener {
        fun onDataChanged(list: List<ChannelBean>, position: Int)
    }

    fun setOnDataChangeListener(listener: DataChangeListener) {
        this.listener = listener
    }

    var listener: DataChangeListener? = null

}
