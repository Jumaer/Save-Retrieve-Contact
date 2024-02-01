package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.LayoutContactInfoItemBinding
import com.example.myapplication.utils.RetrieveContact


class DisplayContactAdapter (
    private val context: Context,
    private val dataList: List<RetrieveContact.ContactView>,


    ) : RecyclerView.Adapter<DisplayContactAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutContactInfoItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        holder.binding.apply {
            txtTitle.text = data.title
            txtValue.text = data.value
        }
    }

    inner class ViewHolder(val binding: LayoutContactInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root)



}