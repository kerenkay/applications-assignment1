package com.example.applications_assignment1.data.storage

import android.content.Context
import com.example.applications_assignment1.data.model.ScoreEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager private constructor(context: Context) {
    private val KEY_LAST_NAME = "LAST_PLAYER_NAME"
    private val prefs = context.getSharedPreferences(
        "game_settings",
        Context.MODE_PRIVATE
    )

    private val gson = Gson()

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
            TypeToken<MutableList<ScoreEntry>>() {}.type

        return runCatching {
            gson.fromJson<MutableList<ScoreEntry>>(json, type)
        }.getOrElse {
            mutableListOf()
        }
    }

    fun clearScores() {
        prefs.edit().remove(KEY_SCORES).apply()
    }

    fun saveLastPlayerName(name: String) {
        prefs.edit().putString(KEY_LAST_NAME, name).apply()
    }

    fun getLastPlayerName(): String {
        return prefs.getString(KEY_LAST_NAME, "") ?: ""
    }
}