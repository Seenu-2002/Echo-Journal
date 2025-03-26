package com.seenu.dev.android.echojournal.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.echojournal.data.audio.AudioPlayer
import com.seenu.dev.android.echojournal.data.entity.Journal
import com.seenu.dev.android.echojournal.data.entity.JournalTag
import com.seenu.dev.android.echojournal.data.entity.JournalWithTags
import com.seenu.dev.android.echojournal.data.entity.Mood
import com.seenu.dev.android.echojournal.data.repository.AudioJournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class CreateJournalViewModel @Inject constructor(
    private val repository: AudioJournalRepository
) : ViewModel() {

    @Inject
    internal lateinit var player: AudioPlayer

    private val _duration: MutableStateFlow<Int> = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration.asStateFlow()

    private val _currentPosition: MutableStateFlow<Int> = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition.asStateFlow()

    private val _suggestions: MutableStateFlow<List<JournalTag>> = MutableStateFlow(listOf())
    val suggestions: StateFlow<List<JournalTag>> = _suggestions.asStateFlow()

    private val _isPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _createStatus: MutableStateFlow<Status> = MutableStateFlow(Status.EMPTY)
    val createStatus: StateFlow<Status> = _createStatus.asStateFlow()

    private lateinit var file: File

    fun init(file: File) {
        viewModelScope.launch {
            this@CreateJournalViewModel.file = file
            _duration.value = player.init(file, onEndReached = {
                _isPlaying.value = false
            })
        }
    }

    fun isPlaying(): Boolean {
        return player.isPlaying()
    }

    fun play() {
        viewModelScope.launch {
            player.play()
            _isPlaying.value = true
            while (player.isPlaying()) {
                delay(1.seconds)
                _currentPosition.value = player.currentPosition()
            }
        }
    }

    fun getSuggestions(text: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.searchTags(text)
                    .stateIn(viewModelScope)
                    .collect {
                        _suggestions.value = it
                    }
            }
        }
    }

    fun createTag(text: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.createTag(text)
            }
        }
    }

    fun pause() {
        viewModelScope.launch {
            player.pause()
            _isPlaying.value = false
        }
    }

    override fun onCleared() {
        player.reset()
    }

    fun save(
        title: String,
        mood: Mood,
        tags: List<JournalTag>,
        description: String
    ) {
        viewModelScope.launch {
            _createStatus.value = Status.LOADING
            repository.insertJournal(
                JournalWithTags(
                    journal = Journal(
                        journalId = 1L,
                        title = title,
                        mood = mood,
                        description = description,
                        audioPath = file.absolutePath,
                        createdTime = System.currentTimeMillis()
                    ),
                    tags = tags
                )
            )
            _createStatus.value = Status.SUCCESS
        }
    }

    fun discard() {
        viewModelScope.launch {
            file.delete()
            player.reset()
        }
    }

}

enum class Status {
    LOADING,
    SUCCESS,
    ERROR,
    EMPTY
}