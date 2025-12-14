package com.example.applications_assignment1

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
            Toast.makeText(this, "crash!!", Toast.LENGTH_SHORT).show()
        }
    }

    fun gameOver(){
        Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show()
        clearMonsters()
        handler.removeCallbacks(gameLoop)
        unableBtn()
    }

    fun unableBtn(){
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
}




//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var MonstersMap: Array<Array<ImageView>>
//    private lateinit var childMap: Array<ImageView>
//    private lateinit var gameManager: GameManager
//    private var lives: Int = 3
//    val DELAY: Long = 600
//    val NUM_OF_ROADS =3
//    val NUM_OF_ROWS=5
//    var childPosition = 1
//    var addMonster=true
//    private val activeMonsters = mutableListOf<Monster>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        MonstersMap = arrayOf(
//            arrayOf(binding.imgMonster00, binding.imgMonster01, binding.imgMonster02),
//            arrayOf(binding.imgMonster10, binding.imgMonster11, binding.imgMonster12),
//            arrayOf(binding.imgMonster20, binding.imgMonster21, binding.imgMonster22),
//            arrayOf(binding.imgMonster30, binding.imgMonster31, binding.imgMonster32),
//            arrayOf(binding.imgMonster40, binding.imgMonster41, binding.imgMonster42),
//        )
//
//        childMap =arrayOf(binding.imgChild00, binding.imgChild01, binding.imgChild02)
//
//        updateChildPosition()
//
//        binding.btnLeft.setOnClickListener {
//            goLeft()
//        }
//
//        binding.btnRight.setOnClickListener {
//            goRight()
//        }
//
////        gameManager = GameManager(numRows = NUM_OF_ROWS, numCols = NUM_OF_ROADS)
//        handler.post(gameLoop)
//
//
//    }
//
//    fun goLeft() {
//        if (childPosition > 0) {
//            childPosition--
//            updateChildPosition()
//        }
//    }
//
//    fun goRight() {
//        if (childPosition < NUM_OF_ROADS -1) {
//            childPosition++
//            updateChildPosition()
//        }
//    }
//
//    fun updateChildPosition() {
//        for ( child in childMap){
//            child.visibility = View.INVISIBLE
//        }
//        childMap[childPosition].visibility = View.VISIBLE
//    }
//
//    fun clearMonsters() {
//        for (row in 0 until NUM_OF_ROWS) {
//            for (col in 0 until NUM_OF_ROADS) {
//                MonstersMap[row][col].visibility = View.INVISIBLE
//            }
//        }
//    }
//
//    fun moveTheMonsters() {
//        //move down the Monsters
//        val it = activeMonsters.iterator()
//        while (it.hasNext()) {
//            val monster = it.next()
//            monster.row += 1
//            if (monster.row >= NUM_OF_ROWS) {
//                it.remove()
//            }
//        }
//
//        // add new monster
//        if(addMonster) {
//            val newCol = (0 until NUM_OF_ROADS).random()
////        if(newCol >= 0) {
//            activeMonsters.add(Monster(row = 0, col = newCol))
//            addMonster = false
////        }
//        }else{
//            addMonster =true
//        }
//
//        updateMonsters()
//    }
//
//    fun updateMonsters() {
//        clearMonsters()
//        for (monster in activeMonsters) {
//            MonstersMap[monster.row][monster.col].visibility = View.VISIBLE
//        }
//    }
//
//    fun checkCrash(){
//        for (monster in activeMonsters){
//            if(monster.row == NUM_OF_ROWS-1 && monster.col == childPosition){
//                lives--
//                crash()
//            }
//        }
//    }
//
//    private fun crash() {
//        binding.heart1.visibility = if (lives >= 1) View.VISIBLE else View.INVISIBLE
//        binding.heart2.visibility = if (lives >= 2) View.VISIBLE else View.INVISIBLE
//        binding.heart3.visibility = if (lives >= 3) View.VISIBLE else View.INVISIBLE
//        vibrate()
//        if(lives == 0){
//            Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show()
//            clearMonsters()
//        }else{
//            Toast.makeText(this, "crash!!", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun vibrate() {
//        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
//        } else {
//            v.vibrate(500)
//        }
//    }
//
//
//    val handler: Handler = Handler(Looper.getMainLooper())
//    val gameLoop = object : Runnable {
//        override fun run() {
//            moveTheMonsters()
//            checkCrash()
//            if(lives>0)
//                handler.postDelayed(this, DELAY)
//        }
//    }
//
//
//
//}