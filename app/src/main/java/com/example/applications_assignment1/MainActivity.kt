package com.example.applications_assignment1

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.example.applications_assignment1.databinding.ActivityMainBinding
import com.example.applications_assignment1.utilities.BackgroundMusicPlayer
import com.example.applications_assignment1.utilities.ImageLoader
import com.example.applications_assignment1.utilities.ScoreStorage
import com.example.applications_assignment1.utilities.SoundEffectPlayer
import com.example.applications_assignment1.utilities.TiltController
import com.example.applications_assignment1.utilities.VibrationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var ObjectsMap: Array<Array<ImageView>>
    private lateinit var childMap: Array<ImageView>
    private lateinit var crashMap: Array<ImageView>
    private lateinit var crashSound: MediaPlayer
    private lateinit var bonusSound: MediaPlayer
    private lateinit var gameManager: GameManager
    private lateinit var gameMode: GameMode
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var monsterDrawable : Drawable
    private lateinit var monsterMikeDrawable : Drawable
    private lateinit var childDrawable : Drawable
    private lateinit var giftDrawable : Drawable
    private lateinit var blastDrawable : Drawable
    private lateinit var tiltController: TiltController

    private val PREFS_NAME = "game_settings"
    private val KEY_GAME_MODE = "GAME_MODE"
    private val KEY_SCORE = "KEY_SCORE"
    var DELAY: Long = 700
    val NUM_OF_ROADS = 5
    val NUM_OF_ROWS = 5
    private val LOCATION_PERMISSION_REQ = 1001
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

        ImageLoader.getInstance()
            .loadImage(R.drawable.img_app_background, binding.imgBackground)
//        binding.imgBackground.setImageResource(R.drawable.img_app_background)

        gameMode = GameMode.valueOf(
            intent.getStringExtra("GAME_MODE") ?: GameMode.BUTTONS_SLOW.name
        )

        tiltController = TiltController(
            context = this,
            gameModeProvider = { gameMode },
            onMove = { dir ->
                if (dir < 0) goLeft() else goRight()
            },
            onSpeed = { norm ->
                setSpeedFromTilt(norm)
            }
        )
        setupGameMode()


//        monsterDrawable = AppCompatResources.getDrawable(this, R.drawable.img_monster)!!
//        monsterMikeDrawable = AppCompatResources.getDrawable(this, R.drawable.img_monster_mike)!!
//        childDrawable = AppCompatResources.getDrawable(this, R.drawable.img_child)!!
//        giftDrawable = AppCompatResources.getDrawable(this, R.drawable.img_gift)!!
//        blastDrawable = AppCompatResources.getDrawable(this, R.drawable.img_blast)!!
//        monsterDrawable = ContextCompat.getDrawable(this, R.drawable.img_monster)!!
//        monsterMikeDrawable = ContextCompat.getDrawable(this, R.drawable.img_monster_mike)!!
//        childDrawable = ContextCompat.getDrawable(this, R.drawable.img_child)!!
//        giftDrawable = ContextCompat.getDrawable(this, R.drawable.img_gift)!!
//        blastDrawable = ContextCompat.getDrawable(this, R.drawable.img_blast)!!

        fun loadD(resId: Int): Drawable {
            android.util.Log.d("DRAW_LOAD", "loading: ${resources.getResourceName(resId)}")
            return AppCompatResources.getDrawable(this, resId)!!
        }

        monsterDrawable = loadD(R.drawable.img_monster)
        monsterMikeDrawable = loadD(R.drawable.img_monster_mike)
        childDrawable = loadD(R.drawable.img_child)
        giftDrawable = loadD(R.drawable.img_gift)
        blastDrawable = loadD(R.drawable.img_blast)

