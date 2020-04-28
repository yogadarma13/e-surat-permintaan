package com.example.e_suratpermintaan

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.e_suratpermintaan.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application(), LifecycleObserver {

    companion object {
        var wasInForeground = false
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                preferenceModule,
                networkModule,
                dataSourceModule,
                repoModule,
                useCaseModule,
                viewModelModule,
                providerModule
            )
        }

        val appContext=this;
        ProcessLifecycleOwner.get().lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        // app moved to foreground
        wasInForeground = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        // app moved to background
        wasInForeground = false
    }
}
