package com.example.floatingtimer

import android.content.Context
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.floatingtimer.databinding.ChronometerItemBinding
import java.nio.file.attribute.AttributeView

class ChronometerItem @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, ): LinearLayout(context, attrs) {
    //@JvmOverloads注解指示Kotlin编译器为该函数生成替代默认参数值的重载。如果在xml文件中使用了这个View，就必须要用带attrs的构造函数

    private val binding = ChronometerItemBinding.inflate(LayoutInflater.from(context), this, true)  //父组件是this并绑定，即绑定自己

    private var rangeTime = 0

//    private val countDownTimer = object : CountDownTimer(30000, 1000) {
//        override fun onTick(millisUntilFinished: Long) {
//            Toast.makeText(context, "countdown", Toast.LENGTH_SHORT).show()
//        }
//
//        override fun onFinish() {
//            cancel()
//        }
//
//    }

    init {  //调用构造函数时就会调用init块
//        countDownTimer.start()
        binding.startBtn.setOnClickListener {
//            binding.chronometer.format = "计时开始：%s"
            binding.chronometer.setBase(SystemClock.elapsedRealtime())
            binding.chronometer.start()
        }

        binding.stopBtn.setOnClickListener {
            rangeTime = ( SystemClock.elapsedRealtime() - binding.chronometer.base ).toInt()
            binding.chronometer.stop()
        }

        binding.continueBtn.setOnClickListener {
            binding.chronometer.setBase(SystemClock.elapsedRealtime() - rangeTime)
            binding.chronometer.start()
        }

        binding.resetBtn.setOnClickListener {
            binding.chronometer.setText("00:00")
            binding.chronometer.setBase(SystemClock.elapsedRealtime())
            binding.chronometer.stop()
            binding.chronometer.onChronometerTickListener = null
        }

        binding.countdownBtn.setOnClickListener {
            binding.chronometer.setOnChronometerTickListener {
                binding.chronometer.setText(binding.chronometer.text.toString().substring(1))
                if (SystemClock.elapsedRealtime() - binding.chronometer.base >= 0)  binding.chronometer.stop()
            }
            binding.chronometer.setBase(SystemClock.elapsedRealtime() + 7000)
            binding.chronometer.start()
        }

    }

}