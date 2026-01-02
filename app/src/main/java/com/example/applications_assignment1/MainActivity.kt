package com.example.applications_assignment1

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.applications_assignment1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var ObjectsMap: Array<Array<ImageView>>
    private lateinit var childMap: Array<ImageView>
    private lateinit var crashMap: Array<ImageView>
    private lateinit var crashSound: MediaPlayer
    private lateinit var bonusSound: MediaPlayer
    private lateinit var gameManager: GameManager
    private lateinit var gameMode: GameMode
    private val PREFS_NAME = "game_settings"
    private val KEY_GAME_MODE = "GAME_MODE"

    var DELAY: Long = 700
    val NUM_OF_ROADS = 5
    val NUM_OF_ROWS = 5

    var score: Int = 0
    val handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        gameMode = loadGameMode()
        setupGameMode()

        crashSound = MediaPlayer.create(this, R.raw.crash_sound)
        bonusSound = MediaPlayer.create(this, R.raw.bonus_points)

        ObjectsMap = arrayOf(
            arrayOf(binding.imgObject00, binding.imgObject01, binding.imgObject02, binding.imgObject03, binding.imgObject04),
            arrayOf(binding.imgObject10, binding.imgObject11, binding.imgObject12, binding.imgObject13, binding.imgObject14),
            arrayOf(binding.imgObject20, binding.imgObject21, binding.imgObject22, binding.imgObject23, binding.imgObject24),
            arrayOf(binding.imgObject30, binding.imgObject31, binding.imgObject32, binding.imgObject33, binding.imgObject34),
            arrayOf(binding.imgObject40, binding.imgObject41, binding.imgObject42, binding.imgObject43, binding.imgObject44),
        )

        childMap = arrayOf(
            binding.imgChild00,
            binding.imgChild01,
            binding.imgChild02,
            binding.imgChild03,
            binding.imgChild04
        )

        crashMap = arrayOf(
            binding.imgCrash00,
            binding.imgCrash01,
            binding.imgCrash02,
            binding.imgCrash03,
            binding.imgCrash04
        )

        gameManager = GameManager(NUM_OF_ROWS, NUM_OF_ROADS)

        updateChildPosition()
//        clearObjects()

        binding.btnLeft.setOnClickListener {
            goLeft()
        }

        binding.btnRight.setOnClickListener {
            goRight()
        }

        handler.post(gameLoop)
        android.util.Log.d("TAL_DEBUG", "MainActivity onCreate END")
    }

    fun goLeft() {
        gameManager.goLeft()
        updateChildPosition()
    }

    fun goRight() {
        gameManager.goRight()
        updateChildPosition()
    }

    fun updateChildPosition() {
        clearCrash()
        for (child in childMap) {
            child.setImageResource(android.R.color.transparent)
        }
        childMap[gameManager.childPosition].setImageResource(R.drawable.img_child)
    }

