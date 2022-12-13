package com.example.floatingwidgets.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.floatingwidgets.myinterface.Callback

class SpotlightBackgroundView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null):
    View(context, attrs), Callback {
    private var currentX = 150f
    private var currentY = 150f
    private var radius = 150f

    private val paint = Paint().apply {
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)    //一会将圆抠成透明的
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.parseColor("#37000000"))    //画布颜色
//        canvas.drawColor(Color.RED)

        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_HARDWARE, null)

        canvas.drawCircle(currentX, currentY, radius, paint)
    }


    override fun onMove(x: Float, y: Float) {
        currentX = x
        currentY = y
        invalidate()
    }

}