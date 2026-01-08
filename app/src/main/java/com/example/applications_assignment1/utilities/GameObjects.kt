package com.example.applications_assignment1.utilities

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.example.applications_assignment1.R

data class GameObjects(
    val monster: Drawable,
    val monsterMike: Drawable,
    val child: Drawable,
    val gift: Drawable,
    val blast: Drawable
) {
    companion object {
        fun load(context: Context): GameObjects {
            fun d(resId: Int) = requireNotNull(AppCompatResources.getDrawable(context, resId)) {
                "Missing drawable: $resId"
            }

            return GameObjects(
                monster = d(R.drawable.img_monster),
                monsterMike = d(R.drawable.img_monster_mike),
                child = d(R.drawable.img_child),
                gift = d(R.drawable.img_gift),
                blast = d(R.drawable.img_blast)
            )
        }
    }
}
