package com.example.applications_assignment1

class TopTenViewModel : ViewModel() {

    private val _scores = MutableLiveData<List<ScoreEntry>>()
    val scores: LiveData<List<ScoreEntry>> = _scores

    fun loadScores(context: Context) {
        _scores.value = ScoreStorage.loadAll(context)
    }
}
