package cn.magicwindow.channelwidget.widget


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.SparseBooleanArray
import android.util.SparseIntArray
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import cn.magicwindow.channelwidget.entity.ChannelBean
import cn.magicwindow.commonui.R
import cn.magicwindow.utils.ResolutionUtils
import java.util.*

/**
 * @author null
 */

class ChannelTabLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : HorizontalScrollView(context, attrs, defStyleAttr) {
    //默认字体大小
    private val DEFAULT_NORMAL_TEXT_SIZE_SP = ResolutionUtils.sp2px(14, context)
    private var mNormalTextSize = DEFAULT_NORMAL_TEXT_SIZE_SP
    //选中字体大小
    private val DEFAULT_SELECT_TEXT_SIZE_SP = ResolutionUtils.sp2px(16, context)
    private var mSelectTextSize = DEFAULT_SELECT_TEXT_SIZE_SP
    //字体颜色
    private val DEFAULT_NORMAL_TEXT_COLOR = Color.BLACK
    private val DEFAULT_SELECT_TEXT_COLOR = Color.RED
    private var mTextColor: ColorStateList? = null
    //指示器高度
    private val DEFAULT_INDICATOR_HEIGHT_DP = ResolutionUtils.dp2px(2, context)
    private var mIndicatorHeight = DEFAULT_INDICATOR_HEIGHT_DP
    //指示器颜色
    private val DEFAULT_INDICATOR_COLOR = Color.RED
    private var mIndicatorColor = DEFAULT_INDICATOR_COLOR
    //tab最小宽度
    private val DEFAULT_TAB_MIN_WIDTH = ResolutionUtils.dp2px(30, context)
    private var mMinTabWidth = DEFAULT_TAB_MIN_WIDTH
    //tab之间的间距
    private var mTabPadding: Int = 0
    //关联的viewpager
    private var mViewPager: ViewPager? = null
    //第一个子View
    private val mTabContainer: Indicator
    //Tab总数
    private var mTabCount: Int = 0
    //当前选中的Tab
    private var mCurrentTabPosition: Int = 0
    //当前切换Tab的偏移量
    private var mCurrentOffset: Float = 0.toFloat()
    //数据源
    private val mDataList: MutableList<ChannelBean>
    //中间线
    private val DEFAULT_DIVIDER_WIDTH = ResolutionUtils.dp2px(1, context)
    private var mDividerWidth = DEFAULT_DIVIDER_WIDTH
    private val DEFAULT_DIVIDER_COLOR = Color.GRAY
    private var mDividerColor = DEFAULT_DIVIDER_COLOR
    private val mDividerPaint: Paint
    private val DEFAULT_DIVIDER_PADDING = ResolutionUtils.dp2px(5, context)
    private var mDividerPadding = DEFAULT_DIVIDER_PADDING
    private var hasShowDivider = false

    //红点显示
    private val DEFAULT_MSG_ROUND_COLOR = Color.RED
    private val mMsgRoundColor = DEFAULT_MSG_ROUND_COLOR
    private val mInitSetMap: SparseBooleanArray
    private val mMsgNumMap: SparseIntArray
    private val mMsgPaint: Paint
    private val mMsgNumPaint: Paint
    private val mMsgNumColor = Color.WHITE
    private val mMsgTextSizeSp = ResolutionUtils.sp2px(8, context)
    private var mMsgPadding: Int = 0

