package com.example.applications_assignment1.ui.game

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.applications_assignment1.gameManage.GameManager
import com.example.applications_assignment1.R
import com.example.applications_assignment1.databinding.ActivityMainBinding
import com.example.applications_assignment1.system.media.BackgroundMusicPlayer
import com.example.applications_assignment1.gameManage.BoardRenderer
import com.example.applications_assignment1.gameManage.GameLoopController
import com.example.applications_assignment1.gameManage.GameObjects
import com.example.applications_assignment1.util.ImageLoader
import com.example.applications_assignment1.system.location.LocationHelper
import com.example.applications_assignment1.system.media.SoundEffectPlayer
import com.example.applications_assignment1.system.sensors.TiltController
import com.example.applications_assignment1.system.media.VibrationManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var gameManager: GameManager
    private lateinit var gameMode: GameMode
    private lateinit var tiltController: TiltController

    private lateinit var objects: GameObjects
    private lateinit var renderer: BoardRenderer
    private lateinit var gameLoop: GameLoopController
    private lateinit var locationHelper: LocationHelper

    private var score: Int = 0
    private var delayMs: Long = 700L
    private var isGameOver = false

    companion object {
        private const val NUM_OF_ROADS = 5
        private const val NUM_OF_ROWS = 5
        private const val EXTRA_SCORE = "KEY_SCORE"
        private const val EXTRA_LAT = "EXTRA_LAT"
        private const val EXTRA_LON = "EXTRA_LON"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Background
        ImageLoader.Companion.getInstance().loadImage(R.drawable.img_app_background, binding.imgBackground)

        // Mode from HomeActivity
        gameMode = GameMode.valueOf(
            intent.getStringExtra("GAME_MODE") ?: GameMode.BUTTONS_SLOW.name
        )

        // Init helpers
        objects = GameObjects.Companion.load(this)
        renderer = BoardRenderer(binding, objects)
        locationHelper = LocationHelper(this)

        SoundEffectPlayer.init(this)
        SoundEffectPlayer.load(this, R.raw.crash_sound)
        SoundEffectPlayer.load(this, R.raw.bonus_points)

        BackgroundMusicPlayer.play(this, R.raw.background_music)

        // Game
        gameManager = GameManager(NUM_OF_ROWS, NUM_OF_ROADS)
        renderer.renderChild(gameManager.childPosition)
        renderer.renderLives(gameManager.lives)
        renderer.setScoreText(score)

        // Buttons
        binding.btnLeft.setOnClickListener { goLeft() }
        binding.btnRight.setOnClickListener { goRight() }

        // Tilt controller
        tiltController = TiltController(
            context = this,
            gameModeProvider = { gameMode },
            onMove = { dir -> if (dir < 0) goLeft() else goRight() },
            onSpeed = { norm -> setSpeedFromTilt(norm) }
        )

        setupGameMode()

        // Game gameLoop
        gameLoop = GameLoopController(
            onTick = { tick() },
            initialDelayMs = delayMs
        )
        gameLoop.start()

        // Location permission (optional)
        if (!locationHelper.hasFineLocationPermission()) {
            locationHelper.requestFineLocationPermission()
        }
    }

    private fun tick() {
        // move
        gameManager.moveTheObjects()

        // render
        renderer.renderBoard(gameManager, NUM_OF_ROWS, NUM_OF_ROADS)

        // score
        score++
        renderer.setScoreText(score)

        // events
        if (gameManager.getIsCrash()) handleCrash()
        if (gameManager.getIsBonus()) handleBonus()

        // stop gameLoop when no lives
        if (gameManager.lives <= 0) {
            gameLoop.stop()
        }
    }

    private fun goLeft() {
        gameManager.goLeft()
        renderer.renderChild(gameManager.childPosition)
    }

    private fun goRight() {
        gameManager.goRight()
        renderer.renderChild(gameManager.childPosition)
    }

    private fun handleCrash() {
        renderer.renderLives(gameManager.lives)
        renderer.showCrashAt(gameManager.childPosition)

        SoundEffectPlayer.play(R.raw.crash_sound)
        VibrationManager.getInstance().vibrate()

        if (gameManager.lives == 0) {
            gameOver()
        } else {
            when (gameManager.lives) {
                2 -> Toast.makeText(this, "oh noo!!", Toast.LENGTH_SHORT).show()
                1 -> Toast.makeText(this, "Last chance!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleBonus() {
        Toast.makeText(this, "bonus 10", Toast.LENGTH_SHORT).show()
        score += 10
        renderer.setScoreText(score)
        SoundEffectPlayer.play(R.raw.bonus_points)
    }

    private fun gameOver() {
        if (isGameOver) return
        isGameOver = true
        Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show()

        gameLoop.stop()
        renderer.setButtonsEnabled(false)
        disableSensors()
        BackgroundMusicPlayer.stop()

        // get location once, then move to ScoreActivity
        locationHelper.getLastLocationOrNull { loc ->
            val intent = Intent(this, ScoreActivity::class.java).apply {
                putExtra(EXTRA_SCORE, score)
                putExtra(EXTRA_LAT, loc?.latitude ?: 0.0)
                putExtra(EXTRA_LON, loc?.longitude ?: 0.0)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun setupGameMode() {
        when (gameMode) {
            GameMode.BUTTONS_SLOW -> {
                renderer.showButtons(true)
                disableSensors()
                delayMs = 700L
                loopIfReadySetDelay()
            }

            GameMode.BUTTONS_FAST -> {
                renderer.showButtons(true)
                disableSensors()
                delayMs = 400L
                loopIfReadySetDelay()
            }

            GameMode.SENSORS -> {
                renderer.showButtons(false)
                enableSensors()
            }
        }
    }

    private fun enableSensors() {
        tiltController.start()
    }

    private fun disableSensors() {
        tiltController.stop()
    }

    override fun onResume() {
        super.onResume()
        if (gameManager.lives > 0) gameLoop.start()
        if (gameMode == GameMode.SENSORS) tiltController.start()
        SoundEffectPlayer.resumeAll()
        BackgroundMusicPlayer.play(this, R.raw.background_music)
    }

    override fun onPause() {
        super.onPause()
        gameLoop.stop()
        tiltController.stop()
        SoundEffectPlayer.pauseAll()
        BackgroundMusicPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        gameLoop.release()
        SoundEffectPlayer.release()
        BackgroundMusicPlayer.stop()
    }

    private fun setSpeedFromTilt(norm: Float) {
        val minDelay = 250L
        val maxDelay = 800L
        val aggressive = norm * norm
        val target = (maxDelay - aggressive * (maxDelay - minDelay)).toLong()

        delayMs = (delayMs * 0.7 + target * 0.3).toLong()
        loopIfReadySetDelay()
    }

    private fun loopIfReadySetDelay() {
        if (::gameLoop.isInitialized) {
            gameLoop.setDelay(delayMs)
        }
    }
}