/*
 *  Android Wheel Control.
 *  https://code.google.com/p/android-wheel/
 *
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.magicwindow.wheelview

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.animation.Interpolator
import android.widget.LinearLayout
import cn.magicwindow.commonui.R
import cn.magicwindow.wheelview.adapter.WheelViewAdapter
import java.util.*


/**
 * Numeric wheel view.
 *
 * @author Yuri Kanivets
 */
class WheelView : View {

    /** Top and bottom shadows colors  */
    private var SHADOWS_COLORS = intArrayOf(-0xeeeeef, 0x00AAAAAA, 0x00AAAAAA)

    // Wheel Values
    private var currentItem = 0

    // Count of visible items
    /**
     * Gets count of visible items
     *
     * @return the count of visible items
     */
    /**
     * Sets the desired count of visible items.
     * Actual amount of visible items depends on wheel layout parameters.
     * To apply changes and rebuild view call measure().
     *
     * @param count the desired count for visible items
     */
    var visibleItems = DEF_VISIBLE_ITEMS

    // Item height
    private var itemHeight = 0

    // Center Line
    private var centerDrawable: Drawable? = null

    // Wheel drawables
    private var wheelBackground = R.drawable.wheel_bg
    private var wheelForeground = R.drawable.wheel_val

    // Shadows drawables
    private var topShadow: GradientDrawable? = null
    private var bottomShadow: GradientDrawable? = null

    // Draw Shadows
    private var drawShadows = true

    // Scrolling
    private var scroller: WheelScroller? = null
    private var isScrollingPerformed: Boolean = false
    private var scrollingOffset: Int = 0

    // Cyclic
    internal var isCyclic = false

    // Items layout
    private var itemsLayout: LinearLayout? = null

    // The number of first item in layout
    private var firstItem: Int = 0


    private var divLineColor: Int = 0

    // View adapter
    /**
     * Gets view adapter
     * @return the view adapter
     */
    /**
     * Sets view adapter. Usually new adapters contain different views, so
     * it needs to rebuild view by calling measure().
     *
     * @param viewAdapter the view adapter
     */
    var viewAdapter: WheelViewAdapter? = null
        set(viewAdapter) {
            if (this.viewAdapter != null) {
                this.viewAdapter!!.unregisterDataSetObserver(dataObserver)
            }
            field = viewAdapter
            if (this.viewAdapter != null) {
                this.viewAdapter!!.registerDataSetObserver(dataObserver)
            }

            invalidateWheel(true)
        }

    // Recycle
    private val recycle = WheelRecycle(this)

    // Listeners
    private val changingListeners = LinkedList<OnWheelChangedListener>()
    private val scrollingListeners = LinkedList<OnWheelScrollListener>()
    private val clickingListeners = LinkedList<OnWheelClickedListener>()

    // Scrolling listener
    internal var scrollingListener: WheelScroller.ScrollingListener = object : WheelScroller.ScrollingListener {
        override fun onStarted() {
            isScrollingPerformed = true
            notifyScrollingListenersAboutStart()
        }

        override fun onScroll(distance: Int) {
            doScroll(distance)

            val height = height
            if (scrollingOffset > height) {
                scrollingOffset = height
                scroller!!.stopScrolling()
            } else if (scrollingOffset < -height) {
                scrollingOffset = -height
                scroller!!.stopScrolling()
            }
        }

        override fun onFinished() {
            if (isScrollingPerformed) {
                notifyScrollingListenersAboutEnd()
                isScrollingPerformed = false
            }

            scrollingOffset = 0
            invalidate()
        }

        override fun onJustify() {
            if (Math.abs(scrollingOffset) > WheelScroller.MIN_DELTA_FOR_SCROLLING) {
                scroller!!.scroll(scrollingOffset, 0)
            }
        }
    }

    // Adapter listener
    private val dataObserver = object : DataSetObserver() {
        override fun onChanged() {
            invalidateWheel(false)
        }

        override fun onInvalidated() {
            invalidateWheel(true)
        }
    }

    /**
     * Calculates range for wheel items
     * @return the items range
     */
    private// top + bottom items
    // process empty items above the first or below the second
    val itemsRange: ItemsRange?
        get() {
            if (getItemHeight() == 0) {
                return null
            }

            var first = currentItem
            var count = 1

            while (count * getItemHeight() < height) {
                first--
                count += 2
            }

            if (scrollingOffset != 0) {
                if (scrollingOffset > 0) {
                    first--
                }
                count++
                val emptyItems = scrollingOffset / getItemHeight()
                first -= emptyItems
                count += Math.asin(emptyItems.toDouble()).toInt()
            }
            return ItemsRange(first, count)
        }

