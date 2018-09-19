package com.viet.news.core.ui.code

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.viet.news.core.R


/**
 * Author:[https://github.com/kungfubrother](https://github.com/kungfubrother)
 * Date:2017/1/7 20:12
 * Description:密码显示控件
 */
class CodeView : View {

    //密码长度，默认6位
    private var length: Int = 0
    //描边颜色，默认#E1E1E1
    private var borderColor: Int = 0
    //被选中的描边颜色，默认#9666FB
    private var borderSelectColor: Int = 0
    //描边宽度，默认1px
    private var borderWidth: Float = 0.toFloat()
    //分割线颜色，默认#E1E1E1
    private var dividerColor: Int = 0
    //分割线宽度，默认1px
    private var dividerWidth: Float = 0.toFloat()
    //默认文本，在XML设置后可预览效果
    private var code: String? = null
    //密码点颜色，默认#000000
    private var codeColor: Int = 0
    //密码点半径，默认8dp
    private var pointRadius: Float = 0.toFloat()
    //显示明文时的文字大小，默认unitWidth/2
    private var textSize: Float = 0.toFloat()
    //输入框间距
    private var padding: Float = 0.toFloat()
    //显示类型，支持密码、明文，默认明文
    private var pwdType: Int = 0
    //输入框样式:1,矩形无间距;2,矩形有间距;3,下划线
    private var showType: Int = 0

    private var unitWidth: Float = 0.toFloat()
    private var paint: Paint? = null
    private var paintLine: Paint? = null
    private var listener: Listener? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //根据宽度来计算单元格大小（高度）
        val width = measuredWidth.toFloat()
        //宽度-左右边宽-中间分割线宽度
        unitWidth = (width - 2 * borderWidth - (length - 1) * dividerWidth - (length - 1) * padding) / length

