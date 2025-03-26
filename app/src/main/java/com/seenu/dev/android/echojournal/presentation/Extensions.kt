package com.seenu.dev.android.echojournal.presentation

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import java.util.Locale

fun Activity.goToAppSetting() {
    val i = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    )
    startActivity(i)
}

fun Activity.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Long.asTimeFormat(): String {
    val minutes = (this / 60000) % 60
    val seconds = (this / 1000) % 60

    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}

fun Int.asTimeFormat(): String {
    val seconds = this / 1000 % 60
    val minutes = this / 1000 / 60

    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}