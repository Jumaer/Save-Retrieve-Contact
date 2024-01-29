package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentFetchContactBinding
import com.example.myapplication.databinding.FragmentSaveContactBinding


class FetchContactFragment : Fragment() {

    private lateinit var binding : FragmentFetchContactBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFetchContactBinding.inflate(inflater, container, false)
        return binding.root
    }
}