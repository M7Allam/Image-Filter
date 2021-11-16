package com.mallam.imagefilters.di

import com.mallam.imagefilters.viewmodel.EditImageViewModel
import com.mallam.imagefilters.viewmodel.SavedImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
    viewModel { SavedImagesViewModel(savedImagesRepository = get()) }
}