    /**
     * Constructor
     */
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initData(context, attrs)
    }

    /**
     * Constructor
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initData(context, attrs)
    }

    /**
     * Constructor
     */
    constructor(context: Context) : super(context) {
        initData(context, null)
    }

    /**
     * Initializes class data
     * @param context the context
     */
    private fun initData(context: Context, attrs: AttributeSet?) {
        scroller = WheelScroller(getContext(), scrollingListener)
        //加载自定义属性
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.WheelView)
        divLineColor = typedArray.getColor(R.styleable.WheelView_div_line_color, ContextCompat.getColor(context, R.color.lable_blue))
        //注意回收TypedArray
        typedArray.recycle()
    }

    /**
     * Set the the specified scrolling interpolator
     * @param interpolator the interpolator
     */
    fun setInterpolator(interpolator: Interpolator) {
        scroller!!.setInterpolator(interpolator)
    }

    /**
     * Adds wheel changing listener
     * @param listener the listener
     */
    fun addChangingListener(listener: OnWheelChangedListener) {
        changingListeners.add(listener)
    }

    /**
     * Removes wheel changing listener
     * @param listener the listener
     */
    fun removeChangingListener(listener: OnWheelChangedListener) {
        changingListeners.remove(listener)
    }

    /**
     * Notifies changing listeners
     * @param oldValue the old wheel value
     * @param newValue the new wheel value
     */
    protected fun notifyChangingListeners(oldValue: Int, newValue: Int) {
        for (listener in changingListeners) {
            listener.onChanged(this, oldValue, newValue)
        }
    }

    /**
     * Adds wheel scrolling listener
     * @param listener the listener
     */
    fun addScrollingListener(listener: OnWheelScrollListener) {
        scrollingListeners.add(listener)
    }

    /**
     * Removes wheel scrolling listener
     * @param listener the listener
     */
    fun removeScrollingListener(listener: OnWheelScrollListener) {
        scrollingListeners.remove(listener)
    }

    /**
     * Notifies listeners about starting scrolling
     */
    protected fun notifyScrollingListenersAboutStart() {
        for (listener in scrollingListeners) {
            listener.onScrollingStarted(this)
        }
    }

    /**
     * Notifies listeners about ending scrolling
     */
    protected fun notifyScrollingListenersAboutEnd() {
        for (listener in scrollingListeners) {
            listener.onScrollingFinished(this)
        }
    }

    /**
     * Adds wheel clicking listener
     * @param listener the listener
     */
    fun addClickingListener(listener: OnWheelClickedListener) {
        clickingListeners.add(listener)
    }

    /**
     * Removes wheel clicking listener
     * @param listener the listener
     */
    fun removeClickingListener(listener: OnWheelClickedListener) {
        clickingListeners.remove(listener)
    }

    /**
     * Notifies listeners about clicking
     */
    protected fun notifyClickListenersAboutClick(item: Int) {
        for (listener in clickingListeners) {
            listener.onItemClicked(this, item)
        }
    }

    /**
     * Gets current value
     *
     * @return the current value
     */
    fun getCurrentItem(): Int {
        return currentItem
    }

    /**
     * Sets the current item. Does nothing when index is wrong.
     *
     * @param index the item index
     * @param animated the animation flag
     */
    @JvmOverloads
    fun setCurrentItem(index: Int, animated: Boolean = false) {
        var index = index
        if (this.viewAdapter == null || this.viewAdapter!!.getItemsCount() == 0) {
            return  // throw?
        }

        val itemCount = this.viewAdapter!!.getItemsCount()
        if (index < 0 || index >= itemCount) {
            if (isCyclic) {
                while (index < 0) {
                    index += itemCount
                }
                index %= itemCount
            } else {
                return  // throw?
            }
        }
        if (index != currentItem) {
            if (animated) {
                var itemsToScroll = index - currentItem
                if (isCyclic) {
                    val scroll = itemCount + Math.min(index, currentItem) - Math.max(index, currentItem)
                    if (scroll < Math.abs(itemsToScroll)) {
                        itemsToScroll = if (itemsToScroll < 0) scroll else -scroll
                    }
                }
                scroll(itemsToScroll, 0)
            } else {
                scrollingOffset = 0

                val old = currentItem
                currentItem = index

                notifyChangingListeners(old, currentItem)

                invalidate()
            }
        }
    }

    /**
     * Tests if wheel is cyclic. That means before the 1st item there is shown the last one
     * @return true if wheel is cyclic
     */
    fun isCyclic(): Boolean {
        return isCyclic
    }

    /**
     * Set wheel cyclic flag
     * @param isCyclic the flag to set
     */
    fun setCyclic(isCyclic: Boolean) {
        this.isCyclic = isCyclic
        invalidateWheel(false)
    }

    /**
     * Determine whether shadows are drawn
     * @return true is shadows are drawn
     */
    fun drawShadows(): Boolean {
        return drawShadows
    }

    /**
     * Set whether shadows should be drawn
     * @param drawShadows flag as true or false
     */
    fun setDrawShadows(drawShadows: Boolean) {
        this.drawShadows = drawShadows
    }

    /**
     * Set the shadow gradient color
     * @param start
     * @param middle
     * @param end
     */
    fun setShadowColor(start: Int, middle: Int, end: Int) {
        SHADOWS_COLORS = intArrayOf(start, middle, end)
    }

    /**
     * Sets the drawable for the wheel background
     * @param resource
     */
    fun setWheelBackground(resource: Int) {
        wheelBackground = resource
        setBackgroundResource(wheelBackground)
    }

    /**
     * Sets the drawable for the wheel foreground
     * @param resource
     */
    fun setWheelForeground(resource: Int) {
        wheelForeground = resource
        centerDrawable = context.resources.getDrawable(wheelForeground)
    }

    /**
     * Invalidates wheel
     * @param clearCaches if true then cached views will be clear
     */
    fun invalidateWheel(clearCaches: Boolean) {
        if (clearCaches) {
            recycle.clearAll()
            if (itemsLayout != null) {
                itemsLayout!!.removeAllViews()
            }
            scrollingOffset = 0
        } else if (itemsLayout != null) {
            // cache all items
            recycle.recycleItems(itemsLayout!!, firstItem, ItemsRange())
        }

        invalidate()
    }

    /**
     * Initializes resources
     */
    private fun initResourcesIfNecessary() {
        if (centerDrawable == null) {
            centerDrawable = context.resources.getDrawable(wheelForeground)
        }

        if (topShadow == null) {
            topShadow = GradientDrawable(Orientation.TOP_BOTTOM, SHADOWS_COLORS)
        }

        if (bottomShadow == null) {
            bottomShadow = GradientDrawable(Orientation.BOTTOM_TOP, SHADOWS_COLORS)
        }

        setBackgroundResource(wheelBackground)
    }

    /**
     * Calculates desired height for layout
     *
     * @param layout
     * the source layout
     * @return the desired layout height
     */
    private fun getDesiredHeight(layout: LinearLayout?): Int {
        if (layout?.getChildAt(0) != null) {
            itemHeight = layout.getChildAt(0).measuredHeight
        }

        val desired = itemHeight * visibleItems - itemHeight * ITEM_OFFSET_PERCENT / 50

        return Math.max(desired, suggestedMinimumHeight)
    }

    /**
     * Returns height of wheel item
     * @return the item height
     */
    private fun getItemHeight(): Int {
        if (itemHeight != 0) {
            return itemHeight
        }

        if (itemsLayout != null && itemsLayout!!.getChildAt(0) != null) {
            itemHeight = itemsLayout!!.getChildAt(0).height
            return itemHeight
        }

        return height / visibleItems
    }

    /**
     * Calculates control width and creates text layouts
     * @param widthSize the input layout width
     * @param mode the layout mode
     * @return the calculated control width
     */
    private fun calculateLayoutWidth(widthSize: Int, mode: Int): Int {
        initResourcesIfNecessary()

        // TODO: make it static
        itemsLayout!!.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        itemsLayout!!.measure(View.MeasureSpec.makeMeasureSpec(widthSize, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        var width = itemsLayout!!.measuredWidth

        if (mode == View.MeasureSpec.EXACTLY) {
            width = widthSize
        } else {
            width += 2 * PADDING

            // Check against our minimum width
            width = Math.max(width, suggestedMinimumWidth)

            if (mode == View.MeasureSpec.AT_MOST && widthSize < width) {
                width = widthSize
            }
        }

        itemsLayout!!.measure(View.MeasureSpec.makeMeasureSpec(width - 2 * PADDING, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

        return width
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        buildViewForMeasuring()

        val width = calculateLayoutWidth(widthSize, widthMode)

        var height: Int
        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize
        } else {
            height = getDesiredHeight(itemsLayout)

            if (heightMode == View.MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize)
            }
        }

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layout(r - l, b - t)
    }

    /**
     * Sets layouts width and height
     * @param width the layout width
     * @param height the layout height
     */
    private fun layout(width: Int, height: Int) {
        val itemsWidth = width - 2 * PADDING

        itemsLayout!!.layout(0, 0, itemsWidth, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (this.viewAdapter != null && this.viewAdapter!!.getItemsCount() > 0) {
            updateView()

            drawItems(canvas)
            drawCenterRect(canvas)
        }

        if (drawShadows) drawShadows(canvas)
    }

    /**
     * Draws shadows on top and bottom of control
     * @param canvas the canvas for drawing
     */
    private fun drawShadows(canvas: Canvas) {
        val height = (1.5 * getItemHeight()).toInt()
        topShadow!!.setBounds(0, 0, width, height)
        topShadow!!.draw(canvas)

        bottomShadow!!.setBounds(0, getHeight() - height, width, getHeight())
        bottomShadow!!.draw(canvas)
    }

    /**
     * Draws items
     * @param canvas the canvas for drawing
     */
    private fun drawItems(canvas: Canvas) {
        canvas.save()

        val top = (currentItem - firstItem) * getItemHeight() + (getItemHeight() - height) / 2
        canvas.translate(PADDING.toFloat(), (-top + scrollingOffset).toFloat())

        itemsLayout!!.draw(canvas)

        canvas.restore()
    }

    /**
     * Draws rect for current value
     * @param canvas the canvas for drawing
     */
//    private fun drawCenterRect(canvas: Canvas) {
//        val center = height / 2
//        val offset = (getItemHeight() / 2 * 1.2).toInt()
//        centerDrawable!!.setBounds(0, center - offset, width, center + offset)
//        centerDrawable!!.draw(canvas)
//    }

    /**
     * 使用自己的画线，而不是描边
     * @param canvas Canvas
     */
    private fun drawCenterRect(canvas: Canvas) {
        val center = height / 2
        val offset = getItemHeight() / 2

        val paint = Paint()
        paint.color = divLineColor
        // 设置线宽
        paint.strokeWidth = 2.toFloat()
        // 绘制上边直线
        canvas.drawLine(0f, (center - offset).toFloat(), width.toFloat(), (center - offset).toFloat(), paint)
        // 绘制下边直线
        canvas.drawLine(0f, (center + offset).toFloat(), width.toFloat(), (center + offset).toFloat(), paint)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled || viewAdapter == null) {
            return true
        }

        when (event.action) {
            MotionEvent.ACTION_MOVE -> if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_UP -> if (!isScrollingPerformed) {
                var distance = event.y.toInt() - height / 2
                if (distance > 0) {
                    distance += getItemHeight() / 2
                } else {
                    distance -= getItemHeight() / 2
                }
                val items = distance / getItemHeight()
                if (items != 0 && isValidItemIndex(currentItem + items)) {
                    notifyClickListenersAboutClick(currentItem + items)
                }
            }
        }

        return scroller!!.onTouchEvent(event)
    }

    /**
     * Scrolls the wheel
     * @param delta the scrolling value
     */
    private fun doScroll(delta: Int) {
        scrollingOffset += delta

        val itemHeight = getItemHeight()
        var count = scrollingOffset / itemHeight

        var pos = currentItem - count
        val itemCount = this.viewAdapter!!.getItemsCount()

        var fixPos = scrollingOffset % itemHeight
        if (Math.abs(fixPos) <= itemHeight / 2) {
            fixPos = 0
        }
        if (isCyclic && itemCount > 0) {
            if (fixPos > 0) {
                pos--
                count++
            } else if (fixPos < 0) {
                pos++
                count--
            }
            // fix position by rotating
            while (pos < 0) {
                pos += itemCount
            }
            pos %= itemCount
        } else {
            //
            if (pos < 0) {
                count = currentItem
                pos = 0
            } else if (pos >= itemCount) {
                count = currentItem - itemCount + 1
                pos = itemCount - 1
            } else if (pos > 0 && fixPos > 0) {
                pos--
                count++
            } else if (pos < itemCount - 1 && fixPos < 0) {
                pos++
                count--
            }
        }

        val offset = scrollingOffset
        if (pos != currentItem) {
            setCurrentItem(pos, false)
        } else {
            invalidate()
        }

        // update offset
        scrollingOffset = offset - count * itemHeight
        if (scrollingOffset > height) {
            if (height <= 0) {
                scrollingOffset = 0
            } else {
                scrollingOffset = scrollingOffset % height + height
            }
        }
    }

    /**
     * Scroll the wheel
     * @param time scrolling duration
     */
    fun scroll(itemsToScroll: Int, time: Int) {
        val distance = itemsToScroll * getItemHeight() - scrollingOffset
        scroller!!.scroll(distance, time)
    }

    /**
     * Rebuilds wheel items if necessary. Caches all unused items.
     *
     * @return true if items are rebuilt
     */
    private fun rebuildItems(): Boolean {
        var updated = false
        val range = itemsRange
        if (itemsLayout != null) {
            val first = recycle.recycleItems(itemsLayout!!, firstItem, range!!)
            updated = firstItem != first
            firstItem = first
        } else {
            createItemsLayout()
            updated = true
        }

        if (!updated) {
            updated = firstItem != range!!.first || itemsLayout!!.childCount != range.count
        }

        if (firstItem > range!!.first && firstItem <= range.last) {
            for (i in firstItem - 1 downTo range.first) {
                if (!addViewItem(i, true)) {
                    break
                }
                firstItem = i
            }
        } else {
            firstItem = range.first
        }

        var first = firstItem
        for (i in itemsLayout!!.childCount until range.count) {
            if (!addViewItem(firstItem + i, false) && itemsLayout!!.childCount == 0) {
                first++
            }
        }
        firstItem = first

        return updated
    }

    /**
     * Updates view. Rebuilds items and label if necessary, recalculate items sizes.
     */
    private fun updateView() {
        if (rebuildItems()) {
            calculateLayoutWidth(width, View.MeasureSpec.EXACTLY)
            layout(width, height)
        }
    }

    /**
     * Creates item layouts if necessary
     */
    private fun createItemsLayout() {
        if (itemsLayout == null) {
            itemsLayout = LinearLayout(context)
            itemsLayout!!.orientation = LinearLayout.VERTICAL
        }
    }

    /**
     * Builds view for measuring
     */
    private fun buildViewForMeasuring() {
        // clear all items
        if (itemsLayout != null) {
            recycle.recycleItems(itemsLayout!!, firstItem, ItemsRange())
        } else {
            createItemsLayout()
        }

        // add views
        // all items must be included to measure width correctly
        for (i in this.viewAdapter!!.getItemsCount() - 1 downTo 0) {
            if (addViewItem(i, true)) {
                firstItem = i
            }
        }
    }

    /**
     * Adds view for item to items layout
     * @param index the item index
     * @param first the flag indicates if view should be first
     * @return true if corresponding item exists and is added
     */
    private fun addViewItem(index: Int, first: Boolean): Boolean {
        val view = getItemView(index)
        if (view != null) {
            if (first) {
                itemsLayout!!.addView(view, 0)
            } else {
                itemsLayout!!.addView(view)
            }

            return true
        }

        return false
    }

    /**
     * Checks whether intem index is valid
     * @param index the item index
     * @return true if item index is not out of bounds or the wheel is cyclic
     */
    private fun isValidItemIndex(index: Int): Boolean {
        return this.viewAdapter != null && this.viewAdapter!!.getItemsCount() > 0 &&
                (isCyclic || index >= 0 && index < this.viewAdapter!!.getItemsCount())
    }

    /**
     * Returns view for specified item
     * @param index the item index
     * @return item view or empty view if index is out of bounds
     */
    private fun getItemView(index: Int): View? {
        var index = index
        if (this.viewAdapter == null || this.viewAdapter!!.getItemsCount() == 0) {
            return null
        }
        val count = this.viewAdapter!!.getItemsCount()
        if (!isValidItemIndex(index)) {
            return this.viewAdapter!!.getEmptyItem(recycle.emptyItem, itemsLayout!!)
        } else {
            while (index < 0) {
                index += count
            }
        }

        index %= count
        return this.viewAdapter!!.getItem(index, recycle.item, itemsLayout!!)
    }

    /**
     * Stops scrolling
     */
    fun stopScrolling() {
        scroller!!.stopScrolling()
    }

    companion object {

        /** Top and bottom items offset (to hide that)  */
        private val ITEM_OFFSET_PERCENT = 0

        /** Left and right padding value  */
        private val PADDING = 10

        /** Default count of visible items  */
        private val DEF_VISIBLE_ITEMS = 5
    }
}
/**
 * Sets the current item w/o animation. Does nothing when index is wrong.
 *
 * @param index the item index
 */