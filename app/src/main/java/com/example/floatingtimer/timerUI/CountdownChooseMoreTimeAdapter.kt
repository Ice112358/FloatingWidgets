package com.example.floatingtimer.timerUI

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.floatingtimer.databinding.MinuteSecondItemBinding

class CountdownChooseMoreTimeAdapter(val minuteOrSecondList: List<String>):
    RecyclerView.Adapter<CountdownChooseMoreTimeAdapter.ViewHolder>() {

    inner class ViewHolder(binding: MinuteSecondItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val minuteOrSecond: TextView = binding.minuteOrSecond
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MinuteSecondItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val minuteOrSecond = minuteOrSecondList[position]
        holder.minuteOrSecond.text = minuteOrSecond
        //Log.d("ChooseMoreTimeAdapter", "$position")
    }

    override fun getItemCount() = minuteOrSecondList.size

}