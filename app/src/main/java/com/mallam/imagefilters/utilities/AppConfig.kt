package com.mallam.imagefilters.utilities

import android.app.Application
import com.mallam.imagefilters.di.repositoryModule
import com.mallam.imagefilters.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class AppConfig: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppConfig)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }
}