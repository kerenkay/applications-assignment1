package com.example.applications_assignment1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class HomeActivity : AppCompatActivity() {
    private val PREFS_NAME = "game_settings"
    private val KEY_GAME_MODE = "GAME_MODE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val btnNewGame = findViewById<MaterialButton>(R.id.btnStartGame)

        val modeContainer = findViewById<View>(R.id.modeContainer)
        val btnModeSlow = findViewById<MaterialButton>(R.id.btnBtnSlow)
        val btnModeFast = findViewById<MaterialButton>(R.id.btnBtnFast)
        val btnModeSensors = findViewById<MaterialButton>(R.id.btnSensor)

        btnNewGame.visibility = View.VISIBLE
        modeContainer.visibility = View.GONE

        btnNewGame.setOnClickListener {
            btnNewGame.visibility = View.GONE
            modeContainer.visibility = View.VISIBLE
        }

        btnModeSlow.setOnClickListener { startGame(GameMode.BUTTONS_SLOW) }
        btnModeFast.setOnClickListener { startGame(GameMode.BUTTONS_FAST) }
        btnModeSensors.setOnClickListener { startGame(GameMode.SENSORS) }
    }

    private fun startGame(mode: GameMode) {
        saveGameMode(mode)

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(KEY_GAME_MODE, mode.name)
        startActivity(intent)
    }

    private fun saveGameMode(mode: GameMode) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        prefs.edit().putString(KEY_GAME_MODE, mode.name).apply()
    }
}

//        val btnStartGame = findViewById<MaterialButton>(R.id.btnStartGame)
//        val rgGameMode = findViewById<RadioGroup>(R.id.rgGameMode)
//
//        val savedMode = loadSavedGameMode()
//        when (savedMode) {
//            GameMode.BUTTONS_SLOW -> rgGameMode.check(R.id.rbModeNormal)
//            GameMode.BUTTONS_FAST -> rgGameMode.check(R.id.rbModeFast)
//            GameMode.SENSORS -> rgGameMode.check(R.id.rbModeSensors)
//        }
//
//        btnStartGame.setOnClickListener {
//
//            val selectedMode = when (rgGameMode.checkedRadioButtonId) {
//                R.id.rbModeNormal -> GameMode.BUTTONS_SLOW
//                R.id.rbModeFast -> GameMode.BUTTONS_FAST
//                R.id.rbModeSensors -> GameMode.SENSORS
//                else -> GameMode.BUTTONS_SLOW
//            }
//            saveGameMode(selectedMode)
//            val intent = Intent(this, MainActivity::class.java)
//            intent.putExtra(KEY_GAME_MODE, selectedMode.name)
//            startActivity(intent)
//        }
//
////        val btnStart = findViewById<Button>(R.id.btnStartGame)
////
////        btnStart.setOnClickListener {
////            val intent = Intent(this, MainActivity::class.java)
////            startActivity(intent)
////        }
//    }
//    private fun saveGameMode(mode: GameMode) {
//        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
//        prefs.edit()
//            .putString(KEY_GAME_MODE, mode.name)
//            .apply()
//    }
//
//    private fun loadSavedGameMode(): GameMode {
//        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
//        val saved = prefs.getString(
//            KEY_GAME_MODE,
//            GameMode.BUTTONS_SLOW.name
//        )!!
//        return GameMode.valueOf(saved)
//    }
//}