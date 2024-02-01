package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.activities.MainActivity
import com.example.myapplication.adapter.ContactsAdapter
import com.example.myapplication.databinding.FragmentFetchContactBinding
import com.example.myapplication.utils.Communicator
import com.example.myapplication.utils.RetrieveContact
import com.example.myapplication.utils.RetrieveContact.DATA_CONTACT


class FetchContactFragment : Fragment() {


    private lateinit var binding: FragmentFetchContactBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFetchContactBinding.inflate(inflater, container, false)
        setObserver()
        setListeners()
        callLauncher()
        return binding.root
    }

    private fun setObserver() {
        RetrieveContact.contactsLiveData.observe(viewLifecycleOwner) {
            it?.apply {
                showContacts(this as MutableList<RetrieveContact.Contact>)
            }
        }
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
            RetrieveContact.fetchContacts(lifecycleScope,this)
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
                       showItem(data)
                }

            })
            binding.rvAllContacts.apply {
                adapter = conAdapter
                setHasFixedSize(true)
            }
        }
    }

    private fun showItem(data: RetrieveContact.Contact) {
      val action = R.id.action_fetchContactFragment_to_displayContactFragment
      findNavController().navigate(action, bundleOf(DATA_CONTACT to data))
    }

    private fun setListeners() {
        binding.apply {
            fabSwipe.setOnClickListener {
                findNavController().navigate(R.id.action_fetchContactFragment_to_saveContactFragment)
            }
        }
    }


}