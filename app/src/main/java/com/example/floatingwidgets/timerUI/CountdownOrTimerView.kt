package com.example.floatingwidgets.timerUI

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.floatingwidgets.databinding.ActivityCountdownOrTimerBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs

class CountdownOrTimerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null): FrameLayout(context, attrs) {
    private val binding = ActivityCountdownOrTimerBinding.inflate(LayoutInflater.from(context), this, true)

    private val timerFragment = TimerFragment()

 //   private val countdownContainerFragment = FragmentContainerFragment()

    private var initialX = 0f
    private var initialY = 0f

    private val fragmentList = arrayListOf<Fragment>().apply {
//        add(countdownChooseTimeFragment)
//        add(countdownChooseMoreTimeFragment)
//        add(countdownContainerFragment)
        add(timerFragment)
    }
    private val titles = arrayListOf("倒计时", "计时")

    init {
        initViewPager2()
    }

    private fun initViewPager2() {
        //offscreenPageLimit默认不开启预加载
        //offscreenPageLimit设置为1表示缓存前一页，预加载下一页，包含当前页一共三页
        binding.viewPager2.offscreenPageLimit = 1
   //     binding.viewPager2.adapter = ViewPager2CountdownOrTimerAdapter(, fragmentList)

        TabLayoutMediator(
            binding.countdownOrTimerTabLayout,
            binding.viewPager2,
            object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    tab.text = titles[position]
                }
            }
        ).attach()

//        binding.tabTest.setOnClickListener {
//
//            binding.countdownOrTimerTabLayout.setTabTextColors(Color.BLACK, Color.BLACK)
//            binding.countdownOrTimerTabLayout.removeTabAt(0)
//            binding.countdownOrTimerTabLayout.setSelectedTabIndicatorHeight(0)
//
//        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = ev.x
                initialY = ev.y
//                parentViewPager2?.requestDisallowInterceptTouchEvent(true)
                //       setIsUserInputEnable(false)
            }

            MotionEvent.ACTION_MOVE -> {
                var endX = ev.x
                var endY = ev.y
                val disX = abs(endX - initialX)
                val disY = abs(endY - initialY)
                if (disX > disY * 5) {
                    //                   parentViewPager2?.requestDisallowInterceptTouchEvent(false)
                    setIsUserInputEnable(true)
                } else {
//                    parentViewPager2?.requestDisallowInterceptTouchEvent(true)
                    setIsUserInputEnable(false)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                setIsUserInputEnable(true)
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun setIsUserInputEnable(isEnable: Boolean) {
        binding.viewPager2.isUserInputEnabled = isEnable
    }
}