package com.example.e_suratpermintaan

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.example.e_suratpermintaan.di.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(networkModule, dataSourceModule, repoModule, useCaseModule, viewModelModule)
        }
    }
}