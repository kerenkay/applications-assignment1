package com.example.applications_assignment1.utilities

import android.content.Context
import com.example.applications_assignment1.ScoreEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ScoreStorage {
    private const val PREFS_NAME = "game_settings"
    private const val KEY_SCORES = "SCORES_LIST"

    private val gson = Gson()

    fun loadAll(context: Context): MutableList<ScoreEntry> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_SCORES, null) ?: return mutableListOf()

        val type = object : TypeToken<MutableList<ScoreEntry>>() {}.type
        return runCatching { gson.fromJson<MutableList<ScoreEntry>>(json, type) }
            .getOrElse { mutableListOf() }
    }

    fun addResult(context: Context, entry: ScoreEntry) {
        val list = loadAll(context)
        list.add(entry)

        val top10 = list
            .sortedWith(compareByDescending<ScoreEntry> { it.score }.thenByDescending { it.timestamp })
            .take(10)

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_SCORES, gson.toJson(top10))
            .apply()

    }
}


//object ScoreStorage {
//    private const val PREFS_NAME = "game_settings"
//    private const val KEY_SCORES = "SCORES_LIST"
//    private val gson = Gson()
//
//    fun loadAll(context: Context): MutableList<ScoreEntry> {
//        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        val json = prefs.getString(KEY_SCORES, null) ?: return mutableListOf()
//
//        val type = object : TypeToken<MutableList<ScoreEntry>>() {}.type
//        return runCatching { gson.fromJson<MutableList<ScoreEntry>>(json, type) }
//            .getOrElse { mutableListOf() }
//    }
//
//    fun saveAll(context: Context, list: List<ScoreEntry>) {
//        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        prefs.edit().putString(KEY_SCORES, gson.toJson(list)).apply()
//    }
//
//    fun addResult(context: Context, entry: ScoreEntry) {
//        val list = loadAll(context)
//        list.add(entry)
//
//        val top10 = list
//            .sortedWith(compareByDescending<ScoreEntry> { it.score }.thenByDescending { it.timestamp })
//            .take(10)
//
//        saveAll(context, top10)
//    }
//}
