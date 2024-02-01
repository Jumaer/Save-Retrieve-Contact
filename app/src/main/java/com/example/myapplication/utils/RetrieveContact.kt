package com.example.myapplication.utils

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


object RetrieveContact {

    const val DATA_CONTACT = "DATA_CONTACT"


    @Keep
    data class Contact(
        var id : String ,
        var name : String,
        var phones : List<String>? = null,
        var mails : List<String>? = null,
        var title : String? = null,
        var image : Uri? = null,
        var company : String? = null
    ) : Serializable

    @Keep
    data class ContactView(
        val title : String,
        val value : String,
    ): Serializable



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


    private fun getContactEmails(mContext: Context): HashMap<String, ArrayList<String>> {
        val contactsEmailMap = HashMap<String, ArrayList<String>>()
        val emailCursor = mContext.contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            null,
            null,
            null)
        if (emailCursor != null && emailCursor.count > 0) {
            val contactIdIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID)
            val emailIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
            while (emailCursor.moveToNext()) {
                val contactId = emailCursor.getString(contactIdIndex)
                val email = emailCursor.getString(emailIndex)
                //check if the map contains key or not, if not then create a new array list with email
                if (contactsEmailMap.containsKey(contactId)) {
                    contactsEmailMap[contactId]?.add(email)
                } else {
                    contactsEmailMap[contactId] = arrayListOf(email)
                }
            }
            //contact contains all the emails of a particular contact
            emailCursor.close()
        }
        return contactsEmailMap
    }


    private fun getContactNumbers(mContext: Context): HashMap<String, ArrayList<String>> {
        val contactsNumberMap = HashMap<String, ArrayList<String>>()
        val phoneCursor: Cursor? = mContext.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (phoneCursor != null && phoneCursor.count > 0) {
            val contactIdIndex = phoneCursor!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val numberIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (phoneCursor.moveToNext()) {
                val contactId = phoneCursor.getString(contactIdIndex)
                val number: String = phoneCursor.getString(numberIndex)
                //check if the map contains key or not, if not then create a new array list with number
                if (contactsNumberMap.containsKey(contactId)) {
                    contactsNumberMap[contactId]?.add(number)
                } else {
                    contactsNumberMap[contactId] = arrayListOf(number)
                }
            }
            //contact contains all the number of a particular contact
            phoneCursor.close()
        }
        return contactsNumberMap
    }


    private fun getPhoneContacts(mContext: Context): ArrayList<Contact> {
        val contactsList = ArrayList<Contact>()
        val contactsCursor = mContext.contentResolver?.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
        if (contactsCursor != null && contactsCursor.count > 0) {
            val idIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts._ID)
            val nameIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            while (contactsCursor.moveToNext()) {
                val id = contactsCursor.getString(idIndex)
                val name = contactsCursor.getString(nameIndex)
                if (name != null) {
                    contactsList.add(Contact(id, name, title = null, company = null))
                }
            }
            contactsCursor.close()
        }
        return contactsList
    }

    private val _contactsLiveData: MutableLiveData<List<Contact>> =
        MutableLiveData()
    val contactsLiveData: LiveData<List<Contact>> get() = _contactsLiveData
    fun fetchContacts(scope : CoroutineScope,mContext: Context) {
        scope.launch {
            val contactsListAsync = async { getPhoneContacts(mContext) }
            val contactNumbersAsync = async { getContactNumbers(mContext) }
            val contactEmailAsync = async { getContactEmails(mContext) }


            val contacts = contactsListAsync.await()
            val contactNumbers = contactNumbersAsync.await()
            val contactEmails = contactEmailAsync.await()

            contacts.forEach {
                contactNumbers[it.id]?.let { numbers ->
                    it.phones = numbers
                }
                contactEmails[it.id]?.let { emails ->
                    it.mails = emails
                }
                it.image = getPhotoUri(mContext,it.id)
            }
            _contactsLiveData.postValue(contacts)
        }
    }
}