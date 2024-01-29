package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.activities.MainActivity
import com.example.myapplication.databinding.FragmentSaveContactBinding
import com.example.myapplication.utils.SaveContactUtil
import com.example.myapplication.utils.getByteArray
import com.google.android.material.textfield.TextInputLayout

class SaveContactFragment : Fragment() {

    private lateinit var binding : FragmentSaveContactBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveContactBinding.inflate(inflater, container, false)
        setListeners()
        return binding.root
    }


    private fun saveToPhonebook() {
        binding.apply {

            val mails = mutableListOf(getData(etEmail1), getData(etEmail2), getData(etEmail3))
            val phones = mutableListOf(getData(etPhone), getData(etPhone2), getData(etPhone3))

            SaveContactUtil.saveToPhoneBook(
                context,
                getData(etName),
                phones,
                mails,
                getData(etTitle),
                getData(etLocation),
                getData(etCompany),
                getByteArray(imgPerson)
            )

        }
    }

    private fun setListeners() {
        binding.apply {
            fabEdit.setOnClickListener {
                (activity as MainActivity).showImgBs(binding.imgPerson)
            }
            btnSave.setOnClickListener {
                saveToPhonebook()
            }
            fabSwipe.setOnClickListener {
                findNavController().navigate(R.id.action_saveContactFragment_to_fetchContactFragment)
            }
        }
    }



    private fun getData(layout: TextInputLayout): String {
        return layout.editText?.text.toString().trim()

    }


}