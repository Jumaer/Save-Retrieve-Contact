package com.example.myapplication.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import java.io.ByteArrayOutputStream

/**
 * This method can convert bitmap to byte array
 * @param bitmap is that bitmap that need to be converted
 * @return [ByteArray] is the result ..
 */
private fun bitmapToByteArray(bitmap: Bitmap?): ByteArray? {
    if(bitmap == null) return null

    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
    return stream.toByteArray()
}

/**
 * This method will help to get byte array directly from image view
 * Because image can be uri, url, file , bitmap ,
 * so if any thing is present to view then image will be load and send
 * @param view is any [View] ... here shapable image view will perform better
 * @return [ByteArray]
 */
fun getByteArray(view: View): ByteArray?{
    val bitmap =
        Bitmap.createBitmap(view.layoutParams.width, view.layoutParams.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.layout(view.left, view.top, view.right, view.bottom)
    view.draw(canvas)
    return bitmapToByteArray(bitmap)
}