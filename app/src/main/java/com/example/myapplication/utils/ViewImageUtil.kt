package com.example.myapplication.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import java.io.ByteArrayOutputStream


private fun bitmapToByteArray(bitmap: Bitmap?): ByteArray? {
    if(bitmap == null) return null

    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
    return stream.toByteArray()
}


fun getByteArray(view: View?): ByteArray? {
    if(view == null) return null
    val specSize = View.MeasureSpec.makeMeasureSpec(
        0 /* any */, View.MeasureSpec.UNSPECIFIED
    )
    view.measure(specSize, specSize)
    val bitmap = Bitmap.createBitmap(
        view.measuredWidth,
        view.measuredHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    view.layout(view.left, view.top, view.right, view.bottom)
    view.draw(canvas)
    return bitmapToByteArray(bitmap)
}