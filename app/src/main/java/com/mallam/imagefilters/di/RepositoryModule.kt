package com.mallam.imagefilters.di

import com.mallam.imagefilters.repo.EditImageRepository
import com.mallam.imagefilters.repo.EditImageRepositoryImpl
import com.mallam.imagefilters.repo.SavedImagesRepository
import com.mallam.imagefilters.repo.SavedImagesRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<EditImageRepository> { EditImageRepositoryImpl(androidContext()) }
    factory<SavedImagesRepository> { SavedImagesRepositoryImpl(androidContext()) }
}