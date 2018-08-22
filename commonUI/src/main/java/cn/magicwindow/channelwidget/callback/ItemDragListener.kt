package cn.magicwindow.channelwidget.callback

/**
 * @author null
 * 条目拖拽监听
 */
interface ItemDragListener {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemSwiped(position: Int)
}
