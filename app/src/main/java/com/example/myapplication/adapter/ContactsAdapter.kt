package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.LayoutContactItemBinding
import com.example.myapplication.utils.RetrieveContact

class ContactsAdapter(
private val context: Context,
private val dataList: List<RetrieveContact.Contact>,
private val listener : OnClickItem

) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutContactItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        holder.binding.apply {
            txtPersonName.text = data.name
            data.phones?.apply {
                txtPersonNumber.text = this[0]
            }
            data.image?.apply {
                imgPerson.setImageURI(this)
            }
            this.root.setOnClickListener {
                listener.onShowItem(data)
            }
        }
    }

    inner class ViewHolder(val binding: LayoutContactItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnClickItem{
        fun onShowItem(data : RetrieveContact.Contact)
    }


}