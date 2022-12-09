package com.example.floatingtimer.viewpager2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.floatingtimer.databinding.ActivityViewPager2TabBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPager2TabActivity: AppCompatActivity() {
    private lateinit var binding: ActivityViewPager2TabBinding

    private val titles = arrayListOf("one", "two", "three", "four", "five", "six", "seven", "eight")
    private val fragmentList = arrayListOf<TextFragment>().apply {
        titles.forEachIndexed { index, s ->
            add(TextFragment.newInstance(s))
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ViewPager2TabActivity::class.java).apply {
                putExtra("key_title", "ViewPager2 + TabLayout + Fragment")
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPager2TabBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewPager2()
    }

    private fun initViewPager2() {
        //offscreenPageLimit默认不开启预加载
        //offscreenPageLimit设置为1表示缓存前一页，预加载下一页，包含当前页一共三页
        binding.viewPager2.offscreenPageLimit = 1
        binding.viewPager2.adapter = ViewPager2TabAdapter(this, fragmentList)
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager2,
            object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    tab.text = titles[position]
                }
            }
        ).attach()
    }
}