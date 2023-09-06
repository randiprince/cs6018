package com.example.lab2

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.lab2.databinding.FragmentClickBinding

class ClickFragment : Fragment() {
    private var buttonFunction : () -> Unit = {}

    fun setButtonFunction(newFunc: () -> Unit) {
        buttonFunction = newFunc
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentClickBinding.inflate(inflater, container, false)
        val viewModel : SimpleViewModel by activityViewModels()

        binding.clickMe.setOnClickListener {
//            viewModel.pickColor()
            buttonFunction()
        }

        return binding.root
    }
}