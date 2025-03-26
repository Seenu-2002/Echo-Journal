package com.seenu.dev.android.echojournal.presentation.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class MoodColorPalette constructor(
    val excited: MoodColorTint = MoodColorTint(
        filled = Color(0xFFDB6C0B),
        outline = Color(0xFFDDD2C8)
    ),
    val peaceful: MoodColorTint = MoodColorTint(
        filled = Color(0xFFBE3294),
        outline = Color(0xFFE1CEDB)
    ),
    val neutral: MoodColorTint = MoodColorTint(
        filled = Color(0xFF41B278),
        outline = Color(0xFFB9DDCB)
    ),
    val sad: MoodColorTint = MoodColorTint(
        filled = Color(0xFF3A8EDE),
        outline = Color(0xFFC5D8E9)
    ),
    val stressed: MoodColorTint = MoodColorTint(
        filled = Color(0xFFDE3A3A),
        outline = Color(0xFFE9C5C5)
    )
)

data class MoodColorTint constructor(
    val filled: Color,
    val outline: Color
)

val LocalMoodColorPalette = compositionLocalOf<MoodColorPalette>{
    MoodColorPalette()
}