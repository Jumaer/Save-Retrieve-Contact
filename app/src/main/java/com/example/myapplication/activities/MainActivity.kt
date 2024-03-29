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
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.R


import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.utils.Communicator
import com.example.myapplication.utils.OpenImageDialog
import com.google.android.material.imageview.ShapeableImageView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var imageUri: Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNav()



    }





    private fun setNav() {
        val navGraph: NavGraph
        val navController: NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        navGraph = navController.navInflater.inflate(R.navigation.contact_nav_graph)
        navGraph.apply {
            setStartDestination(R.id.saveContactFragment)
        }
        navController.setGraph(navGraph,intent.extras)
    }



    private fun setImageUri(uri: Uri){
        personImg?.setImageURI(uri)
    }


    //-------------------------------- IMAGE BOTTOM SHEET------------------------------------//

    private var personImg : ShapeableImageView? = null

    fun showImgBs(personImage : ShapeableImageView) {
        personImg = personImage
        OpenImageDialog(this, { onCapture() }, { onFolder() }).show()
    }

    //-------------------------------- COMMON PERMISSIONS------------------------------------//
    companion object {
        private val CAMERA_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

        private val CONTACT_PERMISSIONS = mutableListOf(Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS).toTypedArray()

    }

    private val cameraPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in CAMERA_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                requestCameraPermissions()
            } else {
                // direct navigate to respective screen
                onCameraLaunch()
            }
        }

    private val contactPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in CONTACT_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                requestContactPermissions(listener)
            } else {
                // direct navigate to respective screen
                setResultToRet()
            }
        }

    private fun setResultToRet() {
        listener?.let {
            it.onFetch()
        }
    }


    private fun allPermissionsGranted() = CAMERA_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermissions() {
        cameraPermissionLauncher.launch(CAMERA_PERMISSIONS)
    }

    private var listener : Communicator? = null
    fun requestContactPermissions(obj : Communicator?){
        listener = obj
        contactPermissionLauncher.launch(CONTACT_PERMISSIONS)
    }


    //-------------------------------- CAPTURE IMAGE------------------------------------//


    private var cameraLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri?.let { setImageUri(it) }
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
                requestCameraPermissions()
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
                imageUri?.let { it1 -> setImageUri(it1) }
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