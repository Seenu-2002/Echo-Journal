package com.seenu.dev.android.echojournal.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object TypeConvertors {

    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun getListFromString(str: String): List<String> {
        return Json.decodeFromString(str)
    }

}