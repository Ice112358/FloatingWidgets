package com.example.floatingwidgets.timerUI

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.floatingwidgets.databinding.ItemMinuteSecondBinding

class CountdownChooseMoreTimeAdapter(val minuteOrSecondList: List<String>):
    RecyclerView.Adapter<CountdownChooseMoreTimeAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemMinuteSecondBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val minuteOrSecond: TextView = binding.minuteOrSecond
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMinuteSecondBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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