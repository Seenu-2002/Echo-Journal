package com.seenu.dev.android.echojournal.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties

@Composable
fun LoadingAlertDialog(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    cancellable: Boolean = false,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = title,
        text = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        },
        confirmButton = {
        },
        onDismissRequest = onDismiss,
        modifier = modifier,
        properties = DialogProperties(
            dismissOnBackPress = cancellable,
            dismissOnClickOutside = cancellable
        )
    )
}

@Preview
@Composable
private fun LoadingAlertDialogPreview() {
    LoadingAlertDialog(onDismiss = {}, title = {})
}