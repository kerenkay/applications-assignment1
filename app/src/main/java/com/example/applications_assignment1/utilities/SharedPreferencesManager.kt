package com.example.applications_assignment1.utilities

import android.content.Context
import com.example.applications_assignment1.ScoreEntry

class SharedPreferencesManager private constructor(context: Context) {

    private val prefs = context.getSharedPreferences(
        "game_settings",
        Context.MODE_PRIVATE
    )

    private val gson = com.google.gson.Gson()

    companion object {
        @Volatile
        private var instance: SharedPreferencesManager? = null

        fun init(context: Context) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = SharedPreferencesManager(context.applicationContext)
                    }
                }
            }
        }

        fun getInstance(): SharedPreferencesManager {
            return instance
                ?: throw IllegalStateException("SharedPreferencesManager not initialized")
        }
    }


    //store api key
    private val KEY_SCORES = "SCORES_LIST"

    fun saveScores(list: List<ScoreEntry>) {
        prefs.edit()
            .putString(KEY_SCORES, gson.toJson(list))
            .apply()
    }

    fun loadScores(): MutableList<ScoreEntry> {
        val json = prefs.getString(KEY_SCORES, null) ?: return mutableListOf()

        val type = object :
            com.google.gson.reflect.TypeToken<MutableList<ScoreEntry>>() {}.type

        return runCatching {
            gson.fromJson<MutableList<ScoreEntry>>(json, type)
        }.getOrElse {
            mutableListOf()
        }
    }

    fun clearScores() {
        prefs.edit().remove(KEY_SCORES).apply()
    }
}
