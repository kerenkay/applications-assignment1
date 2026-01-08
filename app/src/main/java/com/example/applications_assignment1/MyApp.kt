package com.example.applications_assignment1

import android.app.Application
import com.example.applications_assignment1.data.storage.SharedPreferencesManager
import com.example.applications_assignment1.system.media.VibrationManager


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
        VibrationManager.init(this)
    }
}