//        crashSound = MediaPlayer.create(this, R.raw.crash_sound)
//        bonusSound = MediaPlayer.create(this, R.raw.bonus_points)
        SoundEffectPlayer.init(this)
        SoundEffectPlayer.load(this, R.raw.crash_sound)
        SoundEffectPlayer.load(this, R.raw.bonus_points)
        BackgroundMusicPlayer.play(this@MainActivity, R.raw.background_music)

        VibrationManager.init(this)

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

        if (!hasLocationPermission()) {
            requestLocationPermission()
        }
        locationClient = LocationServices.getFusedLocationProviderClient(this)


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
//            child.setImageResource(android.R.color.transparent)
            if (child.drawable != null) child.setImageDrawable(null)
        }
        childMap[gameManager.childPosition].setImageDrawable(childDrawable)
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
        for (row in 0 until NUM_OF_ROWS) {
            for (col in 0 until NUM_OF_ROADS) {

//                val cellType = gameManager.getCellType(row, col)
//
//                ObjectsMap[row][col].setImageResource(
//                    when (cellType) {
//                        CellType.MONSTER -> R.drawable.img_monster
//                        CellType.MONSTER_MIKE    -> R.drawable.img_monster_mike
//                        CellType.GIFT -> R.drawable.img_gift
//                        CellType.EMPTY   -> 0
//                    }
//                )

                val cellType = gameManager.getCellType(row, col)

                val newDrawable: Drawable? = when (cellType) {
                    CellType.MONSTER -> monsterDrawable
                    CellType.MONSTER_MIKE -> monsterMikeDrawable
                    CellType.GIFT -> giftDrawable
                    CellType.EMPTY -> null
                }

                val iv = ObjectsMap[row][col]

                if (iv.drawable !== newDrawable) {
                    iv.setImageDrawable(newDrawable)
                }
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

        crashMap[gameManager.childPosition].setImageDrawable(blastDrawable)
//        crashSound.start()
        SoundEffectPlayer.play(R.raw.crash_sound)

//        vibrate()
        VibrationManager.getInstance().vibrate()
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
//        crashMap[gameManager.childPosition].setImageResource(R.drawable.img_10)
        Toast.makeText(this, "bonus 10", Toast.LENGTH_SHORT).show()
        score += 10
//        bonusSound.start()
        SoundEffectPlayer.play(R.raw.bonus_points)
    }

    fun gameOver() {
        Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show()
        handler.removeCallbacks(gameLoop)
        unableBtn()
        BackgroundMusicPlayer.stop()

        if (hasLocationPermission()) {
            locationClient.lastLocation
                .addOnSuccessListener { location ->
                    saveScoreWithLocation(location)
//                    saveScoreWithLocation(score)
                    openScoreActivity()
                }
                .addOnFailureListener {
                    saveScoreWithLocation(null)
//                    saveScoreWithLocation(score)
                    openScoreActivity()
                }
        } else {
            saveScoreWithLocation(null)
//            saveScoreWithLocation(score)
            openScoreActivity()
        }
    }

//    private fun saveScoreWithLocation(finalScore: Int) {
//        LocationHelper(this).getLastLocation { lat, lon ->
//            val entry = ScoreEntry(score = finalScore, lat = lat, lon = lon)
//            ScoreStorage.addResult(this, entry)
//
//        }
//    }

    private fun saveScoreWithLocation(location: Location?) {
        val entry = ScoreEntry(
            score = score,
            lat = location?.latitude ?: 32.109333,
            lon = location?.longitude ?: 34.855499
        )
        ScoreStorage.addResult(this, entry)
        val all = ScoreStorage.loadAll(this)
        android.util.Log.d("TAL_DEBUG", "Saved scores count=${all.size}, scores=${all.map { it.score }}")
    }

    private fun openScoreActivity() {
        val intent = Intent(this, ScoreActivity::class.java)
        intent.putExtra("KEY_SCORE", score)
        startActivity(intent)
        finish()
    }


    fun unableBtn() {
        binding.btnLeft.isEnabled = false
        binding.btnRight.isEnabled = false
    }

    fun clearCrash(){
        for(img in crashMap){
            if (img.drawable != null) img.setImageDrawable(null)
//            img.setImageResource(android.R.color.transparent)
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
    private fun enableButtons() {
        binding.btnLeft.visibility = View.VISIBLE
        binding.btnRight.visibility = View.VISIBLE
    }

    private fun disableButtons() {
        binding.btnLeft.visibility = View.GONE
        binding.btnRight.visibility = View.GONE
    }

    private fun enableSensors() {
        tiltController.start()
    }

    private fun disableSensors() {
        tiltController.stop()
    }

    override fun onResume() {
        super.onResume()
        if (gameMode == GameMode.SENSORS) tiltController.start()
        SoundEffectPlayer.resumeAll()
    }

    override fun onPause() {
        super.onPause()
        tiltController.stop()
        SoundEffectPlayer.pauseAll()
    }

    private fun setSpeedFromTilt(norm: Float) {

        val minDelay = 250L
        val maxDelay = 800L
        val aggressive = norm * norm
        val target = (maxDelay - aggressive * (maxDelay - minDelay)).toLong()

        DELAY = (DELAY * 0.7 + target * 0.3).toLong()
        android.util.Log.d("SPEED", "norm=$norm DELAY=$DELAY")

    }

    private fun hasLocationPermission(): Boolean {
        return checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }
    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQ
        )
    }

    override fun onDestroy() {
        super.onDestroy()
//        crashSound.release()
//        bonusSound.release()
        SoundEffectPlayer.release()
        handler.removeCallbacks(gameLoop)
    }
}