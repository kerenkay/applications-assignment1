package com.example.applications_assignment1

import android.content.Context

class TiltController(
    private val context: Context,
    private val gameModeProvider: () -> GameMode,      // כדי לבדוק אם באמת מצב חיישנים פעיל
    private val onMove: (dir: Int) -> Unit             // -1 שמאלה, +1 ימינה
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
    }
}
