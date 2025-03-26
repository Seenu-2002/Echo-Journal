package com.seenu.dev.android.echojournal.data.audio

import java.io.File

interface AudioPlayer {

    suspend fun init(file: File, onEndReached: () -> Unit): Int

    fun play()

    fun pause()

    fun isPlaying(): Boolean

    fun currentPosition(): Int

    fun getDuration(): Int

    fun reset()

}