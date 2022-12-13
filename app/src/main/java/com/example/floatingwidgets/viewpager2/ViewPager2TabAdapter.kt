package com.example.floatingwidgets.viewpager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPager2TabAdapter(fragmentActivity: FragmentActivity, private val fragmentList: ArrayList<TextFragment>):
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    override fun getItemId(position: Int): Long {
        return fragmentList[position].hashCode().toLong()
    }
}