package com.example.applications_assignment1.utilities

import com.example.applications_assignment1.ScoreEntry

object ScoreStorage {

    fun addResult(entry: ScoreEntry) {
        val sp = SharedPreferencesManager.getInstance()

        val list = sp.loadScores()
        list.add(entry)

        val top10 = list
            .sortedByDescending { it.score }
            .take(10)

        sp.saveScores(top10)
    }

    fun loadTop10(): List<ScoreEntry> {
        return SharedPreferencesManager
            .getInstance()
            .loadScores()
            .sortedByDescending { it.score }
            .take(10)
    }

    fun loadAll(): MutableList<ScoreEntry> {
        return SharedPreferencesManager
            .getInstance()
            .loadScores()
    }

    fun saveAll(list: List<ScoreEntry>) {
        SharedPreferencesManager
            .getInstance()
            .saveScores(list)
    }

    fun clearAll() {
        saveAll(emptyList())
    }

    fun getBestScore(): ScoreEntry? {
        return loadAll().maxByOrNull { it.score }
    }

    fun isNewHighScore(score: Int): Boolean {
        val list = loadAll()
        if (list.size < 10) return true
        return list.any { score > it.score }
    }

    fun size(): Int {
        return loadAll().size
    }
}