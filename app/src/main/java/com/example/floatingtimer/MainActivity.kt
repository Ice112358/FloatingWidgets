package com.example.floatingtimer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.floatingtimer.databinding.ActivityMainBinding
import com.example.floatingtimer.service.FloatingWindowService
import com.example.floatingtimer.timerUI.CountdownOrTimerActivity
import com.example.floatingtimer.utils.MainViewModel
import com.example.floatingtimer.utils.Utils
import com.example.floatingtimer.viewpager2.ViewPager2SimpleActivity
import com.example.floatingtimer.viewpager2.ViewPager2TabActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startService(Intent(this, FloatingWindowService::class.java))
        binding.chronometerBtn.setOnClickListener(this)
        binding.timerBtn.setOnClickListener(this)
        binding.spotlightBtn.setOnClickListener(this)
        binding.removeBtn.setOnClickListener(this)
        binding.countdownBtn.setOnClickListener(this)

        binding.close.setOnClickListener {
            Toast.makeText(this, "Click Close", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onClick(v: View?) {
        when (v) {
            binding.chronometerBtn -> {
                Utils.checkFloatingWindowPermission(this) {
                    MainViewModel.whichFloatingWindow.postValue(1)
                }
            }

            binding.timerBtn -> {
                Utils.checkFloatingWindowPermission(this) {
                    MainViewModel.whichFloatingWindow.postValue(2)
                }
            }

            binding.spotlightBtn -> {
                Utils.checkFloatingWindowPermission(this) {
                    MainViewModel.whichFloatingWindow.postValue(3)
                }
            }

            binding.countdownBtn -> {
                Utils.checkFloatingWindowPermission(this) {
                    MainViewModel.whichFloatingWindow.postValue(4)
                }
            }

            binding.removeBtn -> {
                MainViewModel.whichFloatingWindow.postValue(0)
            }
        }
    }

    fun toViewPager2(view: View) {
        ViewPager2SimpleActivity.start(this)
    }

    fun toTabVP2(view: View) {
        ViewPager2TabActivity.start(this)
    }

    fun timerUI(view: View) {
        val intent = Intent(this, CountdownOrTimerActivity::class.java)
        startActivity(intent)
//        Utils.checkFloatingWindowPermission(this) {
//            MainViewModel.whichFloatingWindow.postValue(5)
        }

}