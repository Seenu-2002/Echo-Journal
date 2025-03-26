package com.seenu.dev.android.echojournal.data.audio

import android.content.Context
import android.media.MediaPlayer
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AudioPlayerImpl(
    private val context: Context
) : AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null
    override suspend fun init(file: File, onEndReached: () -> Unit): Int {
        return suspendCoroutine { continuation ->
            mediaPlayer = MediaPlayer().apply {
                setDataSource(file.absolutePath)
                setOnPreparedListener {
                    continuation.resume(this.duration)
                }
                setOnCompletionListener {
                    onEndReached()
                }
                prepareAsync()
            }
        }
    }

    override fun play() {
        val mediaPlayer =
            mediaPlayer ?: throw IllegalStateException("Media player is not initialized")

        mediaPlayer.start()
    }

    override fun pause() {
        val mediaPlayer =
            mediaPlayer ?: throw IllegalStateException("Media player is not initialized")

        if (!mediaPlayer.isPlaying) {
            throw IllegalStateException("Media player is not playing")
        }

        mediaPlayer.pause()
    }

    override fun isPlaying(): Boolean {
        val mediaPlayer =
            mediaPlayer ?: return false

        return mediaPlayer.isPlaying
    }

    override fun currentPosition(): Int {
        val mediaPlayer =
            mediaPlayer ?: throw IllegalStateException("Media player is not initialized")

        return mediaPlayer.currentPosition
    }

    override fun getDuration(): Int {
        val mediaPlayer =
            mediaPlayer ?: throw IllegalStateException("Media player is not initialized")

        return mediaPlayer.duration
    }

    override fun reset() {
        val mediaPlayer =
            mediaPlayer ?: throw IllegalStateException("Media player is not initialized")

        with(mediaPlayer) {
            stop()
            release()
        }
        this.mediaPlayer = null
    }
}