package com.seenu.dev.android.echojournal.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class JournalWithTags(
    @Embedded
    val journal: Journal,
    @Relation(
        parentColumn = "journalId",
        entityColumn = "tagId",
        associateBy = Junction(JournalTagCrossRef::class)
    )
    val tags: List<JournalTag>
)


data class TagsWithJournals(
    @Embedded
    val tag: JournalTag,
    @Relation(
        parentColumn = "tagId",
        entityColumn = "journalId",
        associateBy = Junction(JournalTagCrossRef::class)
    )
    val journals: List<Journal>
)