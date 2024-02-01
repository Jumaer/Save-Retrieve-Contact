package com.example.myapplication.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract


object RetrieveContact {

    data class Contact(
        val id : String ,
        val name : String,
        val phones : List<String>? = null,
        val mails : List<String>? = null,
        val address : String? = null,
        val image : Uri? = null
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
                val image = getPhotoUri(mContext,id)
                contacts.add(Contact(id , name, listOf(number), listOf(email),address,image ))
            }
        }
        return contacts
    }

    /**
     * @return the photo URI
     */
    private fun getPhotoUri(mContext: Context, id: String): Uri? {
        try {
            val cur: Cursor? = mContext.contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                null,
                ContactsContract.Data.CONTACT_ID + "=" + id + " AND "
                        + ContactsContract.Data.MIMETYPE + "='"
                        + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
                null
            )
            if (cur != null) {
                if (!cur.moveToFirst()) {
                    return null // no photo
                }
            } else {
                return null // error in cursor process
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        val person =
            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id.toLong())
        return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
    }
}