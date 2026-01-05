package com.example.applications_assignment1.utilities

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

object BackgroundMusicPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private var currentResId: Int? = null
    private var isPaused = false
    private var wasPlayingBeforeBackground = false

    private var lifecycleObserver: DefaultLifecycleObserver = object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            release()
        }

        override fun onStart(owner: LifecycleOwner) {
            // App came to foreground
            if (wasPlayingBeforeBackground) {
                resume()
                wasPlayingBeforeBackground = false
            }
        }

        override fun onStop(owner: LifecycleOwner) {
            // App went to background
            if (isPlaying()) {
                wasPlayingBeforeBackground = true
                pause()
            }
        }

    }


    // Call once in Application.onCreate()
    fun init(application: Application) {
        // auto play/cancel
        ProcessLifecycleOwner.Companion.get().lifecycle.addObserver(lifecycleObserver)
    }



    fun play(context: Context, @RawRes resId: Int, volume: Float = 1.0f) {
        if (resId == currentResId && isPaused) {
            resume()
            return
        }

        if (resId != currentResId) {
            release()
        }

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context.applicationContext, resId).apply {
                isLooping = true
                setVolume(volume, volume)
            }
            currentResId = resId
        }

        mediaPlayer?.start()
        isPaused = false
    }

    fun pause() {
        mediaPlayer?.takeIf { it.isPlaying }?.let {
            it.pause()
            isPaused = true
        }
    }

    fun resume() {
        mediaPlayer?.takeIf { isPaused }?.let {
            it.start()
            isPaused = false
        }
    }

    fun stop() {
        mediaPlayer?.let {
            it.stop()
            it.prepare()
            isPaused = false
        }
    }

    fun setVolume(volume: Float) {
        val v = volume.coerceIn(0f, 1f)
        mediaPlayer?.setVolume(v, v)
    }

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying == true

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        currentResId = null
        isPaused = false
    }
}