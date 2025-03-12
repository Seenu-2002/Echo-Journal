package com.seenu.dev.android.echojournal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seenu.dev.android.echojournal.data.entity.Journal
import com.seenu.dev.android.echojournal.data.entity.JournalTag
import com.seenu.dev.android.echojournal.data.entity.JournalTagCrossRef

@TypeConverters(TypeConvertors::class)
@Database(entities = [Journal::class, JournalTag::class, JournalTagCrossRef::class], version = 1)
abstract class AudioJournalDatabase : RoomDatabase() {

    abstract fun dao(): AudioJournalDao

}