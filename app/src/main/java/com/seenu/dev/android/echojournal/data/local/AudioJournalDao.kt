package com.seenu.dev.android.echojournal.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.seenu.dev.android.echojournal.data.entity.Journal
import com.seenu.dev.android.echojournal.data.entity.JournalTag
import com.seenu.dev.android.echojournal.data.entity.JournalTagCrossRef
import com.seenu.dev.android.echojournal.data.entity.JournalWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioJournalDao {

    @Transaction
    fun insertJournal(journal: JournalWithTags) {
        val journalId = insertJournal(journal.journal)
        val crossRefs = journal.tags.map { tag ->
            JournalTagCrossRef(journalId, tag.tagId)
        }
        insertJournalTagCrossRef(crossRefs)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJournal(journal: Journal): Long

    @Transaction
    @Query("SELECT * FROM journal")
    fun getJournals(): Flow<List<JournalWithTags>>

    @Query("SELECT * FROM journal WHERE journalId = :id")
    fun getJournalById(id: Int): JournalWithTags

    @Delete
    fun deleteJournal(journal: Journal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTag(tag: JournalTag): Long

    @Query("SELECT * FROM journal_tag")
    fun getTags(): List<JournalTag>

    @Query("SELECT * FROM journal_tag WHERE tagId = :id")
    fun getTagById(id: Int): JournalTag

    @Delete
    fun deleteTag(tag: JournalTag)

    @Query("SELECT * FROM journal_tag WHERE tag LIKE :startsWith || '%'")
    fun searchTag(startsWith: String): List<JournalTag>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJournalTagCrossRef(crossRefs: List<JournalTagCrossRef>)

}