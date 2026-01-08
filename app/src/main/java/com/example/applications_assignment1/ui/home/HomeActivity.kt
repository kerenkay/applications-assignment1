package com.example.applications_assignment1.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.applications_assignment1.ui.game.GameMode
import com.example.applications_assignment1.ui.game.MainActivity
import com.example.applications_assignment1.R
import com.example.applications_assignment1.ui.topTen.TopTenActivity
import com.example.applications_assignment1.databinding.ActivityHomeBinding
import com.example.applications_assignment1.util.ImageLoader

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val PREFS_NAME = "game_settings"
    private val KEY_GAME_MODE = "GAME_MODE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ImageLoader.Companion.init(this)

        ImageLoader.Companion.getInstance()
            .loadImage(R.drawable.img_background, binding.imgBackground)

        val btnNewGame = binding.btnStartGame
        val modeContainer = binding.modeContainer
        val btnModeSlow = binding.btnBtnSlow
        val btnModeFast = binding.btnBtnFast
        val btnModeSensors = binding.btnSensor

        btnNewGame.visibility = View.VISIBLE
        modeContainer.visibility = View.GONE

        btnNewGame.setOnClickListener {
            btnNewGame.visibility = View.GONE
            modeContainer.visibility = View.VISIBLE
        }

        btnModeSlow.setOnClickListener { startGame(GameMode.BUTTONS_SLOW) }
        btnModeFast.setOnClickListener { startGame(GameMode.BUTTONS_FAST) }
        btnModeSensors.setOnClickListener { startGame(GameMode.SENSORS) }

        binding.imgTrophy.setOnClickListener {
            startActivity(Intent(this, TopTenActivity::class.java))
        }
    }

    private fun startGame(mode: GameMode) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(KEY_GAME_MODE, mode.name)
        startActivity(intent)
    }
}