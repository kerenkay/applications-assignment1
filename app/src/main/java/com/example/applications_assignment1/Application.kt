package com.example.applications_assignment1

import android.app.Application
import com.example.applications_assignment1.utilities.SharedPreferencesManager
import com.example.applications_assignment1.utilities.SoundEffectPlayer
import com.example.applications_assignment1.utilities.VibrationManager


class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
        VibrationManager.init(this)
    }
}