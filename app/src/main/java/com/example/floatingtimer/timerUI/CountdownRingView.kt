package com.example.floatingtimer.timerUI

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import com.example.floatingtimer.R
import java.text.DecimalFormat
import java.text.NumberFormat

class CountdownRingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) :
    View(context, attrs, defStyleAttr) {
    private var minutes = 0L
    private var seconds = 0L
    private var totalCountdownTime = 0L
    private var startCountingDown = false   //开始倒计时之前设为true，开始倒计时后直到下一次倒计时开始前为false，用于开始倒计时
    private var finishCountingDown = false  //第一次倒计时结束后设为true，用于开启第二次及以后的倒计时

    private var radius = 0.0f   //圆环中圈半径
    private val mWidth          //该CountingDownRingView的宽和高（View为正方形）
        get() = radius * 12 / 5

    private var proportion = 1.0f   //倒计时剩余时间占总倒计时长的比例

    private var mTextSize = 0f  //倒计时间的字体大小
    private val f: NumberFormat = DecimalFormat("00")   //f为倒计时界面数字显示两位的格式设置

    private val backgroundPaint: Paint
    private val backgroundPath: Path    //若把圆环看作绿色的水，backgroundPath就是管道

    private var ringPaint: Paint    //lateinit is unnecessary: definitely initialized in constructors
    private var ringPath: Path

    private val coverPaint: Paint
    private var coverPath: Path     //解决圆环头尾部的StrokeCap处于同一图层并不互相覆盖的问题

    private var edgePaint: Paint
    private var edgeRingPath = Path()   //描边

    private var textPaint: Paint

    init {
        context?.withStyledAttributes(attrs, R.styleable.CountdownRingView) {
            radius = getDimension(R.styleable.CountdownRingView_radius, 100f)
            mTextSize = getDimension(R.styleable.CountdownRingView_textSize, 100f)
        }

        backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = radius / 5 + radius / 50
            color = Color.GRAY
        }
        backgroundPath = Path().apply {
            addArc(mWidth / 2 - radius, mWidth / 2 - radius,mWidth / 2 + radius, mWidth / 2 + radius,
                -90f, 360f
            )
        }

        ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = radius / 5    //圆环的宽度为 r / 5。这样设置实际上是以圆环中圈为中线，向两侧各延伸 r / 10
            strokeCap = Paint.Cap.ROUND
        }
        ringPath = Path().apply {
            addArc(mWidth / 2 - radius, mWidth / 2 - radius,mWidth / 2 + radius, mWidth / 2 + radius,
                -90f, (359 * proportion)
            )   //如果这里写360会导致圆环尾部StrokeCap的描边消失，原因应该是判定成闭合圆了
        }

        coverPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = radius / 5 - radius / 50  //要把头部的StrokeCap盖掉，但不能把环内侧的描边盖掉，所以减去 r / 50
            color = Color.GREEN
        //    color = Color.RED
            strokeCap = Paint.Cap.ROUND
        }
        coverPath = Path().apply {
            addArc(mWidth / 2 - radius, mWidth / 2 - radius,mWidth / 2 + radius, mWidth / 2 + radius,
            -97f, 6f)   //从-97°开始走6°是能把头部的StrokeCap盖掉的最小范围了
        }

        edgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = radius / 50   //描边的宽度为 r / 50
            color = Color.GRAY
        }
        ringPaint.getFillPath(ringPath, edgeRingPath)

        textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = mTextSize
            textAlign = Paint.Align.CENTER
        }
    }

    private val fontMetrics = textPaint.fontMetrics
    private val dy = - fontMetrics.ascent / 2 - fontMetrics.descent / 2     //dy为字体baseline和字体中线的偏移量

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawPath(backgroundPath, backgroundPaint)

        if (proportion > 0.25) {
            ringPaint.color = Color.GREEN
            textPaint.color = Color.BLACK
            canvas?.drawPath(ringPath, ringPaint)
            canvas?.drawPath(edgeRingPath, edgePaint)
           canvas?.drawPath(coverPath, coverPaint)
            val time = "${f.format(minutes)}:${f.format(seconds)}"
            canvas?.drawText(time, mWidth / 2, mWidth / 2 + dy, textPaint)
        } else {
            ringPaint.color = Color.RED
            textPaint.color = Color.RED
            canvas?.drawPath(ringPath, ringPaint)
            canvas?.drawPath(edgeRingPath, edgePaint)   //只剩最后四分之一了肯定不需要coverPath了，所以不绘制
            val time = "${f.format(minutes)}:${f.format(seconds)}"
            canvas?.drawText(time, mWidth / 2, mWidth / 2 + dy, textPaint)
        }
    }

    fun onCountingDown(leftCountdownTime: Long) {
        proportion = (leftCountdownTime).toFloat() / totalCountdownTime     //注意整数相除所得结果小于1时直接为0，转换为浮点数！

        Log.d("CountdownRingView", "$proportion")

        val displayTime = leftCountdownTime + 968    //为了显示出最初的一秒，这里补偿1秒
        seconds = (displayTime / 1000 % 60)
        minutes = (displayTime / 1000 / 60)

        if (leftCountdownTime == 0L) {  //为了不让补偿的1秒留在倒计时结束时，这里手动置0
            seconds = 0L
        }

        updatePaths()
        invalidate()
    }

    private fun updatePaths() {
        ringPath.apply {
            reset()
            addArc(
                mWidth / 2 - radius, mWidth / 2 - radius, mWidth / 2 + radius, mWidth / 2 + radius,
                -90f, (359 * proportion)
            )   //如果这里写360会导致圆环尾部StrokeCap的描边消失，原因应该是判定成闭合圆了
        }

        if (proportion > 0.9) {     //在这个范围内保留coverPath
            coverPath.apply {
                reset()
                addArc(
                    mWidth / 2 - radius, mWidth / 2 - radius, mWidth / 2 + radius, mWidth / 2 + radius,
                    -97 - 359 * (1 - proportion), 6f
                )
            }
        } else {
            coverPath.reset()
        }

        ringPaint.getFillPath(ringPath, edgeRingPath)

    }

    fun refresh() {
        proportion = 1.0f
        minutes = totalCountdownTime / 1000 / 60
        seconds = totalCountdownTime / 1000 % 60
        updatePaths()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(mWidth.toInt(), mWidth.toInt())
    }

    fun setRadiusAndTextSize(radius: Float, textSize: Float) {
        this.radius = radius
        mTextSize = textSize

        backgroundPath.apply {
            reset()
            addArc(mWidth / 2 - radius, mWidth / 2 - radius,mWidth / 2 + radius, mWidth / 2 + radius,
                -90f, 360f
            )
        }

        updatePaths()
        invalidate()
    }

    fun setTotalCountdownTime(totalCountdownTime: Long) {
        this.totalCountdownTime = totalCountdownTime
        minutes = totalCountdownTime / 1000 / 60
        seconds = totalCountdownTime / 1000 % 60
    }

    fun setStartCountingDown(startCountingDown: Boolean) {
        this.startCountingDown = startCountingDown
    }
    fun getStartCountingDown() = startCountingDown

    fun setFinishCountingDown(finishCountingDown: Boolean) {
        this.finishCountingDown = finishCountingDown
    }
    fun getFinishCountingDown() = finishCountingDown
}