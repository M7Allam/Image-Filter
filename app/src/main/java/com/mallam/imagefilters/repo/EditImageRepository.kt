package com.mallam.imagefilters.repo

import android.graphics.Bitmap
import android.net.Uri
import com.mallam.imagefilters.data.ImageFilter

interface EditImageRepository {
    suspend fun getImageFilters(image: Bitmap): List<ImageFilter>
    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?
    suspend fun saveFilteredImage(filteredBitmap: Bitmap): Uri?

}