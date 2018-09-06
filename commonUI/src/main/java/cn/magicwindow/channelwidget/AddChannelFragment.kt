package cn.magicwindow.channelwidget


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import cn.magicwindow.channelwidget.adapter.ChannelAdapter
import cn.magicwindow.channelwidget.entity.ChannelBean
import cn.magicwindow.channelwidget.viewholder.IChannelType
import cn.magicwindow.commonui.R
import cn.magicwindow.utils.GridItemDecoration
import com.app.hubert.guide.util.ScreenUtils.getScreenWidth
import kotlinx.android.synthetic.main.layout_tab_edit.*
import java.util.*

@SuppressLint("ValidFragment")
/**
 * @author null
 */
class AddChannelFragment(private val myStrs: List<String>, private val recStrs: List<String>) : DialogFragment(), ChannelAdapter.ChannelItemClickListener {
    private var mContainerView: View? = null
    private var mMyChannelList = mutableListOf<ChannelBean>()
    private var mRecChannelList = mutableListOf<ChannelBean>()

    override fun onStart() {
        super.onStart()
        initParams()
    }

    private fun initParams() {
        val window = dialog?.window
        val lp = window?.attributes
//        lp?.dimAmount = 0F
        //占用屏幕宽度一定比例
        lp?.width = getScreenWidth(context)
        //设置dialog高度
        lp?.height = WindowManager.LayoutParams.MATCH_PARENT
        //设置dialog进入、退出的动画
        window?.attributes = lp
        window?.setWindowAnimations(R.style.inandoutAnimation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.layout_tab_edit, container, false)
        return mContainerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        val adapter = ChannelAdapter(context, id_tab_recycler_view, mMyChannelList, mRecChannelList, 1, 1)
        adapter.setChannelItemClickListener(this)
        id_tab_recycler_view.adapter = adapter
        val gridLayout = GridLayoutManager(context, 4)
        gridLayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val isHeader = adapter.getItemViewType(position) == IChannelType.TYPE_MY_CHANNEL_HEADER || adapter.getItemViewType(position) == IChannelType.TYPE_REC_CHANNEL_HEADER
                return if (isHeader) 4 else 1
            }
        }
        id_tab_recycler_view.layoutManager = gridLayout
        id_tab_recycler_view.addItemDecoration(GridItemDecoration(5))
    }


    private fun initData() {
        mMyChannelList = ArrayList()
        for (i in 0..(myStrs.size - 1)) {
            val channelBean = ChannelBean()
            channelBean.tabName = myStrs[i]
            channelBean.tabType = if (i == 0) 0 else if (i == 1) 1 else 2
            mMyChannelList.add(channelBean)
        }
        mRecChannelList = ArrayList()
        for (i in 0..(recStrs.size - 1)) {
            val channelBean = ChannelBean()
            channelBean.tabName = recStrs[i]
            channelBean.tabType = 2
            mRecChannelList.add(channelBean)
        }

    }

    override fun onCloseClick() {
        dismiss()
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
