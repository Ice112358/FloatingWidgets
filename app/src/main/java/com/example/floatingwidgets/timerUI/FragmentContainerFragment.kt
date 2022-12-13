package com.example.floatingwidgets.timerUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.floatingwidgets.databinding.FragmentFragmentContainerBinding
import com.example.floatingwidgets.myinterface.SetScreen

class FragmentContainerFragment(val setScreen: SetScreen): Fragment() {
    private var _binding: FragmentFragmentContainerBinding? = null
    private val binding get() = _binding!!

    private var countingDownFragment: CountingDownFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFragmentContainerBinding.inflate(inflater, container, false)

        countingDownFragment = getFragment(CountingDownFragment::class.java)
        countingDownFragment?.setScreen = setScreen

        return binding.root
    }

    private fun <F : Fragment> getFragment(fragmentClass: Class<F>): F? {

        childFragmentManager.fragments.first().childFragmentManager.fragments.forEach {
            if (fragmentClass.isAssignableFrom(it.javaClass)) {
                return it as F
            }
        }

        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}