package com.example.applications_assignment1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applications_assignment1.utilities.ScoreStorage

class ScoreFragment : Fragment(R.layout.fragment_score) {

    private lateinit var adapter: ScoreAdapter

    // Interface to communicate with Activity/Map
    interface ScoreCallback {
        fun onScoreClicked(lat: Double, lon: Double)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv = view.findViewById<RecyclerView>(R.id.rvTopTen)
        adapter = ScoreAdapter()
        
        adapter.onScoreClicked = { lat, lon ->
            // Notify parent activity if it implements the interface
            (activity as? ScoreCallback)?.onScoreClicked(lat, lon)
        }

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val list = ScoreStorage.loadAll()
            .sortedByDescending { it.score }
            .take(10)
        adapter.submit(list)
    }
}
