package com.example.lab2

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.lab2.databinding.FragmentDrawBinding

class DrawFragment : Fragment() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentDrawBinding.inflate(inflater)

        val viewModel : SimpleViewModel by activityViewModels()
        viewModel.bitmap.observe(viewLifecycleOwner){
            binding.customView.passBitmap(it)
        }

        binding.customView.setOnTouchListener { view: View, event: MotionEvent ->
            binding.customView.drawCircle(viewModel.color.value!!)
            true
        }
        return binding.root

    }
}