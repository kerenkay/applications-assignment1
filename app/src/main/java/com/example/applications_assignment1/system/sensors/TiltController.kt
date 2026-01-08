package com.example.applications_assignment1.system.sensors

import android.content.Context
import android.util.Log
import com.example.applications_assignment1.ui.game.GameMode

class TiltController(
    private val context: Context,
    private val gameModeProvider: () -> GameMode,
    private val onMove: (dir: Int) -> Unit,
    private val onSpeed: (norm: Float) -> Unit
) : AccSensorCallBack {

    private val sensorApi = AccSensorApi(context, this)

    private var lastMoveTime = 0L
    private val cooldownMs = 180L
    private val threshold = 2.0f

    fun start() = sensorApi.start()
    fun stop() = sensorApi.stop()

    override fun data(x: Float, y: Float, z: Float) {
        if (gameModeProvider() != GameMode.SENSORS) return

        val now = System.currentTimeMillis()
        if (now - lastMoveTime < cooldownMs) return

        when {
            x > threshold -> { onMove(-1); lastMoveTime = now }
            x < -threshold -> { onMove(+1); lastMoveTime = now }
        }

        val forward = z.coerceIn(0f, 6f)
        val norm = forward / 6f
        onSpeed(norm)
        Log.d("SENSOR", "x=$x y=$y z=$z")
    }
}