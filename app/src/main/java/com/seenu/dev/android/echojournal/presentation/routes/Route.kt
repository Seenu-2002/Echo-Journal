package com.seenu.dev.android.echojournal.presentation.routes

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object EntryList : Route

    @Serializable
    data class CreateRecord constructor(val audioPath: String) : Route

    @Serializable
    data object Settings : Route

}