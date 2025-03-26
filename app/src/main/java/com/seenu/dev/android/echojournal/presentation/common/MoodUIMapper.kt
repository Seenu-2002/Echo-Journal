package com.seenu.dev.android.echojournal.presentation.common

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.seenu.dev.android.echojournal.R
import com.seenu.dev.android.echojournal.data.entity.Mood
import com.seenu.dev.android.echojournal.presentation.theme.LocalMoodColorPalette
import com.seenu.dev.android.echojournal.presentation.theme.MoodColorTint

object MoodUIMapper {

    fun getDrawable(mood: Mood): MoodDrawable {
        val (filled, outline) = when (mood) {
            Mood.EXCITED -> R.drawable.ic_mood_excited to R.drawable.ic_mood_excited_outline
            Mood.PEACEFUL -> R.drawable.ic_mood_peaceful to R.drawable.ic_mood_peaceful_outline
            Mood.NEUTRAL -> R.drawable.ic_mood_neutral to R.drawable.ic_mood_neutral_outline
            Mood.SAD -> R.drawable.ic_mood_sad to R.drawable.ic_mood_sad_outline
            Mood.STRESSED -> R.drawable.ic_mood_stressed to R.drawable.ic_mood_stressed_outline
        }

        return MoodDrawable(outline, filled)
    }

    @Composable
    fun getColorPalette(mood: Mood?): MoodColorTint? {
        if (mood == null) {
            return null
        }

        val palette = LocalMoodColorPalette.current
        return when (mood) {
            Mood.EXCITED -> palette.excited
            Mood.PEACEFUL -> palette.peaceful
            Mood.NEUTRAL -> palette.neutral
            Mood.SAD -> palette.sad
            Mood.STRESSED -> palette.stressed
        }
    }

    fun getLabel(context: Context, mood: Mood): String {
        return context.getString(getLabelRes(mood))
    }

    fun getLabelRes(mood: Mood): Int {
        return when (mood) {
            Mood.EXCITED -> R.string.mood_excited
            Mood.PEACEFUL -> R.string.mood_peaceful
            Mood.NEUTRAL -> R.string.mood_neutral
            Mood.SAD -> R.string.mood_sad
            Mood.STRESSED -> R.string.mood_stressed
        }
    }

}

data class MoodDrawable constructor(
    @DrawableRes
    val outline: Int,
    @DrawableRes
    val filled: Int
)