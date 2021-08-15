package com.example.websocketproject

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.websocketproject.databinding.FragmentSecondBinding


class SecondFragment : Fragment(R.layout.fragment_second) {
    lateinit var binding: FragmentSecondBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val color = requireArguments().getString("color")

        binding = FragmentSecondBinding.inflate(inflater,container, false)
        binding.root.setBackgroundColor(Color.parseColor(color))
        return binding.root
    }

}