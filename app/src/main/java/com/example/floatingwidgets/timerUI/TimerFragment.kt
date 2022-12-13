package com.example.floatingwidgets.timerUI

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.floatingwidgets.R
import com.example.floatingwidgets.databinding.FragmentTimerBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TimerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private val mHandler = Handler(Looper.getMainLooper())

    private var millisecondRecord = 0L
    private var startTime = 0L
    private var timeBuff = 0L

    private var isCounting = false

    private val runnable = object : Runnable {
        override fun run() {
            millisecondRecord = SystemClock.uptimeMillis() - startTime  //记录从计时开始经过的时间（startTime为从开机到按下start按钮为止的时间）
            val accumulatedTime = timeBuff + millisecondRecord  //用于暂停后继续计时的时候，把之前的计时续上

            val milliseconds = accumulatedTime % 1000
            val seconds = accumulatedTime / 1000 % 60
            val minutes = accumulatedTime / 1000 / 60 % 60
            val hours = accumulatedTime / 1000 / 60 / 60
  //          binding.timeTextView.text = String.format("%02d:%02d:%03d", minutes, seconds, milliseconds)
  //          binding.timeTextView.text = String.format("%02d:%02d", minutes, seconds)

            binding.hourMinuteSecond.secondRight.text = (seconds % 10).toString()
            binding.hourMinuteSecond.secondLeft.text = (seconds / 10).toString()
            binding.hourMinuteSecond.minuteRight.text = (minutes % 10).toString()
            binding.hourMinuteSecond.minuteLeft.text = (minutes / 10).toString()
            binding.hourMinuteSecond.hourRight.text = (hours % 10).toString()
            binding.hourMinuteSecond.hourLeft.text = (hours / 10).toString()

            mHandler.postDelayed(this, 0)   //立刻运行
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTimerBinding.inflate(inflater, container, false)

        binding.start.setOnClickListener {
            startTime = SystemClock.uptimeMillis()  //获取从开机到当前（按下start按钮）为止的时间
            mHandler.postDelayed(runnable, 0)   //立刻运行Runnable

            isCounting = true
            binding.start.visibility = View.INVISIBLE
            binding.threeButtons.startOrPause.startOrPause.setImageResource(R.drawable.pause_selector)
            binding.threeButtons.threeButtons.visibility = View.VISIBLE
        }

        binding.threeButtons.startOrPause.startOrPause.setOnClickListener {
            if (isCounting) {
                binding.threeButtons.startOrPause.startOrPause.setImageResource(R.drawable.start_selector)

                isCounting = false
                timeBuff += millisecondRecord   //暂停时把计时器计时时间记录下来
                mHandler.removeCallbacks(runnable)  //删除指定线程对象。停下秒表
            } else {
                binding.threeButtons.startOrPause.startOrPause.setImageResource(R.drawable.pause_selector)

                startTime = SystemClock.uptimeMillis()  //获取从开机到当前（按下start按钮）为止的时间
                mHandler.postDelayed(runnable, 0)   //立刻运行Runnable
                isCounting = true
            }
        }

        binding.threeButtons.reset.reset.setOnClickListener {
            binding.threeButtons.startOrPause.startOrPause.setImageResource(R.drawable.start_selector)

            mHandler.removeCallbacks(runnable)  //删除指定线程对象。停下秒表
            isCounting =false
            millisecondRecord = 0L
            timeBuff = 0L
 //           binding.timeTextView.text = "00:00:00"

            binding.hourMinuteSecond.secondRight.text = "0"
            binding.hourMinuteSecond.secondLeft.text =  "0"
            binding.hourMinuteSecond.minuteRight.text =  "0"
            binding.hourMinuteSecond.minuteLeft.text =  "0"
            binding.hourMinuteSecond.hourRight.text =  "0"
            binding.hourMinuteSecond.hourLeft.text =  "0"
        }


        return binding.root
    }

    fun releaseTimer() {
        mHandler.removeCallbacks(runnable)  //删除指定线程对象。停下秒表
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mHandler.removeCallbacks(runnable)  //删除指定线程对象。停下秒表
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TimerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TimerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}