package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFetchContactBinding


class FetchContactFragment : Fragment() {


    private lateinit var binding: FragmentFetchContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFetchContactBinding.inflate(inflater, container, false)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.apply {
            fabSwipe.setOnClickListener {
                findNavController().navigate(R.id.action_fetchContactFragment_to_saveContactFragment)
            }
        }
    }


}