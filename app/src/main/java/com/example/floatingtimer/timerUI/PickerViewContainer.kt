package com.example.floatingtimer.timerUI

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.floatingtimer.myinterface.ViewPager2InputControl
import kotlin.math.abs

class PickerViewContainer @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null):
    RelativeLayout(context, attrs){

    private var initialX = 0f
    private var initialY = 0f

    private var viewPager2InputControl: ViewPager2InputControl? = null

    //向外逐层遍历找到父View中的ViewPager2
    private val parentViewPager2: ViewPager2?
        get() {
            var v: View? = parent as? View  //as?表示若as不成功则返回null
            while (v != null && v !is ViewPager2) {
                v = v.parent as? View
            }
            return v as? ViewPager2
        }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        when (ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//                initialX = ev.x
//                initialY = ev.y
////                parentViewPager2?.requestDisallowInterceptTouchEvent(true)
//                viewPager2InputControl?.setIsUserInputEnable(false)
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                var endX = ev.x
//                var endY = ev.y
//                val disX = abs(endX - initialX)
//                val disY = abs(endY - initialY)
//                if (disX > disY) {
// //                   parentViewPager2?.requestDisallowInterceptTouchEvent(false)
//                    viewPager2InputControl?.setIsUserInputEnable(true)
//                } else {
////                    parentViewPager2?.requestDisallowInterceptTouchEvent(true)
//                    viewPager2InputControl?.setIsUserInputEnable(false)
//                }
//            }
//            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                viewPager2InputControl?.setIsUserInputEnable(false)
//            }
//        }

        return super.dispatchTouchEvent(ev)
    }

    fun setViewPager2InputControl(viewPager2InputControl: ViewPager2InputControl) {
        this.viewPager2InputControl = viewPager2InputControl
    }


//    //向外逐层遍历找到父View中的ViewPager2
//    private val parentViewPager2: ViewPager2?
//        get() {
//            var v: View? = parent as? View  //as?表示若as不成功则返回null
//            while (v != null && v !is ViewPager2) {
//                v = v.parent as? View
//            }
//            return v as? ViewPager2
//        }

//    init {
//        parentViewPager2?.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//
//        Log.d("MyPickerContainer", "${event}")
//
//        return super.onTouchEvent(event)
//    }
//
//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        if (ev.action == MotionEvent.ACTION_DOWN) {
//            initialX = ev.x
//            initialY = ev.y
//            //DOWN事件直接强制禁止父View拦截事件，后续事件先交给子PickerView判断是否能消费
//            //如果这一块不强制禁止父View拦截事件，会导致后续事件没到子PickerView就被父View拦截了
//            parentViewPager2?.requestDisallowInterceptTouchEvent(true)
//         //   parent.requestDisallowInterceptTouchEvent(true)
//        } else if (ev.action == MotionEvent.ACTION_MOVE) {
//            //计算手指滑动距离
//            val dx = abs(ev.x - initialX)
//            val dy = abs(ev.y - initialY)
//            if (dx < dy * 10) {
//                parentViewPager2?.requestDisallowInterceptTouchEvent(true)
//        //        parent.requestDisallowInterceptTouchEvent(true)
//            } else {
//                parentViewPager2?.requestDisallowInterceptTouchEvent(false)
//        //        parent.requestDisallowInterceptTouchEvent(false)
//            }
//        }
//       return super.onInterceptTouchEvent(ev)
//    }
}
