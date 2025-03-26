package com.seenu.dev.android.echojournal.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.seenu.dev.android.echojournal.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionAlertDialog(
    isPermanentlyDenied: Boolean,
    permission: Permission,
    onDismiss: () -> Unit,
    onOkClicked: () -> Unit,
    goToSettings: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(permission.title, style = MaterialTheme.typography.titleMedium)
        },
        text = {
            val text = if (isPermanentlyDenied) {
                permission.permanentlyDeniedMessage
            } else {
                permission.description
            }
            Text(text, style = MaterialTheme.typography.bodyMedium)
        },
        confirmButton = {
            if (isPermanentlyDenied) {
                Button(
                    onClick = goToSettings
                ) {
                    Text(text = stringResource(R.string.go_to_settings))
                }
            } else {
                Button(
                    onClick = onOkClicked
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        },
    )
}

data class Permission constructor(
    val permission: String,
    val title: String,
    val description: String,
    val permanentlyDeniedMessage: String
)

@Preview
@Composable
private fun PermissionAlertDialogPreview() {
    val permission = Permission(
        permission = "android.permission.CAMERA",
        title = "Camera Permission",
        description = "This permission is required to take photos and record videos.",
        permanentlyDeniedMessage = "This permission is required to take photos and record videos. Please enable it in the settings."
    )
    PermissionAlertDialog(
        isPermanentlyDenied = true,
        permission = permission,
        onDismiss = {},
        onOkClicked = {},
        goToSettings = {}
    )
}