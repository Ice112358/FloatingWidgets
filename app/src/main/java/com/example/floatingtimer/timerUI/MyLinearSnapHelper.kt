package com.example.floatingtimer.timerUI

import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

class MyLinearSnapHelper : LinearSnapHelper() {
    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        val snapView = super.findSnapView(layoutManager)
        if (snapView is TextView) {
            snapView.setTextColor(Color.GREEN)
        //    snapView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 43f)
        }
        return snapView
    }
}