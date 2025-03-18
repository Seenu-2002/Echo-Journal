package com.seenu.dev.android.echojournal.data.audio

import java.io.File

interface AudioRecorder {

    fun start(): String

    fun start(file: File)

    fun pause()

    fun resume()

    fun stop()

}