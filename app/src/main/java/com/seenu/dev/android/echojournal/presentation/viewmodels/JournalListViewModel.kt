package com.seenu.dev.android.echojournal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.echojournal.data.audio.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.inject.Inject

@HiltViewModel
class JournalListViewModel @Inject constructor() : ViewModel() {

    @Inject
    internal lateinit var recorder: AudioRecorder

    private val _isRecording: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()
    
    private val _recordingStartTime: MutableStateFlow<Long?> = MutableStateFlow(null)
    val recordingStartTime: StateFlow<Long?> = _recordingStartTime.asStateFlow()

    var filePath: String? = null
        private set

    fun startRecording() {
        viewModelScope.launch {
            filePath = recorder.start()
            _recordingStartTime.value = System.currentTimeMillis()
            _isRecording.value = true
        }
    }

    fun pauseRecording() {
        viewModelScope.launch {
            recorder.pause()
            _isRecording.value = false
        }
    }

    fun resumeRecording() {
        viewModelScope.launch {
            recorder.resume()
            _isRecording.value = true
        }
    }

    fun discardRecording() {
        viewModelScope.launch {
            filePath?.let {
                recorder.stop()
                File(it).delete()
            }
            _isRecording.value = false
        }
    }

    fun stopRecording(): String {
        runBlocking {
            recorder.stop()
            _isRecording.value = false
        }
        return filePath!!
    }

}