        if (textSize == 0f) {
            textSize = unitWidth / 2
        }
        setMeasuredDimension(width.toInt(), (unitWidth + 2 * borderWidth).toInt())
    }

    private fun init(attrs: AttributeSet?) {
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.textAlign = Paint.Align.CENTER
        paintLine = Paint()
        paintLine!!.isAntiAlias = true
        paintLine!!.textAlign = Paint.Align.CENTER
        if (attrs == null) {
            length = 6
            borderColor = Color.parseColor("#E1E1E1")
            borderSelectColor = Color.parseColor("#343434")
            borderWidth = 1f
            dividerColor = Color.parseColor("#F71F01")
            dividerWidth = 1f
            code = ""
            codeColor = Color.parseColor("#000000")
            pointRadius = dp2px(context, 8f).toFloat()
            pwdType = SHOW_TYPE_WORD
            textSize = 0f
            showType = 1
            padding = dp2px(context, 8f).toFloat()
        } else {
            val typedArray = resources.obtainAttributes(attrs, R.styleable.CodeView)
            length = typedArray.getInt(R.styleable.CodeView_length, 6)
            borderColor = typedArray.getColor(R.styleable.CodeView_borderColor, Color.parseColor("#E1E1E1"))
            borderSelectColor = typedArray.getColor(R.styleable.CodeView_borderSelectColor, Color.parseColor("#9666FB"))
            borderWidth = typedArray.getDimensionPixelSize(R.styleable.CodeView_borderWidth, 1).toFloat()
            dividerColor = typedArray.getColor(R.styleable.CodeView_dividerColor, Color.parseColor("#E1E1E1"))
            dividerWidth = typedArray.getDimensionPixelSize(R.styleable.CodeView_dividerWidth, 1).toFloat()
            code = typedArray.getString(R.styleable.CodeView_code)
            if (code == null) {
                code = ""
            }
            codeColor = typedArray.getColor(R.styleable.CodeView_codeColor, Color.parseColor("#000000"))
            pointRadius = typedArray.getDimensionPixelSize(R.styleable.CodeView_pointRadius, dp2px(context, 8f)).toFloat()
            pwdType = typedArray.getInt(R.styleable.CodeView_pwdType, SHOW_TYPE_WORD)
            showType = typedArray.getInt(R.styleable.CodeView_showType, 1)
            textSize = typedArray.getDimensionPixelSize(R.styleable.CodeView_textSize, 0).toFloat()
            padding = typedArray.getDimensionPixelSize(R.styleable.CodeView_padding, 10).toFloat()

            if (showType == 1) padding = 0f
            typedArray.recycle()
        }
    }

    private fun dp2px(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (showType) {
            1 -> {  //不带padding矩形
                drawDivider(canvas)
                drawBorder(canvas)
            }
            2 -> drawRect(canvas) //带padding的矩形
            3 -> drawUnderline(canvas) //下划线
        }
        when (pwdType) {
            SHOW_TYPE_PASSWORD -> drawPoint(canvas)
            else -> drawValue(canvas)
        }
    }

    /**
     * 描边
     */
    private fun drawBorder(canvas: Canvas) {
        if (borderWidth > 0) {
            paint!!.color = borderColor
            canvas.drawRect(0f, 0f, width.toFloat(), borderWidth, paint!!)
            canvas.drawRect(0f, height - borderWidth, width.toFloat(), height.toFloat(), paint!!)
            canvas.drawRect(0f, 0f, borderWidth, height.toFloat(), paint!!)
            canvas.drawRect(width - borderWidth, 0f, width.toFloat(), height.toFloat(), paint!!)
        }
    }

    /**
     * 画分割线
     */
    private fun drawDivider(canvas: Canvas) {
        if (dividerWidth > 0) {
            paint!!.color = dividerColor
            for (i in 0 until length - 1) {
                val left = unitWidth * (i + 1) + dividerWidth * i + borderWidth
                canvas.drawRect(left, 0f, left + dividerWidth, height.toFloat(), paint!!)
            }
        }
    }

    /**
     * 画分割线
     */
    private fun drawRect(canvas: Canvas) {
        if (dividerWidth > 0) {
            paint!!.color = dividerColor
            paint!!.style = Paint.Style.STROKE
            for (i in 0 until length) {
                if (i == 0) {
                    canvas.drawRect(i * unitWidth, 0f, unitWidth, unitWidth, paint!!)
                } else {
                    canvas.drawRect(i * unitWidth + padding * i + borderWidth * i, 0f, (i + 1) * unitWidth + padding * i + borderWidth * i, unitWidth, paint!!)
                }
            }
        }
    }

    /**
     * 画下划线
     *
     * @param canvas
     */
    private fun drawUnderline(canvas: Canvas) {
        if (dividerWidth > 0) {
            paintLine?.style = Paint.Style.STROKE
            paintLine?.strokeWidth = dip2px(1f).toFloat()
            for (i in 0 until length) {
                if (i > code?.length?.minus(1) ?: 0) {
                    paintLine?.color = borderColor
                } else {
                    paintLine?.color = borderSelectColor
                }
                canvas.drawLine(i * unitWidth + padding * i + borderWidth * i, unitWidth, (i + 1) * unitWidth + padding * i + borderWidth * i, unitWidth, paintLine!!)
            }

        }
    }

    /**
     * 画输入文字
     */
    private fun drawValue(canvas: Canvas) {
        if (pointRadius > 0) {
            paint?.color = codeColor
            paint?.textSize = textSize
            paint?.textAlign = Paint.Align.CENTER
            for (i in 0 until code!!.length) {
                val left = unitWidth * i + dividerWidth * i + borderWidth - padding / 2
                canvas.drawText(code!![i] + "",
                        left + unitWidth / 2 + padding * i,
                        getTextBaseLine(0f, height - padding, paint!!),
                        paint)
            }
        }
    }

    /**
     * @param backgroundTop
     * @param backgroundBottom
     * @param paint
     * @return paint绘制居中文字时，获取文本底部坐标
     */
    fun getTextBaseLine(backgroundTop: Float, backgroundBottom: Float, paint: Paint): Float {
        val fontMetrics = paint.fontMetrics
        return (backgroundTop + backgroundBottom - fontMetrics.bottom - fontMetrics.top) / 2
    }

    /**
     * 画密码点
     */
    private fun drawPoint(canvas: Canvas) {
        if (pointRadius > 0) {
            paint?.color = codeColor
            (0 until code!!.length)
                    .map { unitWidth * it + dividerWidth * it + borderWidth }
                    .forEach { canvas.drawCircle(it + unitWidth / 2, (height / 2).toFloat(), pointRadius, paint!!) }
        }
    }

    fun input(number: String) {
        if (code!!.length < length) {
            code += number
            listener?.onValueChanged(code)
            if (code?.length == length) {
                listener?.onComplete(code!!)
            }
            invalidate()
        }
    }

    fun delete() {
        if (code.isNullOrEmpty().not()) {
            code = code?.substring(0, code!!.length - 1)
            listener?.onValueChanged(code)
            invalidate()
        }
    }

    fun clear() {
        if (code.isNullOrEmpty().not()) {
            code = ""
            listener?.onValueChanged(code)
            invalidate()
        }
    }

    fun getLength(): Int {
        return length
    }

    fun setLength(length: Int) {
        this.length = length
        invalidate()
    }

    fun getBorderColor(): Int {
        return borderColor
    }

    fun setBorderColor(borderColor: Int) {
        this.borderColor = borderColor
        invalidate()
    }

    fun getBorderWidth(): Float {
        return borderWidth
    }

    fun setBorderWidth(borderWidth: Float) {
        this.borderWidth = borderWidth
        invalidate()
    }

    fun getDividerColor(): Int {
        return dividerColor
    }

    fun setDividerColor(dividerColor: Int) {
        this.dividerColor = dividerColor
        invalidate()
    }

    fun getDividerWidth(): Float {
        return dividerWidth
    }

    fun setDividerWidth(dividerWidth: Float) {
        this.dividerWidth = dividerWidth
        invalidate()
    }

    fun getCode(): String? {
        return code
    }

    fun setCode(code: String) {
        this.code = code
        invalidate()
    }

    fun getCodeColor(): Int {
        return codeColor
    }

    fun setCodeColor(codeColor: Int) {
        this.codeColor = codeColor
        invalidate()
    }

    fun getPointRadius(): Float {
        return pointRadius
    }

    fun setPointRadius(pointRadius: Float) {
        this.pointRadius = pointRadius
        invalidate()
    }

    fun getTextSize(): Float {
        return textSize
    }

    fun setTextSize(textSize: Float) {
        this.textSize = textSize
        invalidate()
    }

    fun getPwdType(): Int {
        return pwdType
    }

    fun setPwdType(pwdType: Int) {
        this.pwdType = pwdType
        invalidate()
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setListener(init: ListenerDelegate.() -> Unit) {
        val delegate = ListenerDelegate()
        delegate.init()
        setListener(delegate)
    }

    interface Listener {
        fun onValueChanged(value: String?)
        fun onComplete(value: String)
    }

    class ListenerDelegate : Listener {

        var onValueChanged: ((value: String?) -> Unit)? = null
        var onComplete: ((value: String?) -> Unit)? = null

        override fun onValueChanged(value: String?) {
            onValueChanged?.let { it(value) }
        }

        override fun onComplete(value: String) {
            onComplete?.let { it(value) }
        }
    }

    companion object {
        val SHOW_TYPE_WORD = 1
        val SHOW_TYPE_PASSWORD = 2
    }

    fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}