    init {
        initStyle(context, attrs)
        isFillViewport = true
        isHorizontalScrollBarEnabled = false
        mTabContainer = Indicator(context)
        mTabContainer.setSelectedIndicatorColor(mIndicatorColor)
        mTabContainer.setSelectedIndicatorHeight(mIndicatorHeight)
        addView(mTabContainer, 0, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT))
        mDataList = ArrayList()
        mDividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mMsgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mMsgNumPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mInitSetMap = SparseBooleanArray()
        mMsgNumMap = SparseIntArray()
    }

    /**
     * 设置Style(自定义属性）
     */
    private fun initStyle(context: Context, attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ChannelTabLayout, 0, 0)
        mNormalTextSize = typedArray.getDimensionPixelSize(R.styleable.ChannelTabLayout_tab_normal_textSize, DEFAULT_NORMAL_TEXT_SIZE_SP)
        mSelectTextSize = typedArray.getDimensionPixelSize(R.styleable.ChannelTabLayout_tab_select_textSize, DEFAULT_SELECT_TEXT_SIZE_SP)
        mTextColor = typedArray.getColorStateList(R.styleable.ChannelTabLayout_tab_textColor)
        if (mTextColor == null)
            mTextColor = createDefaultTextColor()
        mIndicatorHeight = typedArray.getDimension(R.styleable.ChannelTabLayout_tab_indicatorHeight, DEFAULT_INDICATOR_HEIGHT_DP.toFloat()).toInt()
        mIndicatorColor = typedArray.getColor(R.styleable.ChannelTabLayout_tab_indicatorColor, DEFAULT_INDICATOR_COLOR)
        mMinTabWidth = typedArray.getColor(R.styleable.ChannelTabLayout_tab_min_width, DEFAULT_TAB_MIN_WIDTH)
        mDividerColor = typedArray.getColor(R.styleable.ChannelTabLayout_tab_dividerColor, DEFAULT_DIVIDER_COLOR)
        mDividerWidth = typedArray.getDimension(R.styleable.ChannelTabLayout_tab_dividerWidth, DEFAULT_DIVIDER_WIDTH.toFloat()).toInt()
        mDividerPadding = typedArray.getDimension(R.styleable.ChannelTabLayout_tab_dividerPadding, DEFAULT_DIVIDER_PADDING.toFloat()).toInt()
        mTabPadding = typedArray.getDimension(R.styleable.ChannelTabLayout_tab_Padding, dp2px(10,context).toFloat()).toInt()
        hasShowDivider = typedArray.getBoolean(R.styleable.ChannelTabLayout_tab_dividerShow, false)
        typedArray.recycle()
    }

    private fun createDefaultTextColor(): ColorStateList {
        return ColorStateList(arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf(0)), intArrayOf(DEFAULT_SELECT_TEXT_COLOR, DEFAULT_NORMAL_TEXT_COLOR))
    }

    fun setDataList(dataList: List<ChannelBean>) {
        this.mDataList.clear()
        this.mDataList.addAll(dataList)
        notifyDataSetChanged()
    }

    fun setupWithViewPager(viewPager: ViewPager?) {
        this.mViewPager = viewPager
        if (viewPager == null) throw IllegalArgumentException("viewpager not is null")
        viewPager.adapter ?: throw IllegalArgumentException("pagerAdapter not is null")
        this.mViewPager!!.addOnPageChangeListener(TabPagerChanger())

        mCurrentTabPosition = viewPager.currentItem
        notifyDataSetChanged()
    }

    fun notifyDataSetChanged() {
        mTabContainer.removeAllViews()
        mTabCount = mDataList.size
        for (i in 0 until mTabCount) {
            val currentPosition = i
            val tabTextView = createTextView()
            tabTextView.setPadding(mTabPadding, 0, mTabPadding, 0)
            tabTextView.text = mDataList[i].channelName
            tabTextView.setOnClickListener { mViewPager!!.currentItem = currentPosition }
            mTabContainer.addView(tabTextView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f))
        }
        setSelectedTabView(mCurrentTabPosition)
    }

    private fun createTextView(): TextView {
        val textView = TextView(context)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNormalTextSize.toFloat())
        textView.setTextColor(mTextColor)
        textView.setLines(1)
        textView.minWidth = mMinTabWidth
        textView.typeface = Typeface.DEFAULT_BOLD
        textView.gravity = Gravity.CENTER
        return textView
    }

    protected fun setSelectedTabView(position: Int) {
        for (i in 0 until mTabCount) {
            val view = mTabContainer.getChildAt(i)
            if (view is TextView) {
                view.isSelected = position == i
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, (if (position == i) mSelectTextSize else mNormalTextSize).toFloat())
            }
        }
    }

    private fun setScrollPosition(position: Int, positionOffset: Float) {
        this.mCurrentTabPosition = position
        this.mCurrentOffset = positionOffset
        mTabContainer.setIndicatorPositionFromTabPosition(position, positionOffset)
        scrollTo(calculateScrollXForTab(mCurrentTabPosition, mCurrentOffset), 0)
        if (position == 3)
            hideMsg(position)
    }

    private fun calculateScrollXForTab(position: Int, positionOffset: Float): Int {
        val selectedChild = mTabContainer.getChildAt(position)
        val nextChild = if (position + 1 < mTabContainer.childCount)
            mTabContainer.getChildAt(position + 1)
        else
            null
        val selectedWidth = selectedChild?.width ?: 0
        val nextWidth = nextChild?.width ?: 0
        return (selectedChild!!.left
                + ((selectedWidth + nextWidth).toFloat() * positionOffset * 0.5f).toInt()
                + selectedChild.width / 2) - width / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isInEditMode || mTabCount <= 0) {
            return
        }
        val height = height
        val paddingLeft = paddingLeft
        // 画中间线间隔
        if (mDividerWidth > 0 && hasShowDivider) {
            mDividerPaint.strokeWidth = mDividerWidth.toFloat()
            mDividerPaint.color = mDividerColor
            for (i in 0 until mTabCount - 1) {
                val tab = mTabContainer.getChildAt(i)
                canvas.drawLine((paddingLeft + tab.right).toFloat(), mDividerPadding.toFloat(), (paddingLeft + tab.right).toFloat(), (height - mDividerPadding).toFloat(), mDividerPaint)
            }
        }
        //画消息提示
        for (i in 0 until mTabCount - 1) {
            if (mInitSetMap.get(i)) {
                updateMsgPosition(canvas, mTabContainer.getChildAt(i), mMsgNumMap.get(i))
            }
        }
    }

    private fun updateMsgPosition(canvas: Canvas, updateView: View?, msgNum: Int) {
        if (updateView == null)
            return
        val circleX: Int
        val circleY: Int
        if (updateView.width > 0) {
            val selectTextPadding = ((updateView.width - measureTextLength(updateView)) / 2 + 0.5f).toInt()
            circleX = updateView.right - selectTextPadding + mMsgPadding
            circleY = ((mTabContainer.height - measureTextHeight(updateView)) / 2 - mMsgPadding).toInt()
            drawMsg(canvas, circleX, circleY, msgNum)
        }
    }

    /**
     * 画出条目上的消息数目
     */
    private fun drawMsg(canvas: Canvas, mMsgCircleX: Int, mMsgCircleY: Int, mMsgNum: Int) {
        mMsgPaint.style = Paint.Style.FILL
        mMsgPaint.color = mMsgRoundColor
        if (mMsgNum > 0) {
            mMsgNumPaint.textSize = mMsgTextSizeSp.toFloat()
            mMsgNumPaint.color = mMsgNumColor
            mMsgNumPaint.textAlign = Paint.Align.CENTER
            val showTxt = if (mMsgNum > 99) "99+" else mMsgNum.toString()
            val mMsgNumRadius = Math.max(mMsgNumPaint.descent() - mMsgNumPaint.ascent(),
                    mMsgNumPaint.measureText(showTxt)).toInt() / 2 + ResolutionUtils.dp2px(2, context)
            canvas.drawCircle((mMsgCircleX + mMsgNumRadius).toFloat(), mMsgCircleY.toFloat(), mMsgNumRadius.toFloat(), mMsgPaint)
            val fontMetrics = mMsgNumPaint.fontMetricsInt
            val baseline = ((2 * mMsgCircleY - (fontMetrics.descent - fontMetrics.ascent)) / 2 - fontMetrics.ascent + 0.5f).toInt()
            canvas.drawText(showTxt, (mMsgCircleX + mMsgNumRadius).toFloat(),
                    baseline.toFloat(), mMsgNumPaint)
        } else {
            canvas.drawCircle((mMsgCircleX + ResolutionUtils.dp2px(2, context)).toFloat(), mMsgCircleY.toFloat(), ResolutionUtils.dp2px(2, context).toFloat(), mMsgPaint)
        }
    }

    fun showMsg(msgPosition: Int, msgNum: Int, msgPadding: Int) {
        mInitSetMap.put(msgPosition, true)
        this.mMsgNumMap.put(msgPosition, msgNum)
        mMsgPadding = msgPadding
        ViewCompat.postInvalidateOnAnimation(this)
    }

    fun hideMsg(msgPosition: Int) {
        mInitSetMap.put(msgPosition, false)
        this.mMsgNumMap.delete(msgPosition)
        ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun measureTextLength(measureView: View): Float {
        if (measureView is TextView) {
            val text = measureView.text.toString()
            return measureView.paint.measureText(text)
        }
        return 0f
    }

    private fun measureTextHeight(measureView: View): Float {
        if (measureView is TextView) {
            val textPaint = measureView.paint
            return textPaint.descent() - textPaint.ascent()
        }
        return 0f
    }

    private inner class TabPagerChanger : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            setScrollPosition(position, positionOffset)
        }

        override fun onPageSelected(position: Int) {
            setSelectedTabView(position)
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    fun dp2px(value: Int, context: Context): Int {
        return (context.applicationContext.resources.displayMetrics.density * value).toInt()
    }
}