//    fun clearObjects() {
//        gameManager.clearObjects()
//        for (row in 0 until NUM_OF_ROWS) {
//            for (col in 0 until NUM_OF_ROADS) {
//                ObjectsMap[row][col].setImageDrawable(null)
//            }
//        }
//    }

    fun moveTheObjects() {
        gameManager.moveTheObjects()
        updateObjects()
        checkCrash()
    }

    fun updateObjects() {
//        for (row in 0 until NUM_OF_ROWS) {
//            for (col in 0 until NUM_OF_ROADS) {
//                ObjectsMap[row][col].setImageDrawable(null)
//            }
//        }
//        for (obj in gameManager.activeObjects) {
//            ObjectsMap[obj.row][obj.col].setImageResource(R.drawable.img_monster)
//        }
        for (row in 0 until NUM_OF_ROWS) {
            for (col in 0 until NUM_OF_ROADS) {

                val cellType = gameManager.getCellType(row, col)

                ObjectsMap[row][col].setImageResource(
                    when (cellType) {
                        CellType.MONSTER -> R.drawable.img_monster
                        CellType.MONSTER_MIKE    -> R.drawable.img_monster_mike
                        CellType.GIFT -> R.drawable.img_gift
                        CellType.EMPTY   -> 0
                    }
                )
            }
        }
    }

    fun checkCrash() {
//        val crashed = gameManager.getIsCrash()
        if (gameManager.getIsCrash()) {
            crash()
        }
        if(gameManager.getIsBonus()){
            getBonus()
        }
    }

    private fun crash() {
        val lives = gameManager.lives

        binding.heart1.visibility = if (lives >= 1) View.VISIBLE else View.INVISIBLE
        binding.heart2.visibility = if (lives >= 2) View.VISIBLE else View.INVISIBLE
        binding.heart3.visibility = if (lives >= 3) View.VISIBLE else View.INVISIBLE

        crashMap[gameManager.childPosition].setImageResource(R.drawable.img_blast)
        crashSound.start()

        vibrate()
        if (lives == 0) {
            gameOver()
        } else {
            if (lives == 2)
                Toast.makeText(this, "oh noo!!", Toast.LENGTH_SHORT).show()
            if (lives == 1)
                Toast.makeText(this, "Last chance!", Toast.LENGTH_SHORT).show()
        }
    }

    fun getBonus(){
        crashMap[gameManager.childPosition].setImageResource(R.drawable.img_10)
        Toast.makeText(this, "bonus 10", Toast.LENGTH_SHORT).show()
        score += 10
        bonusSound.start()
    }

    fun gameOver() {
        Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show()
//        clearObjects()
        handler.removeCallbacks(gameLoop)
        unableBtn()
        showGameOverDialog()
    }

    fun unableBtn() {
        binding.btnLeft.isEnabled = false
        binding.btnRight.isEnabled = false
    }

    fun clearCrash(){
        for(img in crashMap){
            img.setImageResource(android.R.color.transparent)
        }
    }
    private fun vibrate() {
        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(
                VibrationEffect.createOneShot(
                    500,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            v.vibrate(500)
        }
    }

    fun updateScore(){
        score++;
        binding.lblScore.text = "score: $score"
    }

    val gameLoop = object : Runnable {
        override fun run() {
            moveTheObjects()
            updateScore()
//            checkCrash()
            if (gameManager.lives > 0) {
                handler.postDelayed(this, DELAY)
            }
        }
    }

    private fun showGameOverDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Game Over!")
        builder.setMessage("Do you want to play again or exit?")

        builder.setPositiveButton("New Game") { dialog, which ->
            startNewGame()
        }

        builder.setNegativeButton("Exit") { dialog, which ->
            finish()
        }

        builder.setCancelable(false)
        builder.create().show()
    }

    private fun startNewGame() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(KEY_GAME_MODE, gameMode.name) // אותו מצב כמו המשחק הקודם
        finish()
        startActivity(intent)
    }

    private fun setupGameMode() {
        when (gameMode) {

            GameMode.BUTTONS_SLOW -> {
                enableButtons()
                disableSensors()
                DELAY = 700
            }

            GameMode.BUTTONS_FAST -> {
                enableButtons()
                disableSensors()
                DELAY = 400
            }

            GameMode.SENSORS -> {
                disableButtons()
                enableSensors()
            }
        }
    }

    private fun loadGameMode(): GameMode {
        intent.getStringExtra(KEY_GAME_MODE)?.let {
            return GameMode.valueOf(it)
        }

        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val saved = prefs.getString(KEY_GAME_MODE, GameMode.BUTTONS_SLOW.name)!!
        return GameMode.valueOf(saved)
    }
    private fun enableButtons() {
        binding.btnLeft.visibility = View.VISIBLE
        binding.btnRight.visibility = View.VISIBLE
    }

    private fun disableButtons() {
        binding.btnLeft.visibility = View.GONE
        binding.btnRight.visibility = View.GONE
    }

    private fun enableSensors() {
//        sensorManager.registerListener(...)
    }

    private fun disableSensors() {
//        sensorManager.unregisterListener(...)
    }
    override fun onDestroy() {
        super.onDestroy()
        crashSound.release()
        bonusSound.release()
        handler.removeCallbacks(gameLoop)
    }
}