package com.seenu.dev.android.echojournal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal")
data class Journal constructor(
    @PrimaryKey(autoGenerate = true)
    val journalId: Long,
    val title: String,
    val description: String,
    val mood: Mood,
    val createdTime: Long,
    val audioPath: String
)

enum class Mood {
    EXCITED,
    PEACEFUL,
    NEUTRAL,
    SAD,
    STRESSED
}