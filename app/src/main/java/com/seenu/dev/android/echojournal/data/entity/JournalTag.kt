package com.seenu.dev.android.echojournal.data.entity

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
@Entity(tableName = "journal_tag")
data class JournalTag constructor(
    @PrimaryKey(autoGenerate = true)
    val tagId: Long,
    val tag: String
) {

    object Saver : androidx.compose.runtime.saveable.Saver<SnapshotStateList<JournalTag>, String> {
        override fun restore(value: String): SnapshotStateList<JournalTag>? {
            val list: List<JournalTag> = Json.decodeFromString(value)
            return mutableStateListOf<JournalTag>().apply {
                addAll(list)
            }
        }

        override fun SaverScope.save(value: SnapshotStateList<JournalTag>): String? {
            return Json.encodeToString<List<JournalTag>>(value.toList())
        }

    }

}

