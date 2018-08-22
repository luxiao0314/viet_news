package com.lcorekit.channeldemo.callback

/**
 * @author null
 */
interface ItemDragListener {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemSwiped(position: Int)
}
