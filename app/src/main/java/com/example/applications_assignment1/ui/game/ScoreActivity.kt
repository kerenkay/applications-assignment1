package com.example.applications_assignment1.ui.game

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.applications_assignment1.R
import com.example.applications_assignment1.data.model.ScoreEntry
import com.example.applications_assignment1.ui.topTen.TopTenActivity
import com.example.applications_assignment1.databinding.ActivityScoreBinding
import com.example.applications_assignment1.util.ImageLoader
import com.example.applications_assignment1.data.storage.ScoreStorage
import com.example.applications_assignment1.data.storage.SharedPreferencesManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ScoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ImageLoader.Companion.getInstance()
            .loadImage(R.drawable.img_app_background, binding.imgBackground)

        val txtScore = findViewById<TextView>(R.id.txtScore)
        val score = intent.getIntExtra("KEY_SCORE", 0)
        txtScore.text = score.toString()

        binding.btnClose.setOnClickListener {
            startActivity(Intent(this, TopTenActivity::class.java))
            finish()
        }

        val lat = intent.getDoubleExtra("EXTRA_LAT", 0.0)
        val lon = intent.getDoubleExtra("EXTRA_LON", 0.0)

        if (ScoreStorage.isNewHighScore(score)) {

            showNameDialog { name ->
                ScoreStorage.addResult(
                    ScoreEntry(
                        name = name,
                        score = score,
                        lat = lat,
                        lon = lon
                    )
                )
            }

        } else {
            ScoreStorage.addResult(
                ScoreEntry(
                    name = "Player",
                    score = score,
                    lat = lat,
                    lon = lon
                )
            )
        }
    }

    private fun showNameDialog(onNameConfirmed: (String) -> Unit) {
        val view = layoutInflater.inflate(R.layout.dialog_player_name, null)

        val edtName = view.findViewById<TextInputEditText>(R.id.edtName)
        val inputLayout = view.findViewById<TextInputLayout>(R.id.inputLayout)
        val btnSave = view.findViewById<MaterialButton>(R.id.btnSave)
        val txtError = view.findViewById<TextView>(R.id.txtError)

        val lastName = SharedPreferencesManager.Companion.getInstance().getLastPlayerName()
        edtName.setText(lastName)
        edtName.setSelection(edtName.text?.length ?: 0)

        edtName.filters = arrayOf(InputFilter.LengthFilter(12))

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        btnSave.setOnClickListener {
            val name = edtName.text.toString().trim()

            if (name.isBlank()) {
                inputLayout.error = "Name cannot be empty"
                return@setOnClickListener
            }

            inputLayout.error = null
            SharedPreferencesManager.Companion.getInstance().saveLastPlayerName(name)
            onNameConfirmed(name)
            dialog.dismiss()
        }

        dialog.show()
    }


//    private fun showNameDialog(
//        onNameConfirmed: (String) -> Unit
//    ) {
//        val editText = EditText(this).apply {
//            hint = "Enter your name"
//            maxLines = 1
//            filters = arrayOf(InputFilter.LengthFilter(12))
//
//            // שם אחרון כברירת מחדל
//            val lastName = SharedPreferencesManager
//                .getInstance()
//                .getLastPlayerName()
//
//            setText(lastName)
//            setSelection(text.length)
//        }
//
//        AlertDialog.Builder(this)
//            .setTitle("New High Score! 🎉")
//            .setMessage("Enter your name:")
//            .setView(editText)
//            .setCancelable(false)
//            .setPositiveButton("Save", null) // נטפל בלחיצה ידנית
//            .show()
//            .apply {
//                getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
//                    val name = editText.text.toString().trim()
//
//                    if (name.isEmpty()) {
//                        editText.error = "Name cannot be empty"
//                        return@setOnClickListener
//                    }
//
//                    // שמירת השם לפעם הבאה
//                    SharedPreferencesManager
//                        .getInstance()
//                        .saveLastPlayerName(name)
//
//                    onNameConfirmed(name)
//                    dismiss()
//                }
//            }
//    }
}