package cn.magicwindow.channelwidget.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView


/**
 * @author null
 */

class Indicator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    //指示器
    private var mSelectedIndicatorHeight: Int = 0
    private val mSelectedIndicatorPaint: Paint
    private var mSelectedPosition = -1
    private var mSelectionOffset: Float = 0.toFloat()
    private var mIndicatorLeft = -1
    private var mIndicatorRight = -1

    init {
        setWillNotDraw(false)
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_HORIZONTAL
        mSelectedIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    fun setSelectedIndicatorColor(color: Int) {
        if (mSelectedIndicatorPaint.color != color) {
            mSelectedIndicatorPaint.color = color
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    fun setSelectedIndicatorHeight(height: Int) {
        if (mSelectedIndicatorHeight != height) {
            mSelectedIndicatorHeight = height
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    fun setIndicatorPositionFromTabPosition(position: Int, positionOffset: Float) {
        mSelectedPosition = position
        mSelectionOffset = positionOffset
        updateIndicatorPosition()
    }

    private fun updateIndicatorPosition() {
        val selectedTitle = getChildAt(mSelectedPosition)
        var left: Int
        var right: Int

        if (selectedTitle != null && selectedTitle.width > 0) {
            val selectTextPadding = ((selectedTitle.width - measureTextLength(selectedTitle)) / 2 + 0.5f).toInt()
            left = selectedTitle.left + selectTextPadding
            right = selectedTitle.right - selectTextPadding

            if (mSelectionOffset > 0f && mSelectedPosition < childCount - 1) {
                val nextTitle = getChildAt(mSelectedPosition + 1)
                val textPadding = ((nextTitle.width - measureTextLength(nextTitle)) / 2 + 0.5f).toInt()
                val moveLeft = nextTitle.left + textPadding - left
                val moveRight = nextTitle.right - textPadding - right
                left = (left + moveLeft * mSelectionOffset).toInt()
                right = (right + moveRight * mSelectionOffset).toInt()
            }
        } else {
            right = -1
            left = right
        }
        setIndicatorPosition(left, right)
    }

    private fun setIndicatorPosition(left: Int, right: Int) {
        if (left != mIndicatorLeft || right != mIndicatorRight) {
            mIndicatorLeft = left
            mIndicatorRight = right
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }


    private fun measureTextLength(measureView: View): Float {
        if (measureView is TextView) {
            val text = measureView.text.toString()
            return measureView.paint.measureText(text)
        }
        return 0f
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (mIndicatorLeft in 0..(mIndicatorRight - 1)) {
            canvas.drawRect(mIndicatorLeft.toFloat(), (height - mSelectedIndicatorHeight).toFloat(),
                    mIndicatorRight.toFloat(), height.toFloat(), mSelectedIndicatorPaint)
        }
    }
}
