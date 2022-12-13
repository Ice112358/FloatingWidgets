package com.example.floatingwidgets.util

import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.example.floatingwidgets.myinterface.Callback
import com.example.floatingwidgets.widget.SpotlightView

class ItemViewTouchListener(
    private val wl: WindowManager.LayoutParams, private val windowManager: WindowManager,
    private val callback: Callback? = null): View.OnTouchListener {

    private var x = 0
    private var y = 0

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                x = motionEvent.rawX.toInt()
                y = motionEvent.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val nowX = motionEvent.rawX.toInt()
                val nowY = motionEvent.rawY.toInt()
                val movedX = nowX - x
                val movedY = nowY - y
                x = nowX
                y = nowY
                wl.apply {
                    x += movedX
                    y += movedY
                }
                //更新悬浮窗位置
                windowManager.updateViewLayout(view, wl)

                if (view is SpotlightView) {
                    val location = IntArray(2)
                    view.getLocationOnScreen(location)  //获取view相对屏幕的绝对坐标

                    //注意状态栏高度的补偿
                    callback?.onMove((location[0] + view.width / 2).toFloat(), (location[1] + view.height / 2 - 67.5 ).toFloat())

                }
            }
        }
        return false
    }

}