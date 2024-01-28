package com.example.myapplication.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts


import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.utils.OpenImageDialog

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.bind(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.fabEdit.setOnClickListener {
            OpenImageDialog(this, { onCapture() }, { onFolder() })
        }
    }

    private val galleryLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
            if (it.resultCode == RESULT_OK) {
                val uri = it.data?.data

            }
        }
    )
    private fun onFolder() {
        galleryLauncher.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
    }


    private var cameraLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
            if (it.resultCode == RESULT_OK) {
                val uri = it.data

            }
        }
    )
    private fun onCapture(){
          cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }


}