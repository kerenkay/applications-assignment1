package com.example.applications_assignment1.ui.topTen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.applications_assignment1.R
import com.example.applications_assignment1.ui.topTen.ScoreFragment
import com.example.applications_assignment1.databinding.ActivityTopTenBinding

class TopTenActivity : AppCompatActivity(), ScoreFragment.ScoreCallback {
    private lateinit var binding: ActivityTopTenBinding
    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopTenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapFragment = MapFragment()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.layLst, ScoreFragment())
                .add(R.id.layMap, mapFragment)
                .commit()
        }

        binding.btnClose.setOnClickListener {
            finish()
        }
    }

    override fun onScoreClicked(lat: Double, lon: Double) {
        mapFragment.zoomTo(lat, lon)
    }
}