package com.example.applications_assignment1

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.applications_assignment1.utilities.ScoreStorage

class TopTenViewModel : ViewModel() {
    private val _top10 = MutableLiveData<List<ScoreEntry>>()
    val top10: LiveData<List<ScoreEntry>> = _top10

    fun load(context: Context) {
        _top10.value = ScoreStorage.loadTop10(context)
    }
}

