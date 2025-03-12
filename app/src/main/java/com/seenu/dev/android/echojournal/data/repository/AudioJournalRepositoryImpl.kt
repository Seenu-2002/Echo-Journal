package com.seenu.dev.android.echojournal.data.repository

import com.seenu.dev.android.echojournal.data.entity.Journal
import com.seenu.dev.android.echojournal.data.entity.JournalWithTags
import com.seenu.dev.android.echojournal.data.local.AudioJournalDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudioJournalRepositoryImpl @Inject constructor(
    private val dao: AudioJournalDao
) : AudioJournalRepository() {

    override suspend fun getJournals(): Flow<List<JournalWithTags>> {
        return withContext(Dispatchers.IO) {
            dao.getJournals()
        }
    }

    override suspend fun getJournalById(id: Int): JournalWithTags {
        return withContext(Dispatchers.IO) {
            dao.getJournalById(id)
        }
    }

    override suspend fun insertJournal(journal: JournalWithTags) {
        return withContext(Dispatchers.IO) {
            dao.insertJournal(journal)
        }
    }

    override suspend fun deleteJournal(journal: Journal) {
        return withContext(Dispatchers.IO) {
            dao.deleteJournal(journal)
        }
    }
}