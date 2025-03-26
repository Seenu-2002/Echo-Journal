package com.seenu.dev.android.echojournal.di

import android.content.Context
import androidx.room.Room
import com.seenu.dev.android.echojournal.data.audio.AudioPlayer
import com.seenu.dev.android.echojournal.data.audio.AudioPlayerImpl
import com.seenu.dev.android.echojournal.data.audio.AudioRecorder
import com.seenu.dev.android.echojournal.data.audio.AudioRecorderImpl
import com.seenu.dev.android.echojournal.data.local.AudioJournalDao
import com.seenu.dev.android.echojournal.data.local.AudioJournalDatabase
import com.seenu.dev.android.echojournal.data.repository.AudioJournalRepository
import com.seenu.dev.android.echojournal.data.repository.AudioJournalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AudioJournalDatabase {
        return Room
            .databaseBuilder(context, AudioJournalDatabase::class.java, "audio_journal_db")
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(@ApplicationContext context: Context): AudioJournalDao {
        return provideDatabase(context).dao()
    }

    @Provides
    @Singleton
    fun provideRepository(@ApplicationContext context: Context): AudioJournalRepository {
        return AudioJournalRepositoryImpl(provideDao(context))
    }

    @Provides
    @Singleton
    fun providesAudioRecorder(@ApplicationContext context: Context): AudioRecorder {
        return AudioRecorderImpl(context)
    }

    @Provides
    @Singleton
    fun provideAudioPlayer(@ApplicationContext context: Context): AudioPlayer {
        return AudioPlayerImpl(context)
    }

}