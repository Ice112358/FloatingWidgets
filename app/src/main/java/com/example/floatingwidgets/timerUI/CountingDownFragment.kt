package com.example.floatingwidgets.timerUI

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.floatingwidgets.R
import com.example.floatingwidgets.databinding.FragmentCountingDownBinding
import com.example.floatingwidgets.myinterface.SetScreen

class CountingDownFragment: Fragment() {
    private var _binding: FragmentCountingDownBinding? = null
    private val binding get() = _binding!!

    private var minutes = 0L
    private var seconds = 0L
    private var totalCountdownTime = 0L
        get() = minutes * 60 * 1000 + seconds * 1000

    private var leftCountdownTime = 0L

    private var isCounting = false

    private var countdownTimer: CountDownTimer? = null
    private val countdownInterval = 32L     //Android系统帧率也就60fps，一帧16ms，所以32ms刷新一次就差不多了

    var setScreen: SetScreen? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountingDownBinding.inflate(inflater, container, false)

        minutes = arguments?.getLong("minutes")!!
        seconds = arguments?.getLong("seconds")!!

        refreshCountdownRing(totalCountdownTime, true)

        countdownTimer = createCountdownTimer(totalCountdownTime, countdownInterval)

        binding.threeButtons.startOrPause.startOrPause.setOnClickListener {
            binding.threeButtons.startOrPause.startOrPause.setImageResource(R.drawable.pause_selector)

            if (!isCounting && binding.countdownRing.getStartCountingDown()) { //开始计时
                if (binding.countdownRing.getFinishCountingDown()) {    //若已经完成第一次倒计时
                    binding.countdownRing.refresh()

                    countdownTimer = createCountdownTimer(totalCountdownTime, countdownInterval)
                }
                countdownTimer?.start()
                isCounting = true
                binding.countdownRing.setStartCountingDown(false)
            } else if (isCounting) {    //暂停计时
                binding.threeButtons.startOrPause.startOrPause.setImageResource(R.drawable.start_selector)

//                leftCountdownTime = minutes * 60 * 1000 + seconds * 1000 + 500   //minutes和seconds是随倒计时变化的，记录它俩即可
                                                                                  //同时为了防止暂停后继续时丢掉最初的1秒，补偿500ms
                countdownTimer?.cancel()     //释放倒计时器，为继续倒计时的时候创建新倒计时器准备
                countdownTimer = null
                isCounting = false
            } else {    //继续计时
                binding.threeButtons.startOrPause.startOrPause.setImageResource(R.drawable.pause_selector)

                countdownTimer = createCountdownTimer(leftCountdownTime, countdownInterval)
                countdownTimer?.start()
                isCounting = true
            }
        }

        binding.threeButtons.reset.reset.setOnClickListener {
            countdownTimer?.cancel()
            countdownTimer = null
            findNavController().navigate(R.id.action_countingDownFragment2_to_countdownChooseTimeFragment)
        }

        binding.threeButtons.fullScreen.fullScreen.setOnClickListener {
            Log.d("CountingDownFragment", "$setScreen")
            setScreen?.setScreen()
            binding.threeButtons.threeButtons.visibility = View.INVISIBLE
            binding.countdownRing.setRadiusAndTextSize(resources.displayMetrics.density * 60, resources.displayMetrics.scaledDensity * 34)
        }

        return binding.root
    }

    private fun createCountdownTimer(countdownTime: Long, countdownInterval: Long): CountDownTimer {

        //CountDownTimer构造函数的第一个参数是倒计时长，第二个参数是调用onTick函数的时间间隔
        return object : CountDownTimer(countdownTime, countdownInterval) {
            override fun onTick(millisUntilFinished: Long) {    //millisUntilFinished是剩余倒计时长
                leftCountdownTime = millisUntilFinished
//                minutes = millisUntilFinished / 1000 / 60
//                seconds = millisUntilFinished / 1000 % 60
                binding.countdownRing.onCountingDown(millisUntilFinished)
            }

            override fun onFinish() {   //onFinish函数在倒计时结束时调用
                binding.threeButtons.startOrPause.startOrPause.setImageResource(R.drawable.start_selector)

                binding.countdownRing.onCountingDown(0) //消除CountdownRing的onCountingDown中补偿的1秒
                cancel()    //释放倒计时器，不要忘记调用造成内存泄漏！
                countdownTimer = null
                isCounting = false
                binding.countdownRing.setStartCountingDown(true)
                binding.countdownRing.setFinishCountingDown(true)
            }
        }
    }

    private fun refreshCountdownRing(totalCountdownTime: Long, startCountingDown: Boolean) {
        binding.countdownRing.setTotalCountdownTime(totalCountdownTime)
        binding.countdownRing.setStartCountingDown(startCountingDown)
    }

    fun releaseCountdownTimer() {
        countdownTimer?.cancel()
        countdownTimer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}