package com.example.floatingtimer.timerUI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.floatingtimer.R
import com.example.floatingtimer.myinterface.FragmentChange
import com.example.floatingtimer.databinding.FragmentCountdownChooseTimeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CountdownChooseTimeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CountdownChooseTimeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentCountdownChooseTimeBinding? = null
    private val binding get() = _binding!!

    private val timeAndUnitList = listOf("30秒", "1 分钟", "2 分钟", "3 分钟", "5 分钟", "更多")
    var minutes = 0L
    var seconds = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCountdownChooseTimeBinding.inflate(inflater, container, false)

        binding.chooseTimeRecyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.chooseTimeRecyclerView.adapter = CountdownChooseTimeAdapter(timeAndUnitList, this)

        return binding.root
    }

    fun changeToChooseMoreTimeFragment() {
        findNavController().navigate(R.id.action_countdownChooseTimeFragment_to_countdownChooseMoreTimeFragment)
    }

    fun changeToCountingDownFragment() {
        val bundle = Bundle().apply {
            putLong("minutes", minutes)
            putLong("seconds", seconds)
        }
        findNavController().navigate(R.id.action_countdownChooseTimeFragment_to_countingDownFragment2, bundle)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CountdownChooseTimeFragment.
         */
        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            CountdownChooseTimeFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}