package com.example.floatingtimer.timerUI

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class LooperLayoutManager(context: Context?): LinearLayoutManager(context) {
    private var looperEnable = true

    fun setLooperEnable(looperEnable: Boolean) {
        this.looperEnable = looperEnable
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams { //这个方法的作用是给 itemView 设置默认的LayoutParams

        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    override fun canScrollVertically(): Boolean {   //打开纵向滚动开关
        return true
    }
    //对RecyclerView进行初始化布局
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (itemCount <= 0) {
            return
        }
        //如果当前是准备状态，直接返回
        if (state?.isPreLayout == true) {
            return
        }
        //detachAndScrapAttachedViews(recycler)暂时将已经附加的view分离，缓存Scrap中，下次重新填充时直接拿出来复用，以准备重新对view进行排版
        recycler?.let { detachAndScrapAttachedViews(it) }

        var actualHeight = 0
        for (i in 0 until itemCount) {
            //初始化，将在屏幕内的view填充
            val itemView = recycler?.getViewForPosition(i)
            addView(itemView)
            //测量itemView的宽高
            itemView?.let { measureChildWithMargins(it, 0, 0) }
            val width = itemView?.let { getDecoratedMeasuredWidth(it) }
            val height = itemView?.let { getDecoratedMeasuredHeight(it) }
            //根据itemView的宽高进行布局
            if (itemView != null && width != null && height != null) {
                layoutDecorated(itemView, 0, actualHeight, width, actualHeight + height)
            }
            if (height != null) {
                actualHeight += height
            }
            //如果当前布局过的itemView的高度总和大于RecyclerView的高，则不再进行布局
            if (actualHeight > getHeight()) {
                break
            }
        }
    }
    //对RecyclerView进行滚动和回收itemView处理
    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        //纵向滑动的时候，对上下两边按顺序填充itemView
        val travel = fill(dy, recycler, state)
        if (travel == 0) {
            return 0
        }
        //滚动itemView
        offsetChildrenVertical(-travel)
        //回收已经不可见的itemView
        recyclerHideView(dy, recycler, state)
        return travel
    }
    //回收界面不可见的view
    private fun recyclerHideView(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        for (i in 0 until itemCount) {
            val view = getChildAt(i) ?: continue
            if (dy > 0) {
                //向上滚动，移除上方不在内容里的view
                if (view.bottom < 0) {
                    recycler?.let { removeAndRecycleView(view, it) }
                }
            } else {
                //向下滚动，移除下方不在内容里的view
                if (view.top > height) {
                    recycler?.let { removeAndRecycleView(view, it) }
                }
            }
        }
    }

    //上下滑动的时候。填充
    private fun fill(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        if (dy > 0) {   //向下滚动
            val lastView = getChildAt(childCount - 1) ?: return 0
            val lastPos = getPosition(lastView)
            //可见的最后一个itemView完全滑进来了，需要补充新的
            if (lastView.height < height) {
                var scrap: View? = null
                //判断可见的最后一个itemView的索引
                //如果是最后一个，则将下一个itemView设置成第一个，否则设置为当前索引的下一个
                if (lastPos == itemCount - 1) {
                    if (looperEnable) {
                        scrap = recycler?.getViewForPosition(0)
                    } else {
                    //    dy = 0
                    }
                } else {
                    scrap = recycler?.getViewForPosition(lastPos + 1)
                }
                if (scrap == null) {
                    return dy
                }
                //将新的itemView add进来并对其测量和布局
                addView(scrap)
                measureChildWithMargins(scrap, 0, 0)
                val width = getDecoratedMeasuredWidth(scrap)
                val height = getDecoratedMeasuredHeight(scrap)
                layoutDecorated(scrap, 0, lastView.bottom, width, lastView.bottom + height)
                return dy
            }
        } else {    //向上滚动
            val firstView = getChildAt(0) ?: return 0
            val firstPos = getPosition(firstView)
            //可见的第一个itemView完全滑进来了，需要在它上面补充新的
            if (firstView.top >= 0) {
                var scrap: View? = null
                //判断可见的第一个itemView的索引
                //如果是第一个，则将上一个itemView设置成最后一个，否则设置为当前索引的上一个
                if (firstPos == 0) {
                    if (looperEnable) {
                        scrap = recycler?.getViewForPosition(itemCount - 1)
                    } else {
                    //    dy = 0
                    }
                } else {
                    scrap = recycler?.getViewForPosition(firstPos - 1)
                }
                if (scrap == null) {
                //    return 0
                    return dy
                }
                addView(scrap, 0)
                measureChildWithMargins(scrap, 0,0)
                val width = getDecoratedMeasuredWidth(scrap)
                val height = getDecoratedMeasuredHeight(scrap)
                layoutDecorated(scrap, 0, firstView.top - height, width, firstView.top)
                return dy
            }
        }
        return dy
    }
}