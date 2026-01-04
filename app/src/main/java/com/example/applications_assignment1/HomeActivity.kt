package com.example.applications_assignment1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.applications_assignment1.databinding.ActivityHomeBinding
import com.google.android.material.button.MaterialButton

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val PREFS_NAME = "game_settings"
    private val KEY_GAME_MODE = "GAME_MODE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ImageLoader.init(this)

//        ImageLoader.getInstance()
//            .loadImage(R.drawable.img_background, binding.imgBackground)
        binding.imgBackground.setImageResource(R.drawable.img_background)

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
//        saveGameMode(mode)

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(KEY_GAME_MODE, mode.name)
        startActivity(intent)
    }

//    private fun saveGameMode(mode: GameMode) {
//        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
//        prefs.edit().putString(KEY_GAME_MODE, mode.name).apply()
//    }
}
