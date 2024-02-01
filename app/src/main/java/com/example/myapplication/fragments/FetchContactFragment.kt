package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.activities.MainActivity
import com.example.myapplication.adapter.ContactsAdapter
import com.example.myapplication.databinding.FragmentFetchContactBinding
import com.example.myapplication.utils.Communicator
import com.example.myapplication.utils.RetrieveContact


class FetchContactFragment : Fragment() {


    private lateinit var binding: FragmentFetchContactBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFetchContactBinding.inflate(inflater, container, false)
        setListeners()
        callLauncher()
        return binding.root
    }



    private fun callLauncher() {
        (activity as MainActivity).requestContactPermissions(object : Communicator{
            override fun onFetch() {
                getData()
            }
        })
    }

    private fun getData() {
        context?.apply {
            val list =  RetrieveContact.getNamePhoneDetails(this)
            showContacts(list)
        }
    }

    private fun showContacts(list: MutableList<RetrieveContact.Contact>) {
        val dataList = ArrayList<RetrieveContact.Contact>()
        list.forEach {
            dataList.add(it)
        }
        context?.apply {
            val conAdapter = ContactsAdapter(this, dataList, object : ContactsAdapter.OnClickItem{
                override fun onShowItem(data: RetrieveContact.Contact) {

                }

            })
            binding.rvAllContacts.apply {
                adapter = conAdapter
                setHasFixedSize(true)
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            fabSwipe.setOnClickListener {
                findNavController().navigate(R.id.action_fetchContactFragment_to_saveContactFragment)
            }
        }
    }


}