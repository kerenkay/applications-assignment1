package com.example.applications_assignment1.gameManage

import android.os.Handler
import android.os.Looper

class GameLoopController(
    private val onTick: () -> Unit,
    initialDelayMs: Long
) {
    private val handler = Handler(Looper.getMainLooper())
    private var delayMs: Long = initialDelayMs
    private var running = false

    private val runnable = object : Runnable {
        override fun run() {
            if (!running) return
            onTick()
            handler.postDelayed(this, delayMs)
        }
    }

    fun start() {
        if (running) return
        running = true
        handler.post(runnable)
    }

    fun stop() {
        running = false
        handler.removeCallbacks(runnable)
    }

    fun setDelay(ms: Long) {
        delayMs = ms
    }

    fun getDelay(): Long = delayMs

    fun release() {
        stop()
    }
}