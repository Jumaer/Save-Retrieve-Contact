package com.example.myapplication.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract


object SaveContactUtil {


    /**
     * It saves specific information to phone book
     * @param name name of person
     * @param phones multiple phones
     * @param title title of position
     * @param location address of office
     * @param mails multiple mails
     * @param company company name
     * @param contactImg   con image for phone
     */
    fun saveToPhoneBook(
        name: String?,
        phones: List<String?>?, mails: List<String?>?,
        title: String?,
        location: String?,
        company: String?, context: Context?, contactImg: ByteArray?
    ): Int {

        val intent = Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI).apply {
            // Sets the MIME type to match the Contacts Provider
            type = ContactsContract.RawContacts.CONTENT_TYPE
        }

        /*
        * Inserts new data into the Intent. This data is passed to the
        * contacts app's Insert screen
        */

        intent.apply { ->

            // Inserts a Name
            putExtra(ContactsContract.Intents.Insert.NAME, name)
            // Insert a title
            putExtra(ContactsContract.Intents.Insert.JOB_TITLE, title)
            // Insert a company
            putExtra(ContactsContract.Intents.Insert.COMPANY, company)
            // Insert a location
            putExtra(ContactsContract.Intents.Insert.POSTAL, location)


            if ( // phone and mails with image ..
                preparePhonesMailsProfileImage(
                    intent, phones, mails, contactImg
                )
                == 0
            )
                context?.startActivity(intent)


        }

        return 0


    }

    /**
     * take exact params and adding list of values with intent
     * @param phones list of numbers
     * @param mails list of emails
     * @param contactImg is profile image
     */
    private fun preparePhonesMailsProfileImage(
        intent: Intent,
        phones: List<String?>?,
        mails: List<String?>?,
        contactImg: ByteArray?
    ): Int {
        val data = ArrayList<ContentValues>()
        data.addAll(getNumberContents(phones))
        data.addAll(getMailContents(mails))
        data.addAll(getContactImgContent(contactImg))
        intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data)

        return 0
    }

    /**
     * get content values for data content item
     * @param value as String
     * @return [ContentValues] as object of content values
     */
    private fun getContentValuesObject(value: String): ContentValues {
        val row = ContentValues()
        row.put(
            ContactsContract.Contacts.Data.MIMETYPE,
            value
        )
        return row
    }
    /**
     * Adding phones string list to data ..
     * @param phones as list of string
     * @return [ArrayList]---- of contentValues
     */
    private fun getNumberContents(phones: List<String?>?): ArrayList<ContentValues> {
        val data = ArrayList<ContentValues>()

        // Filling data with phone numbers
        phones?.apply {
            for (i in phones.indices) {
                val row =
                    getContentValuesObject(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                phones[i]?.apply {
                    row.put(ContactsContract.CommonDataKinds.Phone.NUMBER, this)
                }
                data.add(row)
            }
        }
        return data
    }

    /**
     * Adding mails string list to data ..
     * @param mails as list of string
     * @return [ArrayList]---- of contentValues
     */
    private fun getMailContents(mails: List<String?>?): ArrayList<ContentValues> {
        val data = ArrayList<ContentValues>()

        // Filling data with mails
        mails?.apply {
            for (i in mails.indices) {
                val row =
                    getContentValuesObject(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                mails[i]?.apply {
                    row.put(ContactsContract.CommonDataKinds.Email.DATA, this)
                }
                data.add(row)
            }
        }
        return data
    }

    /**
     * Adding image byte array to data ..
     * Image of contact will be added (single image)
     * @param contactImg as Byte Array
     * @return [ArrayList]---- of contentValues
     */
    private fun getContactImgContent( contactImg: ByteArray?): ArrayList<ContentValues> {
        val data = ArrayList<ContentValues>()

        contactImg?.apply {
            val row =
                getContentValuesObject(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
            row.put(ContactsContract.CommonDataKinds.Photo.PHOTO, this)
            data.add(row)
        }
        return data
    }

}