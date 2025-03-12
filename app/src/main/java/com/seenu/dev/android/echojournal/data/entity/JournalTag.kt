package com.seenu.dev.android.echojournal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_tag")
data class JournalTag constructor(
    @PrimaryKey(autoGenerate = true)
    val tagId: Long,
    val tag: String
)

