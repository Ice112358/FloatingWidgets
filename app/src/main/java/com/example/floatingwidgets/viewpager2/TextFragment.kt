package com.example.floatingwidgets.viewpager2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.floatingwidgets.databinding.FragmentTextBinding

class TextFragment: Fragment() {
    private var param: String? = null

    private var _binding: FragmentTextBinding? = null
    private val binding get() = _binding!!


    companion object {
        private const val ARG_PARAM = "param"

        @JvmStatic
        fun newInstance(param: String) = TextFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM, param)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param = it.getString(ARG_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTextBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.textView.text = "$param \n ${System.currentTimeMillis()}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}