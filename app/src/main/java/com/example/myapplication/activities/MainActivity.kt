package com.example.myapplication.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat


import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.utils.OpenImageDialog
import com.example.myapplication.utils.SaveContactUtil
import com.example.myapplication.utils.getByteArray
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setListeners()
    }


    private fun setListeners() {
        binding.apply {
            fabEdit.setOnClickListener {
                showImgBs()
            }
            btnSave.setOnClickListener {
                saveToPhonebook()
            }
        }
    }

    private fun saveToPhonebook() {
        binding.apply {

            val mails = mutableListOf(getData(etEmail1), getData(etEmail2), getData(etEmail3))
            val phones = mutableListOf(getData(etPhone), getData(etPhone2), getData(etPhone3))

            SaveContactUtil.saveToPhoneBook(
                this@MainActivity,
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

    private fun getData(layout: TextInputLayout): String {
        return layout.editText?.text.toString().trim()

    }

    //-------------------------------- IMAGE BOTTOM SHEET------------------------------------//


    private fun showImgBs() {
        OpenImageDialog(this, { onCapture() }, { onFolder() }).show()
    }

    //-------------------------------- COMMON PERMISSIONS------------------------------------//
    companion object {
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

    }

    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                requestPermissions()
            } else {
                // direct navigate to respective screen
                onCameraLaunch()
            }
        }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        permissionLauncher.launch(REQUIRED_PERMISSIONS)
    }


    //-------------------------------- CAPTURE IMAGE------------------------------------//


    private var cameraLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                binding.imgPerson.setImageURI(imageUri)
            }
        }

    @SuppressLint("ObsoleteSdkInt")
    private fun onCapture() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // direct navigate to respective screen
            onCameraLaunch()

        } else {
            if (allPermissionsGranted()) onCameraLaunch()
            else {
                requestPermissions()
            }
        }

    }


    private fun onCameraLaunch() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraLauncher.launch(cameraIntent)
    }


    //-------------------------------- GALLERY IMAGE------------------------------------//

    private val galleryLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
            if (it.resultCode == RESULT_OK) {
                imageUri = it.data?.data
                binding.imgPerson.setImageURI(imageUri)
            }
        }
    )

    private fun onFolder() {
        galleryLauncher.launch(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        )
    }


}