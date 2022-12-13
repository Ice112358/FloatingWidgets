package com.example.floatingwidgets.widget

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.floatingwidgets.databinding.ViewCommonTimerBinding


class CommonTimerView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null): LinearLayout(context, attributeSet) {
    private val binding = ViewCommonTimerBinding.inflate(LayoutInflater.from(context), this, true)

    private val mHandler = Handler(Looper.getMainLooper())

    private var millisecondRecord = 0L
    private var startTime = 0L
    private var timeBuff = 0L

    private val runnable = object : Runnable {
        override fun run() {
            millisecondRecord = SystemClock.uptimeMillis() - startTime  //记录从计时开始经过的时间（startTime为从开机到按下start按钮为止的时间）
            val accumulatedTime = timeBuff + millisecondRecord  //用于暂停后继续计时的时候，把之前的计时续上

            val milliseconds = accumulatedTime % 1000
            val seconds = accumulatedTime / 1000 % 60
            val minutes = accumulatedTime / 1000 / 60
            binding.textView.text = String.format("%02d:%02d:%03d", minutes, seconds, milliseconds)

            mHandler.postDelayed(this, 0)   //立刻运行
        }
    }

    init {
        binding.start.setOnClickListener {
            startTime = SystemClock.uptimeMillis()  //获取从开机到当前（按下start按钮）为止的时间
            mHandler.postDelayed(runnable, 0)   //立刻运行Runnable

            binding.start.isEnabled = false
            binding.reset.isEnabled = false
        }

        binding.pause.setOnClickListener {
            timeBuff += millisecondRecord   //暂停时把计时器计时时间记录下来
            mHandler.removeCallbacks(runnable)  //删除指定线程对象。停下秒表

            binding.start.isEnabled = true
            binding.reset.isEnabled = true
        }

        binding.reset.setOnClickListener {
            millisecondRecord = 0L
            timeBuff = 0L
            binding.textView.text = "00:00:00"
        }
    }
}