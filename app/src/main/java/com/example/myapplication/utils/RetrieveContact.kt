package com.example.myapplication.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract

object RetrieveContact {

    data class Contact(
        val id : String ,
        val name : String,
        val phones : List<String>? = null,
        val mails : List<String>? = null,
        val address : String? = null
    )

    @SuppressLint("Range")
    fun getNamePhoneDetails(mContext: Context): MutableList<Contact> {
        val contacts = ArrayList<Contact>()
        val cr = mContext.contentResolver
        val cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            null, null, null)
        if (cur!!.count > 0) {
            while (cur.moveToNext()) {
                val id = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID))
                val name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val email = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                val address = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DATA))
                contacts.add(Contact(id , name, listOf(number), listOf(email),address ))
            }
        }
        return contacts
    }
}