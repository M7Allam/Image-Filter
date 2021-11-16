package com.mallam.imagefilters.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mallam.imagefilters.repo.SavedImagesRepository
import com.mallam.imagefilters.utilities.Coroutines
import java.io.File

class SavedImagesViewModel(private val savedImagesRepository: SavedImagesRepository) : ViewModel() {

    private val savedImageDataState = MutableLiveData<SavedImageDataState>()
    val savedImageUiState :LiveData<SavedImageDataState> get() = savedImageDataState

    fun loadSavedImages(){
        Coroutines.io {
            runCatching {
                emitSavedImagesUiState(isLoading = true)
                savedImagesRepository.loadSavedImages()
            }.onSuccess {  savedImages ->
                if(savedImages.isNullOrEmpty()){
                    emitSavedImagesUiState(error = "No image found")
                }else{
                    emitSavedImagesUiState(savedImages = savedImages)
                }
            }.onFailure {
                emitSavedImagesUiState(error = it.message.toString())
            }
        }
    }

    private fun emitSavedImagesUiState(
        isLoading: Boolean = false,
        savedImages: List<Pair<File, Bitmap>>? = null,
        error: String? = null
    ){
        val dataState = SavedImageDataState(isLoading, savedImages, error)
        savedImageDataState.postValue(dataState)
    }

    data class SavedImageDataState(
        val isLoading :Boolean,
        val savedImages : List<Pair<File, Bitmap>>?,
        val error : String?

    )
}