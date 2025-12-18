package com.example.applications_assignment1

import android.app.AlertDialog
import android.content.Intent
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
    private lateinit var MonstersMap: Array<Array<ImageView>>
    private lateinit var childMap: Array<ImageView>
    private lateinit var gameManager: GameManager

    val DELAY: Long = 600
    val NUM_OF_ROADS = 3
    val NUM_OF_ROWS = 5

    val handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        MonstersMap = arrayOf(
            arrayOf(binding.imgMonster00, binding.imgMonster01, binding.imgMonster02),
            arrayOf(binding.imgMonster10, binding.imgMonster11, binding.imgMonster12),
            arrayOf(binding.imgMonster20, binding.imgMonster21, binding.imgMonster22),
            arrayOf(binding.imgMonster30, binding.imgMonster31, binding.imgMonster32),
            arrayOf(binding.imgMonster40, binding.imgMonster41, binding.imgMonster42),
        )

        childMap = arrayOf(
            binding.imgChild00,
            binding.imgChild01,
            binding.imgChild02
        )

        gameManager = GameManager(NUM_OF_ROWS, NUM_OF_ROADS)

        updateChildPosition()
        clearMonsters()

        binding.btnLeft.setOnClickListener {
            goLeft()
        }

        binding.btnRight.setOnClickListener {
            goRight()
        }

        handler.post(gameLoop)
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
        for (child in childMap) {
            child.visibility = View.INVISIBLE
        }
        childMap[gameManager.childPosition].visibility = View.VISIBLE
    }

    fun clearMonsters() {
        gameManager.clearMonsters()
        for (row in 0 until NUM_OF_ROWS) {
            for (col in 0 until NUM_OF_ROADS) {
                MonstersMap[row][col].visibility = View.INVISIBLE
            }
        }
    }

    fun moveTheMonsters() {
        gameManager.moveTheMonsters()
        updateMonsters()
        checkCrash()
    }

    fun updateMonsters() {
        for (row in 0 until NUM_OF_ROWS) {
            for (col in 0 until NUM_OF_ROADS) {
                MonstersMap[row][col].visibility = View.INVISIBLE
            }
        }
        for (monster in gameManager.activeMonsters) {
            MonstersMap[monster.row][monster.col].visibility = View.VISIBLE
        }
    }

    fun checkCrash() {
//        val crashed = gameManager.getIsCrash()
        if (gameManager.getIsCrash()) {
            crash()
        }
    }

    private fun crash() {
        val lives = gameManager.lives

        binding.heart1.visibility = if (lives >= 1) View.VISIBLE else View.INVISIBLE
        binding.heart2.visibility = if (lives >= 2) View.VISIBLE else View.INVISIBLE
        binding.heart3.visibility = if (lives >= 3) View.VISIBLE else View.INVISIBLE

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

    fun gameOver() {
        Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show()
        clearMonsters()
        handler.removeCallbacks(gameLoop)
        unableBtn()
        showGameOverDialog()
    }

    fun unableBtn() {
        binding.btnLeft.isEnabled = false
        binding.btnRight.isEnabled = false
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

    val gameLoop = object : Runnable {
        override fun run() {
            moveTheMonsters()
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
        finish()
        startActivity(intent)
    }
}