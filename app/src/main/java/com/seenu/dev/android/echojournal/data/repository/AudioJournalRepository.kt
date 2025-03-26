package com.seenu.dev.android.echojournal.data.repository

import com.seenu.dev.android.echojournal.data.entity.Journal
import com.seenu.dev.android.echojournal.data.entity.JournalTag
import com.seenu.dev.android.echojournal.data.entity.JournalWithTags
import kotlinx.coroutines.flow.Flow

abstract class AudioJournalRepository {

    abstract suspend fun getJournals(): Flow<List<JournalWithTags>>

    abstract suspend fun getJournalById(id: Int): JournalWithTags

    abstract suspend fun insertJournal(journal: JournalWithTags)

    abstract suspend fun deleteJournal(journal: Journal)

    abstract suspend fun createTag(tag: String)

    abstract suspend fun searchTags(query: String): Flow<List<JournalTag>>

}