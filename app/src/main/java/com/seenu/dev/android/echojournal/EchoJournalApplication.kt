package com.seenu.dev.android.echojournal

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EchoJournalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        openOrCreateDatabase("audio_journal_db", Context.MODE_PRIVATE, null)
    }

}