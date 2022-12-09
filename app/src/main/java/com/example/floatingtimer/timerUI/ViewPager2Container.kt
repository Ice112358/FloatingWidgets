package com.example.floatingtimer.timerUI

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.MotionEventCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class ViewPager2Container @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    RelativeLayout(context, attrs, defStyleAttr){

    //记录手指按下时的坐标
    var mInitialTouchX = -1f
    var mInitialTouchY = -1f

    var mViewPager2: ViewPager2? = null

    //判断是否需要在分发原MotionEvent事件之前，分发一个滑动距离为滑动阈值的事件，让ViewPager2（内部自带的RecyclerView）无法滑动
//    var splitFlag = false

    init {
        mViewPager2 = getChildAt(0) as ViewPager2
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            if (it.action == MotionEvent.ACTION_DOWN) {
                mInitialTouchX = it.x
                mInitialTouchY = it.y
//                splitFlag = true
                (mViewPager2 as ViewPager2)?.isUserInputEnabled = false
            } else if (it.action == MotionEvent.ACTION_MOVE) {
                var isPickerScrolling = Math.abs(ev.x - mInitialTouchX) < Math.abs(ev.y - mInitialTouchY)
                (mViewPager2 as ViewPager2)?.isUserInputEnabled = !isPickerScrolling
            } else {

            }
        }

        return super.dispatchTouchEvent(ev)
    }

    //获取RecyclerView的滑动阈值
    fun getRecyclerViewTouchSlop(recyclerView: RecyclerView): Int {
        var clazz = RecyclerView::class.java
        var field = clazz.getDeclaredField("mTouchSlop")
        field.isAccessible = true
        var touchSlop = field.get(recyclerView) as Int
        return touchSlop
    }

    //获取ViewGroup中的RecyclerView
    fun searchRecyclerView(vg: ViewGroup): RecyclerView? {
        for (i in 0 until vg.childCount) {
            var view = vg.getChildAt(i)
            if (view is RecyclerView) {
                return view
            }else if (view is ViewGroup) {
                var recyclerView = searchRecyclerView(view)
                if (recyclerView != null) {
                    return recyclerView
                }
            }
        }
        return null
    }

    fun searchViewPager2(vg: ViewGroup): ViewPager2? {
        for (i in 0 until vg.childCount) {
            var view = vg.getChildAt(i)
            if (view is ViewPager2) {
                return view
            } else if (view is ViewGroup) {
                var viewPager2 = searchViewPager2(view)
                if (viewPager2 != null) {
                    return viewPager2
                }
            }
        }
        return null
    }

//    private var mViewPager2: ViewPager2? = null
//    private var disallowParentInterceptDownEvent = true
//    private var startX = 0
//    private var startY = 0
//
//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        for (i in 0 until childCount) {
//            val childView = getChildAt(i)
//            if (childView is ViewPager2) {
//                mViewPager2 = childView
//                break
//            }
//        }
//        if (mViewPager2 == null) {
//            throw IllegalStateException("The root child of ViewPager2Container must contains a ViewPager2")
//        }
//    }
//
//    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
//        val doNotNeedIntercept = (
//                !mViewPager2!!.isUserInputEnabled || (mViewPager2?.adapter != null && mViewPager2?.adapter!!.itemCount <= 1)
//                )
//        if (doNotNeedIntercept) {
//            return super.onInterceptTouchEvent(ev)
//        }
//
//        when (ev?.action) {
//            MotionEvent.ACTION_DOWN -> {
//                startX = ev.x.toInt()
//                startY = ev.y.toInt()
//                parent.requestDisallowInterceptTouchEvent(!disallowParentInterceptDownEvent)
//            }
//            MotionEvent.ACTION_MOVE -> {
//                val endX = ev.x.toInt()
//                val endY = ev.y.toInt()
//                val disX = abs(endX - startX)
//                val disY = abs(endY - startY)
//                if (mViewPager2!!.orientation == ViewPager2.ORIENTATION_VERTICAL) {
//                    onVerticalActionMove(endY, disX, disY)
//                } else if (mViewPager2!!.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
//                    onHorizontalActionMove(endX, disX, disY)
//                }
//            }
//            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
//                parent.requestDisallowInterceptTouchEvent(false)
//        }
//        return super.onInterceptTouchEvent(ev)
//    }
//
//    private fun onHorizontalActionMove(endX: Int, disX: Int, disY: Int) {
//        if (mViewPager2?.adapter == null) {
//            return
//        }
//        if (disX > disY) {
//            val currentItem = mViewPager2?.currentItem
//            val itemCount = mViewPager2?.adapter!!.itemCount
//            if (currentItem == 0 && endX - startX > 0) {    //endX - startX > 0: 手指向右滑
//                parent.requestDisallowInterceptTouchEvent(false)
//            } else {
//                parent.requestDisallowInterceptTouchEvent(
//                    currentItem != itemCount - 1 || endX - startX >= 0)
//            }
//        } else if (disY > disX) {
//            parent.requestDisallowInterceptTouchEvent(false)
//        }
//    }
//
//    private fun onVerticalActionMove(endY: Int, disX: Int, disY: Int) {
//        if (mViewPager2?.adapter == null) {
//            return
//        }
//        val currentItem = mViewPager2?.currentItem
//        val itemCount = mViewPager2?.adapter!!.itemCount
//        if (disY > disX) {
//            if (currentItem == 0 && endY - startY > 0) {    //endY - startY > 0: 手指向下滑
//                parent.requestDisallowInterceptTouchEvent(false)
//            } else {
//                parent.requestDisallowInterceptTouchEvent(
//                    currentItem != itemCount - 1 || endY - startY >= 0
//                )
//            }
//        } else if (disX > disY) {
//            parent.requestDisallowInterceptTouchEvent(false)
//        }
//    }
//
//    fun disallowParentInterceptDownEvent(disallowParentInterceptDownEvent: Boolean) {
//        this.disallowParentInterceptDownEvent = disallowParentInterceptDownEvent
//    }
}