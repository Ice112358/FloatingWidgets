package com.example.floatingtimer

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import kotlin.math.*

class SpotlightItem @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
    private val currentX = 150f
    private val currentY = 150f     //圆心的初始位置
    private val radius = 150f

    private val paint = Paint().apply {
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)    //一会将圆抠成透明的
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension((radius * 2).toInt(), (radius * 2).toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        canvas.drawColor(Color.parseColor("#37000000"))    //画布颜色

        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_HARDWARE, null)     //禁用硬件加速

        canvas.drawCircle(currentX, currentY, radius, paint)
    }

//    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
//        when (motionEvent.action) {
//            MotionEvent.ACTION_DOWN -> {
//                return false
//            }
//            MotionEvent.ACTION_MOVE -> {
//                return if (!isPointInCircle(motionEvent.rawX, motionEvent.rawY, currentX, currentY, radius)) {
//                    false
//                } else {
//                    currentX = motionEvent.rawX
//                    currentY = motionEvent.rawY
//                    invalidate()
//                    true
//                }
//            }
//            MotionEvent.ACTION_UP -> {
//                return false
//            }
//        }
//        return false
//    }

    private fun isPointInCircle(x: Float, y: Float, circleX: Float, circleY: Float, radius: Float) =
        sqrt( (x - circleX).toDouble().pow(2) + (y - circleY).toDouble().pow(2) ) <= radius

}