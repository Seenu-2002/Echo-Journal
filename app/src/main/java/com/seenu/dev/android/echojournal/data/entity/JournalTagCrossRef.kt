package com.seenu.dev.android.echojournal.data.entity

import androidx.room.Entity

@Entity(primaryKeys = ["journalId", "tagId"])
data class JournalTagCrossRef constructor(
    val journalId: Long,
    val tagId: Long
)