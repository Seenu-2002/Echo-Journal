package com.seenu.dev.android.echojournal.data.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

class AudioRecorderImpl constructor(
    private val context: Context
) : AudioRecorder {

    private var mediaRecorder: MediaRecorder? = null
    private var isRecording: Boolean = false

    override fun start(): String {
        val file = File(context.cacheDir, "audio-${System.currentTimeMillis()}.mp3")
        file.createNewFile()
        start(file)
        return file.absolutePath
    }

    override fun start(file: File) {
        if (isRecording) {
            throw IllegalArgumentException("MediaRecorder is already recording")
        }

        mediaRecorder = getMediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(file.absolutePath)
            prepare()
            start()
            isRecording = true
        }
    }

    @Suppress("DEPRECATION")
    private fun getMediaRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
    }

    override fun pause() {
        if (mediaRecorder == null) {
            throw IllegalStateException("MediaRecorder is not started")
        }

        if (!isRecording) {
            throw IllegalStateException("MediaRecorder is already paused")
        }

        mediaRecorder?.pause()
        isRecording = false
    }

    override fun resume() {
        if (mediaRecorder == null) {
            throw IllegalStateException("MediaRecorder is not started")
        }

        if (isRecording) {
            throw IllegalStateException("MediaRecorder is already recording")
        }

        mediaRecorder?.resume()
        isRecording = true
    }

    override fun stop() {
        mediaRecorder?.let {
            it.stop()
            it.reset()
            it.release()
        }

        mediaRecorder = null
        isRecording = false
    }
}