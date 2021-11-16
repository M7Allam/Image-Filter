package com.mallam.imagefilters.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mallam.imagefilters.data.ImageFilter
import com.mallam.imagefilters.repo.EditImageRepository
import com.mallam.imagefilters.utilities.Coroutines

class EditImageViewModel(private val editImageRepository: EditImageRepository) : ViewModel() {

    //region:: Prepare image preview
    private val imagePreviewDataState = MutableLiveData<ImagePreviewDataState>()
    val imagePreviewUiState: LiveData<ImagePreviewDataState> get() = imagePreviewDataState
    fun prepareImageView(imageUri: Uri){
        Coroutines.io {
            runCatching {
                emitImagePreviewUiState(isLoading = true)
                editImageRepository.prepareImagePreview(imageUri)
            }.onSuccess { bitmap ->
                if(bitmap != null){
                    emitImagePreviewUiState(bitmap = bitmap)
                }else{
                    emitImagePreviewUiState(error = "Unable to prepare image preview")
                }
            }.onFailure {
                emitImagePreviewUiState(error = it.message.toString())
            }
        }
    }

    private fun emitImagePreviewUiState(
        isLoading: Boolean = false,
        bitmap: Bitmap? = null,
        error: String? = null
    ) {
        val dataState = ImagePreviewDataState(isLoading, bitmap, error)
        imagePreviewDataState.postValue(dataState)
    }

    data class ImagePreviewDataState(
        val isLoading: Boolean,
        val bitmap: Bitmap?,
        val error: String?
    )
    //endregion

    //region:: Load image filter
    private val imageFiltersDataState = MutableLiveData<ImageFiltersDataState>()
    val imageFiltersUIState :LiveData<ImageFiltersDataState> get() = imageFiltersDataState

    fun loadImageFilter(originalImage :Bitmap){
        Coroutines.io {
            runCatching {
                emitImageFiltersUiState(isLoading = true)
                editImageRepository.getImageFilters(getPreviewImage(originalImage))
            }.onSuccess { imageFilters ->
                emitImageFiltersUiState(imageFilter = imageFilters)
            }.onFailure {
                emitImageFiltersUiState(error = it.message.toString())
            }
        }
    }

    private fun getPreviewImage(originalImage :Bitmap) :Bitmap {
        return runCatching {
            val previewWidth = 150
            val previewHeight = originalImage.height * previewWidth / originalImage.width
            Bitmap.createScaledBitmap(originalImage, previewWidth, previewHeight, false)
        }.getOrDefault(originalImage)
    }

    private fun emitImageFiltersUiState(
        isLoading: Boolean = false,
        imageFilter: List<ImageFilter>? =null,
        error: String? =null
    ){
        val dataState = ImageFiltersDataState(isLoading, imageFilter, error)
        imageFiltersDataState.postValue(dataState)
    }

    data class ImageFiltersDataState(
        val isLoading: Boolean,
        val imageFilter: List<ImageFilter>?,
        val error: String?
    )
    //endregion

    //region:: Save filtered image
    private val saveFilteredImageDataState = MutableLiveData<SaveFilteredImageDataState>()
    val saveFilteredImageUIState :LiveData<SaveFilteredImageDataState> get() = saveFilteredImageDataState

    fun saveFilteredImage(filteredBitmap: Bitmap){
        Coroutines.io {
            runCatching {
                emitSaveFilteredImageUiState(isLoading = true)
                editImageRepository.saveFilteredImage(filteredBitmap)
            }.onSuccess {  saveImageUri ->
                emitSaveFilteredImageUiState(uri = saveImageUri)
            }.onFailure {
                emitSaveFilteredImageUiState(error = it.message.toString())
            }
        }
    }

    private fun emitSaveFilteredImageUiState(
        isLoading: Boolean = false,
        uri: Uri? = null,
        error: String? = null
    ){
        val dataState = SaveFilteredImageDataState(isLoading, uri, error)
        saveFilteredImageDataState.postValue(dataState)
    }

    data class SaveFilteredImageDataState(
        val isLoading: Boolean,
        val uri: Uri?,
        val error: String?
    )
    //endregion

}