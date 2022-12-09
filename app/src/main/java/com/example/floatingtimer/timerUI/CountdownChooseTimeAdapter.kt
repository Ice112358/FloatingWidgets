package com.example.floatingtimer.timerUI

import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.floatingtimer.databinding.ItemChooseTimeBinding

class CountdownChooseTimeAdapter(val timeAndUnitList: List<String>, val countdownChooseTimeFragment: CountdownChooseTimeFragment):
    RecyclerView.Adapter<CountdownChooseTimeAdapter.ViewHolder>(){

    inner class ViewHolder(binding: ItemChooseTimeBinding): RecyclerView.ViewHolder(binding.root) {
        val textView: TextView = binding.timeAndUnit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChooseTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val viewHolder = ViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val timeItem = timeAndUnitList[position]
            when (timeItem) {
                "30秒" -> {
                    countdownChooseTimeFragment.minutes = 0
                    countdownChooseTimeFragment.seconds = 30
                    countdownChooseTimeFragment.changeToCountingDownFragment()
                }
                "1 分钟" -> {
                    countdownChooseTimeFragment.minutes = 1
                    countdownChooseTimeFragment.seconds = 0
                    countdownChooseTimeFragment.changeToCountingDownFragment()
                }
                "2 分钟" -> {
                    countdownChooseTimeFragment.minutes = 2
                    countdownChooseTimeFragment.seconds = 0
                    countdownChooseTimeFragment.changeToCountingDownFragment()
                }
                "3 分钟" -> {
                    countdownChooseTimeFragment.minutes = 3
                    countdownChooseTimeFragment.seconds = 0
                    countdownChooseTimeFragment.changeToCountingDownFragment()
                }
                "5 分钟" -> {
                    countdownChooseTimeFragment.minutes = 5
                    countdownChooseTimeFragment.seconds = 0
                    countdownChooseTimeFragment.changeToCountingDownFragment()
                }
                "更多" -> {
                    countdownChooseTimeFragment.changeToChooseMoreTimeFragment()
                }
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeAndUnit = timeAndUnitList[position]

        val ass = AbsoluteSizeSpan(12, true)
        val spn = SpannableString(timeAndUnit)
        spn.setSpan(ass, 2, timeAndUnit.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        holder.textView.text = spn

    }

    override fun getItemCount(): Int = timeAndUnitList.size
}