package com.example.floatingwidgets.timerUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.floatingwidgets.R
import com.example.floatingwidgets.databinding.FragmentCountdownChooseMoreTimeBinding
import java.util.*
import kotlin.collections.ArrayList

class CountdownChooseMoreTimeFragment: Fragment() {

    private var _binding: FragmentCountdownChooseMoreTimeBinding? = null
    private val binding get() = _binding!!

    private val minuteList = ArrayList<String>().apply {
        for (i in 0 until 13)   add("$i")
    }

    private val secondList = ArrayList<String>().apply {
        for (i in 0 until 60)   add("$i")
    }

    var minutes = 0L
    var seconds = 0L


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCountdownChooseMoreTimeBinding.inflate(inflater, container, false)

        binding.timePickerView.minutePV.setData(minuteList)
        binding.timePickerView.minutePV.setOnSelectListener(object : OnSelectListener {
            override fun onSelect(text: String) {
  //              Toast.makeText(context, "选择了${text}分", Toast.LENGTH_SHORT).show()
                minutes = text.toLong()
            }
        })
        binding.timePickerView.minutePV.setSelected(0)

        binding.timePickerView.secondPV.setData(secondList)
        binding.timePickerView.secondPV.setOnSelectListener(object : OnSelectListener {
            override fun onSelect(text: String) {
    //            Toast.makeText(context, "选择了${text}秒", Toast.LENGTH_SHORT).show()
                seconds = text.toLong()
            }
        })
        binding.timePickerView.secondPV.setSelected(0)

        binding.startBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putLong("minutes", minutes)
                putLong("seconds", seconds)
            }
            findNavController().navigate(R.id.action_countdownChooseMoreTimeFragment_to_countingDownFragment2, bundle)
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_countdownChooseMoreTimeFragment_to_countdownChooseTimeFragment)
        }

//        val minuteLayoutManager = LooperLayoutManager(context)
//        val minuteAdapter =  CountdownChooseMoreTimeAdapter(minuteList)
//        binding.recyclerViewMinute.layoutManager = minuteLayoutManager
//        binding.recyclerViewMinute.adapter = minuteAdapter
//        val minuteSnapHelper = MyPagerSnapHelper()
//        minuteSnapHelper.attachToRecyclerView(binding.recyclerViewMinute)
//
//        val secondLayoutManager = LooperLayoutManager(context)
//        val secondAdapter = CountdownChooseMoreTimeAdapter(secondList)
//        binding.recyclerViewSecond.layoutManager = secondLayoutManager
//        binding.recyclerViewSecond.adapter = secondAdapter
//        val secondSnapHelper = MyLinearSnapHelper()
//        secondSnapHelper.attachToRecyclerView(binding.recyclerViewSecond)

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}