package com.example.floatingwidgets.widget

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.example.floatingwidgets.databinding.ViewCountdownBinding

class CountdownView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null): RelativeLayout(context, attributeSet) {
    private val binding = ViewCountdownBinding.inflate(LayoutInflater.from(context), this, true)

    private var minutes = 0L
    private var seconds = 30L
    private var countdownTime = minutes * 60 * 1000 + seconds * 1000

    private var isCounting = false

    private var countdownTimer: CountDownTimer? = null

    init {
        //CountDownTimer构造函数的第一个参数是倒计时长，但为了不丢掉最初的一秒，略微延长该参数；第二个参数是调用onTick函数的时间间隔
            countdownTimer = object : CountDownTimer(countdownTime + 100, 1000) {
                //millisUntilFinished是剩余倒计时长
            override fun onTick(millisUntilFinished: Long) {
                seconds = millisUntilFinished / 1000 % 60
                minutes = millisUntilFinished / 1000 / 60
                binding.countdownText.text = String.format("%02d:%02d", minutes, seconds)
            }
                //onFinish函数在倒计时结束时调用
            override fun onFinish() {
                cancel()    //释放倒计时器，不要忘记调用以造成内存泄露！
                isCounting = false
            }
        }

        binding.startOrStopBtn.setOnClickListener {
            if (!isCounting && binding.countdownText.text == "00:00") { //开始计时
                (countdownTimer as CountDownTimer).start()
                isCounting = true
            } else if (isCounting) {    //暂停计时
                countdownTime = minutes * 60 * 1000 + seconds * 1000    //minutes和seconds是随倒计时变化的，记录它俩即可
                (countdownTimer as CountDownTimer).cancel()     //释放倒计时器，为继续倒计时的时候创建新倒计时器准备
                isCounting = false
            } else {
                countdownTimer = object : CountDownTimer(countdownTime + 100, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        seconds = millisUntilFinished / 1000 % 60
                        minutes = millisUntilFinished / 1000 / 60
                        binding.countdownText.text = String.format("%02d:%02d", minutes, seconds)
                    }

                    override fun onFinish() {
                        cancel()
                        isCounting = false
                    }
                }
                (countdownTimer as CountDownTimer).start()
                isCounting = true
            }
        }

    }

}