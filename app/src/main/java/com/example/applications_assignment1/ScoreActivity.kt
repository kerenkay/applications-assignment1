package com.example.applications_assignment1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.applications_assignment1.databinding.ActivityScoreBinding
import com.example.applications_assignment1.utilities.ImageLoader

class ScoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ImageLoader.getInstance()
            .loadImage(R.drawable.img_app_background, binding.imgBackground)
//        binding.imgBackground.setImageResource(R.drawable.img_app_background)

        val txtScore = findViewById<TextView>(R.id.txtScore)
        val score = intent.getIntExtra("KEY_SCORE", 0)
        txtScore.text = score.toString()

        binding.btnClose.setOnClickListener {
            startActivity(Intent(this, TopTenActivity::class.java))
            finish()
        }
    }
}