package com.example.jsonparsertesttask2

import android.graphics.*
import com.squareup.picasso.Transformation

class CroppedBitmapTransformation : Transformation {
    override fun transform(bitmap: Bitmap): Bitmap {
        val croppedBitmap = Bitmap
            .createBitmap(bitmap, 1, 1, bitmap.width - 2, bitmap.height - 2)
        if (croppedBitmap != bitmap) {
            bitmap.recycle()
        }
        return croppedBitmap
    }

    override fun key(): String {
        return HotelConstants.HOTEL_CONSTANTS_CROPPED_BITMAP_KEY
    }
}