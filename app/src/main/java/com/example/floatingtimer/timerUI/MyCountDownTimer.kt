package com.example.floatingtimer.timerUI

import android.os.CountDownTimer

class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long, val countdownRingView: CountdownRingView):
    CountDownTimer(millisInFuture, countDownInterval) {

    private var millisUntilFinished = 0L
    private var isCounting = false

    override fun onTick(millisUntilFinished: Long) {
        countdownRingView.onCountingDown(millisUntilFinished)
        this.millisUntilFinished = millisUntilFinished
    }

    override fun onFinish() {
        isCounting = false
        cancel()
    }
}