package com.example.mobilepresence

import android.app.Application
import com.example.mobilepresence.di.appModule
import com.example.mobilepresence.di.networkModule
import com.example.mobilepresence.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(networkModule, appModule, viewModelModule)
        }

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}