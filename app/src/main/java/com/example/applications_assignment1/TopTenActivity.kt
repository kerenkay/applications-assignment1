package com.example.applications_assignment1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.applications_assignment1.databinding.ActivityTopTenBinding

class TopTenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopTenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopTenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.layLst, ScoreFragment())
                .add(R.id.layMap, MapFragment())
                .commit()
        }

        binding.btnClose.setOnClickListener {
            finish()
        }
    }

}