package com.akash.wardrobe

import android.app.Application
import com.akash.wardrobe.injection.appModule
import org.koin.android.ext.android.startKoin

class AppClass : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }
}