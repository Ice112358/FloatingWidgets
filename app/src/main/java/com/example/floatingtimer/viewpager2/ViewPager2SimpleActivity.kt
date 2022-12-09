package com.example.floatingtimer.viewpager2

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEachIndexed
import com.example.floatingtimer.databinding.ActivityViewPager2SimpleBinding
import androidx.viewpager2.widget.ViewPager2
import com.example.floatingtimer.R

class ViewPager2SimpleActivity: AppCompatActivity() {
    private lateinit var binding: ActivityViewPager2SimpleBinding

    //图片资源
    private val mImgIds = intArrayOf(
        R.drawable.a,
        R.drawable.b,
        R.drawable.c,
        R.drawable.d,
        R.drawable.e
    )

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ViewPager2SimpleActivity::class.java).apply {
                putExtra("title", "ViewPager2基本使用")
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPager2SimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewPager2()
    }

    private fun initViewPager2() {
        val width = dp2px(10)

        for (i in mImgIds.indices) {
            val dot = View(this)
            dot.setBackgroundResource(R.drawable.dot_selector)
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams(width, width))
            if (i != 0) {
                layoutParams.leftMargin = width
            } else {
                dot.isSelected = true
            }
            binding.indicator.addView(dot, layoutParams)
        }

        binding.viewPager2.adapter = ViewPager2Adapter(this, mImgIds)

        //设置监听
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                binding.indicator.forEachIndexed { index, view ->
                    view.isSelected = index == position
                }
            }
        })
    }

    fun dp2px(dpValue: Int): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5F).toInt()
    }


}