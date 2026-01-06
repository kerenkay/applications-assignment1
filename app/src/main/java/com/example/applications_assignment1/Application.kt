package com.example.applications_assignment1

import android.app.Application
import com.example.applications_assignment1.utilities.SharedPreferencesManager


class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
    }
}