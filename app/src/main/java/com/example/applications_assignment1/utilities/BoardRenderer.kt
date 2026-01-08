package com.example.applications_assignment1.utilities

import android.view.View
import android.widget.ImageView
import com.example.applications_assignment1.CellType
import com.example.applications_assignment1.GameManager
import com.example.applications_assignment1.databinding.ActivityMainBinding

class BoardRenderer(
    private val binding: ActivityMainBinding,
    private val objects: GameObjects
) {
    private val objectsGrid: Array<Array<ImageView>> = arrayOf(
        arrayOf(binding.imgObject00, binding.imgObject01, binding.imgObject02, binding.imgObject03, binding.imgObject04),
        arrayOf(binding.imgObject10, binding.imgObject11, binding.imgObject12, binding.imgObject13, binding.imgObject14),
        arrayOf(binding.imgObject20, binding.imgObject21, binding.imgObject22, binding.imgObject23, binding.imgObject24),
        arrayOf(binding.imgObject30, binding.imgObject31, binding.imgObject32, binding.imgObject33, binding.imgObject34),
        arrayOf(binding.imgObject40, binding.imgObject41, binding.imgObject42, binding.imgObject43, binding.imgObject44),
    )

    private val childRow: Array<ImageView> = arrayOf(
        binding.imgChild00, binding.imgChild01, binding.imgChild02, binding.imgChild03, binding.imgChild04
    )

    private val crashRow: Array<ImageView> = arrayOf(
        binding.imgCrash00, binding.imgCrash01, binding.imgCrash02, binding.imgCrash03, binding.imgCrash04
    )

    fun renderChild(position: Int) {
        clearCrash()
        for (iv in childRow) {
            if (iv.drawable != null) iv.setImageDrawable(null)
        }
        childRow[position].setImageDrawable(objects.child)
    }

    fun renderBoard(gameManager: GameManager, rows: Int, cols: Int) {
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val cellType = gameManager.getCellType(r, c)
                val newDrawable = when (cellType) {
                    CellType.MONSTER -> objects.monster
                    CellType.MONSTER_MIKE -> objects.monsterMike
                    CellType.GIFT -> objects.gift
                    CellType.EMPTY -> null
                }

                val iv = objectsGrid[r][c]
                if (iv.drawable !== newDrawable) {
                    iv.setImageDrawable(newDrawable)
                }
            }
        }
    }

    fun showCrashAt(childPosition: Int) {
        crashRow[childPosition].setImageDrawable(objects.blast)
    }

    fun clearCrash() {
        for (iv in crashRow) {
            if (iv.drawable != null) iv.setImageDrawable(null)
        }
    }

    fun renderLives(lives: Int) {
        binding.heart1.visibility = if (lives >= 1) View.VISIBLE else View.INVISIBLE
        binding.heart2.visibility = if (lives >= 2) View.VISIBLE else View.INVISIBLE
        binding.heart3.visibility = if (lives >= 3) View.VISIBLE else View.INVISIBLE
    }

    fun setScoreText(score: Int) {
        binding.lblScore.text = "score: $score"
    }

    fun setButtonsEnabled(enabled: Boolean) {
        binding.btnLeft.isEnabled = enabled
        binding.btnRight.isEnabled = enabled
    }

    fun showButtons(show: Boolean) {
        binding.btnLeft.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnRight.visibility = if (show) View.VISIBLE else View.GONE
    }
}
