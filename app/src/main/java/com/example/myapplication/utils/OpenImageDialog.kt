package com.example.myapplication.utils

import android.content.Context
import android.view.LayoutInflater
import com.example.myapplication.R
import com.example.myapplication.databinding.LayoutCapturePhotoBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class OpenImageDialog(context: Context, onCapture: () -> Unit,onFolder: () -> Unit) {


    private var binding : LayoutCapturePhotoBinding
    private var bottomSheet: BottomSheetDialog

    init {

        binding =
            LayoutCapturePhotoBinding.inflate(
                LayoutInflater.from(context),
                null,
                false
            ).apply {

                imgClickCamera.setOnClickListener {
                    onCapture()
                }
                imgFileTake.setOnClickListener {
                    onFolder()
                }

            }
        bottomSheet = BottomSheetDialog(context, R.style.BottomSheetDialog).apply {
            setContentView(binding.root)
        }
    }

    fun show() {
        if (!bottomSheet.isShowing) bottomSheet.show()
    }

    fun dismiss() {
        if (bottomSheet.isShowing) bottomSheet.dismiss()
    }

}