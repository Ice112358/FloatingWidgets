package com.example.floatingwidgets.timerUI

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.floatingwidgets.myinterface.FragmentChange
import com.example.floatingwidgets.databinding.ActivityCountdownOrTimerBinding
import com.example.floatingwidgets.myinterface.SetScreen
import com.example.floatingwidgets.myinterface.ViewPager2InputControl
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs

class CountdownOrTimerActivity: AppCompatActivity(), FragmentChange, ViewPager2InputControl, SetScreen {

    private lateinit var binding: ActivityCountdownOrTimerBinding

    private var mIsSmall = false

    private var mLastX = 0f
    private var mLastY = 0f

    private val countdownChooseTimeFragment = CountdownChooseTimeFragment()
    private val countdownChooseMoreTimeFragment = CountdownChooseMoreTimeFragment()
    private val timerFragment = TimerFragment()

    private val countdownContainerFragment = FragmentContainerFragment(this)

    private var initialX = 0f
    private var initialY = 0f

    private val fragmentList = arrayListOf<Fragment>().apply {
//        add(countdownChooseTimeFragment)
//        add(countdownChooseMoreTimeFragment)
        add(countdownContainerFragment)
        add(timerFragment)
    }
    private val titles = arrayListOf("倒计时", "计时")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = window.attributes
        layoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                             WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL

        binding = ActivityCountdownOrTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toSmall()

//        binding.countdownOrTimerActivity.setOnTouchListener { v, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    mLastX = event.getRawX()
//                    mLastY = event.getRawY()
//                    return@setOnTouchListener true
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    val dx = event.getRawX() - mLastX
//                    val dy = event.getRawY() - mLastY
//                    mLastX = event.getRawX()
//                    mLastY = event.getRawY()
//                    if (mIsSmall) {
//                        val lp = window.attributes
//                        lp.x += dx.toInt()
//                        lp.y += dy.toInt()
//                        window.attributes = lp
//                    }
//                }
//                MotionEvent.ACTION_UP -> {
//                    return@setOnTouchListener true
//                }
//            }
//            return@setOnTouchListener false
//        }
//        setIsUserInputEnable(false)

        initViewPager2()

        binding.closeIV.setOnClickListener {

            val countingDownFragment = getFragment(CountingDownFragment::class.java)
            countingDownFragment?.releaseCountdownTimer()

            timerFragment.releaseTimer()
//            val timerFragment = supportFragmentManager.fragments[1] as TimerFragment
//            timerFragment.releaseTimer()

            finish()
        }

    }

    private fun <F : Fragment> getFragment(fragmentClass: Class<F>): F? {
        Log.d("fragments", "${supportFragmentManager.fragments}")
        //supportFragmentManager.fragments为：FragmentContainerFragment，TimerFragment

        val fragmentContainer = supportFragmentManager.fragments.first()

        Log.d("ContainerChild", "${fragmentContainer.childFragmentManager.fragments}")
        // fragmentContainer.childFragmentManager.fragments只有：NavHostFragment

        val navHostFragment = fragmentContainer.childFragmentManager.fragments.first() as NavHostFragment

        Log.d("navHostFragmentChildren", "${navHostFragment.childFragmentManager.fragments}")
        // navHostFragment.childFragmentManager.fragments只有一个，
        // 为当前显示的Fragment（ ChooseTimeFragment，ChooseMoreTimeFragment，CountingDownFragment三选一）

        navHostFragment.childFragmentManager.fragments.forEach {
            if (fragmentClass.isAssignableFrom(it.javaClass)) {
                return it as F
            }
        }

        //根据上述Fragment结构分析，这么写也行：
//        if (fragmentClass.isAssignableFrom(navHostFragment.childFragmentManager.fragments.first().javaClass)) {
//            return navHostFragment.childFragmentManager.fragments.first() as F
//        }

        return null
    }


    private fun toSmall() {
        mIsSmall = true

//        val m = windowManager
//        val d = m.defaultDisplay
        val p = window.attributes.apply {
            width = (resources.displayMetrics.density * 320).toInt()    //320dp转换为px
            Log.d("width", "$width")
            Log.d("width2", "${resources.displayMetrics.widthPixels}")
//            width = WRAP_CONTENT
            height = (resources.displayMetrics.density * 225).toInt()
 //           dimAmount = 0f  //这里好像没用
        }
//        p.height = (d.height * 0.5).toInt()
//        p.width = (d.width * 0.5).toInt()
        window.attributes = p

    }

    private fun toBig() {
        mIsSmall = false

//        val m = windowManager
//        val d = m.defaultDisplay
        val p = window.attributes.apply {
            width = MATCH_PARENT
            height = MATCH_PARENT
            dimAmount = 0f  //这里好像没用
        }
//        p.height = (d.height * 0.5).toInt()
//        p.width = (d.width * 0.5).toInt()
        window.attributes = p
    }


//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                mLastX = event.getRawX()
//                mLastY = event.getRawY()
//                return true
//            }
//            MotionEvent.ACTION_MOVE -> {
//                val dx = event.getRawX() - mLastX
//                val dy = event.getRawY() - mLastY
//                mLastX = event.getRawX()
//                mLastY = event.getRawY()
//                if (mIsSmall) {
//                    val lp = window.attributes
//                    lp.x += dx.toInt()
//                    lp.y += dy.toInt()
//                    window.attributes = lp
//                }
//                return true
//            }
//            MotionEvent.ACTION_UP -> {
//                return true
//            }
//        }
//        return false
//    }

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
                if (disX > disY * 2) {
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

    private fun initViewPager2() {
        //offscreenPageLimit默认不开启预加载
        //offscreenPageLimit设置为1表示缓存前一页，预加载下一页，包含当前页一共三页
        binding.viewPager2.offscreenPageLimit = 1
        binding.viewPager2.adapter = ViewPager2CountdownOrTimerAdapter(this, fragmentList)

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

    override fun onChangeFragment() {
        fragmentList[0] = countdownChooseMoreTimeFragment
        initViewPager2()
    }

    override fun setIsUserInputEnable(isEnable: Boolean) {
        binding.viewPager2.isUserInputEnabled = isEnable
    }

    override fun setScreen() {
        binding.countdownOrTimerTabLayout.visibility = View.INVISIBLE

        toBig()

    }
}