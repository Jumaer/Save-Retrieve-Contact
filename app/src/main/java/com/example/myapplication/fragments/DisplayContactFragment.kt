package com.example.myapplication.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.adapter.DisplayContactAdapter
import com.example.myapplication.databinding.FragmentDisplayContactBinding
import com.example.myapplication.utils.BundleUtils
import com.example.myapplication.utils.RetrieveContact
import com.example.myapplication.utils.RetrieveContact.DATA_CONTACT

class DisplayContactFragment : Fragment() {
    private lateinit var binding: FragmentDisplayContactBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisplayContactBinding.inflate(inflater, container, false)
        getArg()

        return binding.root
    }

    private fun getArg() {
        arguments?.let { it ->
            BundleUtils.getSerializable(it,DATA_CONTACT, RetrieveContact.Contact::class.java)?.apply {
                val dataList = mutableListOf<RetrieveContact.ContactView>()
                dataList.add(RetrieveContact.ContactView("Name",name))
                var counter = 0
                phones?.forEach {
                    counter++
                    dataList.add(RetrieveContact.ContactView("Phone $counter",it))
                }
                counter = 0
                mails?.forEach {
                    counter++
                    dataList.add(RetrieveContact.ContactView("Mail $counter",it))
                }
                setImage(image)
                setAdapter(dataList)
            }
        }
    }

    private fun setImage(image: Uri?) {
        if(image != null)
            binding.imgPerson.setImageURI(image)

    }

    private fun setAdapter(dataList: MutableList<RetrieveContact.ContactView>) {
        context?.let {
            val adapterItem = DisplayContactAdapter(it,dataList)
            binding.rvAllInformation.apply {
                adapter = adapterItem
                setHasFixedSize(true)
            }

        }


    }


}