package com.example.domashkareborn

import android.app.Application
import com.example.domashkareborn.di.dataSourceModule
import com.example.domashkareborn.di.navigationModule
import com.example.domashkareborn.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)

            modules(navigationModule, viewModelsModule, dataSourceModule)
        }
    }
}