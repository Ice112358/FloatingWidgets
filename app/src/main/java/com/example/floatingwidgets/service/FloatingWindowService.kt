package com.example.floatingwidgets.service

import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import androidx.lifecycle.LifecycleService
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.example.floatingwidgets.timerUI.CountdownOrTimerView
import com.example.floatingwidgets.util.MainViewModel
import com.example.floatingwidgets.util.ItemViewTouchListener
import com.example.floatingwidgets.util.Util
import com.example.floatingwidgets.widget.*

class FloatingWindowService : LifecycleService() {
    private lateinit var windowManager: WindowManager

    private val floatRootViewList = arrayListOf<View>()

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        MainViewModel.apply {
            //判断加载哪个布局
            whichFloatingWindow.observe(this@FloatingWindowService) {
                when (it) {
                    0 -> {  //清除所有悬浮窗
                        removeAllFloatingWindows()
                    }

                    1 -> {
                        val chronometerView = ChronometerView(this@FloatingWindowService)
                        val layoutParamsOfChronometerItem = setFloatingWindow(chronometerView)
                        showFloatingWindow(chronometerView, layoutParamsOfChronometerItem)
                    }

                    2 -> {
                        val commonTimerView = CommonTimerView(this@FloatingWindowService)
                        val layoutParamsOfTimerItem = setFloatingWindow(commonTimerView)
                        showFloatingWindow(commonTimerView, layoutParamsOfTimerItem)
                    }

                    3 -> {
                        val spotlightBackgroundView = SpotlightBackgroundView(this@FloatingWindowService)
                        val layoutParamOfBackground = setFloatingWindow(spotlightBackgroundView, false, false)

                        val spotlightView = SpotlightView(this@FloatingWindowService)
                        val layoutParamOfItem = setFloatingWindow(spotlightView, true, false)

                        spotlightView.setOnTouchListener(ItemViewTouchListener(layoutParamOfItem, windowManager, spotlightBackgroundView))

                        showFloatingWindow(spotlightBackgroundView, layoutParamOfBackground)

                        showFloatingWindow(spotlightView, layoutParamOfItem)
                    }

                    4 -> {
                        val countdownView = CountdownView(this@FloatingWindowService)
                        val layoutParamsOfCountdownView = setFloatingWindow(countdownView)
                        showFloatingWindow(countdownView, layoutParamsOfCountdownView)
                    }

                    5 -> {
                        val countdownOrTimerView = CountdownOrTimerView(this@FloatingWindowService)
                        val layoutParamsOfCountdownOrTimerView = setFloatingWindow(countdownOrTimerView)
                        showFloatingWindow(countdownOrTimerView, layoutParamsOfCountdownOrTimerView)
                    }
                }
            }
        }
    }

    //创建悬浮窗
    private fun setFloatingWindow(view: View, touchable: Boolean = true, isSingleFloatingWindow: Boolean = true): WindowManager.LayoutParams {
        val layoutParams = WindowManager.LayoutParams().apply {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            format = PixelFormat.RGBA_8888  //RGBA_8888为android的一种32位颜色格式
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

            if (touchable) {
                width = WRAP_CONTENT
                height = WRAP_CONTENT
                gravity = Gravity.START or Gravity.TOP  //默认是放在屏幕中间
            } else {
                width = MATCH_PARENT
                height = MATCH_PARENT
                flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE   //设置不可点击，让点击穿透到下一层
            }
        }
        if (isSingleFloatingWindow) {
            view.setOnTouchListener(ItemViewTouchListener(layoutParams, windowManager))
        } else {
        }
        return layoutParams
    }

    //显示悬浮窗
    private fun showFloatingWindow(view: View, layoutParams: WindowManager.LayoutParams) {
        windowManager.addView(view, layoutParams)
        floatRootViewList.add(view)
    }

    //清除所有悬浮窗
    private fun removeAllFloatingWindows() {
        for (floatRootView in floatRootViewList) {
            if (!Util.isNull(floatRootView)) {
                if (!Util.isNull(floatRootView?.windowToken)) {
                    if (!Util.isNull(windowManager)) {
                        windowManager?.removeView(floatRootView)
                    }
                }
            }
        }
        floatRootViewList.clear()
    }

}