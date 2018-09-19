package cn.magicwindow.channelwidget


import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.GridLayoutManager
import android.view.*
import cn.magicwindow.channelwidget.adapter.ChannelAdapter
import cn.magicwindow.channelwidget.entity.ChannelBean
import cn.magicwindow.channelwidget.viewholder.IChannelType
import cn.magicwindow.commonui.R
import cn.magicwindow.utils.GridItemDecoration
import com.app.hubert.guide.util.ScreenUtils.getScreenWidth
import kotlinx.android.synthetic.main.layout_tab_edit.*

@SuppressLint("ValidFragment")
/**
 * @author null
 */
class AddChannelFragment(private var mMyChannelList: MutableList<ChannelBean>, private var mRecChannelList: MutableList<ChannelBean>) : DialogFragment(), ChannelAdapter.ChannelItemClickListener, DialogInterface.OnKeyListener {

    override fun onStart() {
        super.onStart()
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
        return inflater.inflate(R.layout.layout_tab_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ChannelAdapter(context, id_tab_recycler_view, mMyChannelList, mRecChannelList, 1, 1)
        adapter.setChannelItemClickListener(this)
        id_tab_recycler_view.adapter = adapter
        val gridLayout = GridLayoutManager(context, 3)
        gridLayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val isHeader = adapter.getItemViewType(position) == IChannelType.TYPE_MY_CHANNEL_HEADER || adapter.getItemViewType(position) == IChannelType.TYPE_REC_CHANNEL_HEADER
                return if (isHeader) 3 else 1
            }
        }
        id_tab_recycler_view.layoutManager = gridLayout
        id_tab_recycler_view.addItemDecoration(GridItemDecoration(5))
        dialog?.setOnKeyListener(this)
    }

    override fun onCloseClick(list: List<ChannelBean>) {
        followList = list
        if (dataChange) listener?.dataChangeListener(followList, 100000) //100000表示不移动新闻列表中tab位置
        dismiss()
    }

    override fun onKey(p0: DialogInterface?, p1: Int, p2: KeyEvent?): Boolean {
        return if (p1 == KeyEvent.KEYCODE_BACK) {
            if (dataChange) listener?.dataChangeListener(followList, 100000)
            dismiss()
            true
        } else {
            //这里注意当不是返回键时需将事件扩散，否则无法处理其他点击事件
            false
        }
    }

    /**
     * 不编辑下点我的刷新,需要更新新闻
     */
    override fun clickMyChannel(list: List<ChannelBean>, position: Int) {
        this.followList = list
        listener?.dataChangeListener(followList, position)
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        if (dataChange) {
            listener?.onChannelItemMoved(followList) { super.onDismiss(dialog) }
        } else {
            super.onDismiss(dialog)
        }
    }

    var followList = listOf<ChannelBean>()
    var dataChange = false
    //不需要刷新新闻列表
    override fun onChannelItemMoved(list: List<ChannelBean>, position: Int) {
        this.followList = list
        this.dataChange = true
    }

    interface DataChangeListener {
        fun dataChangeListener(list: List<ChannelBean>, position: Int)
        fun onChannelItemMoved(list: List<ChannelBean>, function: () -> Unit)
    }

    fun setOnDataChangeListener(listener: DataChangeListener) {
        this.listener = listener
    }

    var listener: DataChangeListener? = null

}
