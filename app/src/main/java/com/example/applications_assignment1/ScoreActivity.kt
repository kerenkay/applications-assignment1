package com.example.applications_assignment1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.applications_assignment1.databinding.ActivityScoreBinding

class ScoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imgBackground = findViewById<ImageView>(R.id.imgBackground)
//        ImageLoader.getInstance()
//            .loadImage(R.drawable.img_app_background, imgBackground)
        binding.imgBackground.setImageResource(R.drawable.img_app_background)

        val txtScore = findViewById<TextView>(R.id.txtScore)
        val score = intent.getIntExtra("KEY_SCORE", 0)
        txtScore.text = score.toString()

        binding.btnClose.setOnClickListener {
            startActivity(Intent(this, TopTenActivity::class.java))
            finish()
        }
    }
}