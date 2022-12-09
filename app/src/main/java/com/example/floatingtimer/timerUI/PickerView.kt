package com.example.floatingtimer.timerUI

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.math.abs

interface OnSelectListener {
    fun onSelect(text: String)
}

//思路是在DOWN和MOVE的时候先画出移动之后的list（夹杂list头尾调整），在UP的时候“吸附”中心项

const val TAG = "PickerView"
//text之间间距和minTextSize之比，能调整显示几个item
const val MARGIN_ALPHA = 1.9f
//自动回滚到中间的速度
const val SPEED = 1

class PickerView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private var mDataList = ArrayList<String>()

    private val mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            speedY = if (abs(velocityY) < 5000) {
                velocityY
            } else {
                velocityY / abs(velocityY) * 5000
            }

            return super.onFling(e1, e2, velocityX, velocityY)
        //    return true
        }
    })

    private var speedY = 0f     //滑动松手后Y方向速度

    //选中的位置，这个位置是mDataList的中心位置，一直不变（等于list.size / 2）
    private var mCurrentSelected = 0

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = mTextColor
    }

    private var mMaxTextSize = 80f
    private var mMinTextSize = 40f

    private val mMaxTextAlpha = 255
    private val mMinTextAlpha = 60

    private val mTextColor = 0x333333

    private var mViewHeight = 0
    private var mViewWidth = 0

    //记录event.y用，见触摸事件中的逻辑
    private var mLastDownY = 0f

    //滑动的距离
    private var mMoveLen = 0f

    private var isInit = false
    private var mSelectListener: OnSelectListener? = null
    private val timer = Timer()
    private var mTask: MyTimerTask? = null


    val updateHandler = object : android.os.Handler() {
        override fun handleMessage(msg: Message) {
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0f
                speedY = 0f
                if (mTask != null) {
                    mTask!!.cancel()
                    mTask = null
                    performSelect()
                }
            } else {
                    if (abs(speedY) > 1000) {
                        mMoveLen +=  speedY / 100

                        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
                            //往下滑超过离开距离（能使中心变成上一个item的滑动距离）
                            moveTailToHead()
                            mMoveLen -= MARGIN_ALPHA * mMinTextSize
                        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
                            //往上滑超过离开距离
                            moveHeadToTail()
                            mMoveLen += MARGIN_ALPHA * mMinTextSize
                        }

                        val a = 2
                        if (speedY > 0) {
                            speedY -= a
                        } else {
                            speedY += a
                        }

                        invalidate()
                    } else {
                        //这里mMoveLen / Math.abs(mMoveLen)是为了取出mMoveLen的正负号，以实现上滚或下滚
                        mMoveLen = (mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED)
                        invalidate()
                    }
            }
        }
    }

    fun setOnSelectListener(listener: OnSelectListener) {
        mSelectListener = listener
    }

    private fun performSelect() {   //本次滑动结束时回调该方法，但具体的逻辑是由MainActivity（外部）决定的
        mSelectListener?.onSelect(mDataList.get(mCurrentSelected))
    }

    fun setData(datas: ArrayList<String>) {
        mDataList = datas
        mCurrentSelected = datas.size / 2
        invalidate()
    }

    //根据index将该item置于中心
    fun setSelected(selected: Int) {
        mCurrentSelected = selected
        val distance = mDataList.size / 2 - mCurrentSelected
        if (distance < 0) {
            var i = 0
            while (i < -distance) {
                moveHeadToTail()
                mCurrentSelected--
                i++
            }
        } else if (distance > 0) {
            var i = 0
            while (i < distance) {
                moveTailToHead()
                mCurrentSelected++
                i++
            }
        }
        invalidate()
    }

    fun setSelected(mSelectItem: String) {
        for (i in 0 until mDataList.size) {
            if (mDataList.get(i).equals(mSelectItem)) {
                setSelected(i)
                break
            }
        }
    }

    private fun moveHeadToTail() {
        val head = mDataList.get(0)
        mDataList.removeAt(0)
        mDataList.add(head)
    }

    private fun moveTailToHead() {
        val tail = mDataList.get(mDataList.size - 1)
        mDataList.removeAt(mDataList.size - 1)
        mDataList.add(0, tail)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewHeight = measuredHeight
        mViewWidth = measuredWidth
        //按照View的高度计算字体大小
        mMaxTextSize = mViewHeight / 4f
        mMinTextSize = mMaxTextSize / 2f

        isLongClickable = true

        isInit = true
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //根据index绘制view
        if (isInit) drawData(canvas)
    }

    private fun drawData(canvas: Canvas?) {
        //先绘制选中的text再往上往下绘制其余的text
//        val scale = parabola(mViewHeight / 4f, mMoveLen)  //用抛物线模型
        val scale = linearFun(mViewHeight / 2f, mMoveLen)   //用线性模型
        val size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize
        mPaint.color = Color.GREEN  //中心项绘制成绿色
        mPaint.textSize = size
        mPaint.alpha = ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha).toInt()
        //text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        val x = mViewWidth / 2
        val y = mViewHeight / 2 + mMoveLen
        val fmi = mPaint.fontMetricsInt
        val baseline = y - ( fmi.bottom / 2.0 + fmi.top / 2.0 )

        canvas?.drawText(
            mDataList.get(mCurrentSelected), x.toFloat(),
            baseline.toFloat(), mPaint
        )
        //绘制上方data
        mPaint.color = mTextColor   //其他项绘制成黑色
        var i = 1
        while (mCurrentSelected - i >= 0) {
            drawOtherText(canvas, i, -1)
            i++
        }
        //绘制下方data
        i = 1
        while (mCurrentSelected + i < mDataList.size) {
            drawOtherText(canvas, i, 1)
            i++
        }
    }

    //position: 距离mCurrentSelected的index差值，type: 1表示向下绘制，-1表示向上绘制
    private fun drawOtherText(canvas: Canvas?, position: Int, type: Int) {
        val d = MARGIN_ALPHA * mMinTextSize * position + type * mMoveLen    //与随后要成为中心的item的距离
//        val scale = parabola(mViewHeight / 4f, d)     //用抛物线模型
        val scale = linearFun(mViewHeight / 2f, d)  //用线性模型
        val size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize
        mPaint.textSize = size
        mPaint.alpha = ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha).toInt()
        val y = mViewHeight / 2.0 + type * d
        val fmi = mPaint.fontMetricsInt
        val baseline = y - ( fmi.bottom / 2.0 + fmi.top / 2.0 )
        canvas?.drawText(mDataList.get(mCurrentSelected + type * position), mViewWidth / 2f, baseline.toFloat(), mPaint)
    }

    private fun parabola(zero: Float, x: Float): Float {    //parabola n.抛物线，zero为零点坐标，x为偏移量，return scale(缩放倍率)
        val f = 1 - Math.pow((x / zero).toDouble(), 2.0)
        return if (f < 0) 0f else f.toFloat()
    }

    private fun linearFun(zero: Float, x: Float): Float {
        val scale = 1 - abs(x) / zero
        return if (scale < 0) 0f else scale
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        mGestureDetector.onTouchEvent(event)

        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                doDown(event)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                doMove(event)
                Log.d("Len", "${mMoveLen}")
                return true
            }
            MotionEvent.ACTION_UP -> {
                doUp(event)
                return true
            }
        }
        return false
    }


    private fun doUp(event: MotionEvent?) {
        //抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0f
            return
        }
        if (mTask != null) {
            mTask!!.cancel()
            mTask = null
        }
        Log.d("speedY", "$speedY")
        mTask = MyTimerTask(updateHandler)
        timer.schedule(mTask, 0, 10)
    }

    private fun doMove(event: MotionEvent?) {
        mMoveLen += (event?.y!! - mLastDownY)

        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
            //往下滑超过离开距离（能使中心变成上一个item的滑动距离）
            moveTailToHead()
            mMoveLen -= MARGIN_ALPHA * mMinTextSize
        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
            //往上滑超过离开距离
            moveHeadToTail()
            mMoveLen += MARGIN_ALPHA * mMinTextSize
        }

        mLastDownY = event?.y!!
        invalidate()
    }

    private fun doDown(event: MotionEvent?) {
        if (mTask != null) {
            mTask!!.cancel()
            mTask = null
        }
        mLastDownY = event?.y!!
    }

    class MyTimerTask(val handler: android.os.Handler): TimerTask() {

        override fun run() {
            handler.sendMessage(handler.obtainMessage())
        }
    }

}