package com.seenu.dev.android.echojournal.presentation.screens

import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.echojournal.R
import com.seenu.dev.android.echojournal.presentation.asTimeFormat
import com.seenu.dev.android.echojournal.presentation.components.Permission
import com.seenu.dev.android.echojournal.presentation.components.PermissionAlertDialog
import com.seenu.dev.android.echojournal.presentation.components.PulsatingButton
import com.seenu.dev.android.echojournal.presentation.goToAppSetting
import com.seenu.dev.android.echojournal.presentation.hasPermission
import com.seenu.dev.android.echojournal.presentation.theme.EchoJournalTheme
import com.seenu.dev.android.echojournal.presentation.viewmodels.JournalListViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalListScreen(
    modifier: Modifier,
    createRecord: (audioPath: String) -> Unit,
    openSettings: () -> Unit
) {

    val viewModel: JournalListViewModel = hiltViewModel()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var isPermissionGranted by remember { mutableStateOf(false) }
    val isRecording by viewModel.isRecording.collectAsStateWithLifecycle()
    val recordingStartedTime by viewModel.recordingStartTime.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.background(color = MaterialTheme.colorScheme.secondary),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(R.string.entries_list_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }, actions = {
                IconButton(onClick = {
                    openSettings()
                }) {
                    Icon(painterResource(R.drawable.ic_settings), contentDescription = null)
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color(0xFF578CFF),
                contentColor = Color.White,
                shape = CircleShape, onClick = {
                    viewModel.startRecording()
                    showBottomSheet = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }

        }) { innerPadding ->
        NoJournalsErrorWidget(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )

        if (showBottomSheet) {
            if (isPermissionGranted) {

                var durationText by remember { mutableStateOf("00:00") }
                LaunchedEffect(isRecording) {
                    while (isRecording) {
                        delay(1.seconds)
                        durationText = (System.currentTimeMillis() - recordingStartedTime!!).asTimeFormat()
                    }
                }
                RecordAudioBottomSheet(
                    modifier = Modifier,
                    sheetState = sheetState,
                    isRecording = isRecording,
                    durationLabel = durationText,
                    onSave = {
                        val path = viewModel.stopRecording()
                        showBottomSheet = false
                        createRecord(path)
                    },
                    onResume = {
                        viewModel.resumeRecording()
                    },
                    onPause = {
                        viewModel.pauseRecording()
                    },
                    onDiscard = {
                        viewModel.discardRecording()
                        showBottomSheet = false
                    },
                    onDismiss =  {
                        viewModel.discardRecording()
                        showBottomSheet = false
                    }
                )
            } else {
                RequestPermission(
                    onDismiss = {
                        showBottomSheet = false
                        viewModel.discardRecording()
                    },
                    onPermissionGranted = {
                        isPermissionGranted = true
                    }
                )
            }
        }
    }
}

@Composable
fun RequestPermission(
    onDismiss: () -> Unit,
    onPermissionGranted: () -> Unit
) {
    val activity = LocalActivity.current ?: return
    val permission = Permission(
        permission = android.Manifest.permission.RECORD_AUDIO,
        title = stringResource(R.string.permission_microphone_title),
        description = stringResource(R.string.permission_microphone_message),
        permanentlyDeniedMessage = stringResource(R.string.permission_microphone_permanently_denied)
    )

    if (activity.hasPermission(permission.permission)) {
        return onPermissionGranted()
    }

    var isPermanentlyDenied by remember {
        mutableStateOf(
            !activity.shouldShowRequestPermissionRationale(permission.permission)
        )
    }
    val permissionRequestLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onPermissionGranted()
            } else {
                isPermanentlyDenied =
                    !activity.hasPermission(permission.permission) &&
                            !ActivityCompat.shouldShowRequestPermissionRationale(
                                activity,
                                permission.permission
                            )
            }
        }
    )

    PermissionAlertDialog(
        onDismiss = onDismiss,
        permission = permission,
        onOkClicked = {
            permissionRequestLauncher.launch(permission.permission)
        },
        goToSettings = {
            activity.goToAppSetting()
        },
        isPermanentlyDenied = false
    )

}

@Preview(showBackground = true)
@Composable
private fun NoJournalsErrorWidget(
    modifier: Modifier = Modifier
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(R.drawable.ic_no_journals), contentDescription = null)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.no_entries),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.no_entries_msg),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordAudioBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    isRecording: Boolean,
    durationLabel: String,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSave: () -> Unit,
    onDiscard: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(modifier = modifier, sheetState = sheetState, onDismissRequest = onDismiss) {
        RecordAudioBottomSheetContent(
            modifier = Modifier.fillMaxHeight(.4F),
            durationText = durationLabel,
            isRecording = isRecording,
            onPause = onPause,
            onResume = onResume,
            onSave = onSave,
            onDiscard = onDiscard
        )
    }
}

@Composable
fun RecordAudioBottomSheetContent(
    modifier: Modifier = Modifier,
    durationText: String,
    isRecording: Boolean,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSave: () -> Unit,
    onDiscard: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.recording),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = durationText,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(48.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                modifier = Modifier.background(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.errorContainer
                ), onClick = {
                    onDiscard()
                }) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    contentDescription = null
                )
            }
            PulsatingButton(
                modifier = Modifier.size(72.dp),
                duration = 2000,
                noOfCircles = 3,
                animate = isRecording,
                rippleColor = Color(0xFF578CFF)
            ) {
                IconButton(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            shape = CircleShape,
                            brush = Brush.verticalGradient( // TODO: Theme
                                listOf(
                                    Color(0xFF578CFF),
                                    Color(0xFF1F70F5)
                                )
                            )
                        ), onClick = {
                        if (isRecording) {
                            onSave()
                        } else {
                            onResume()
                        }
                    }) {
                    val icon = if (isRecording) {
                        R.drawable.ic_check
                    } else {
                        R.drawable.ic_mic
                    }
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(icon),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = null
                    )
                }
            }
            if (!isRecording) {
                IconButton(
                    modifier = Modifier.background(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ), onClick = {
                        onSave()
                    }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                }
            } else {
                IconButton(
                    modifier = Modifier.background(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ), onClick = {
                        onPause()
                    }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_pause),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun RecordAudioBottomSheetContentPreview() {
    EchoJournalTheme {
        RecordAudioBottomSheetContent(
            durationText = "01:30:10",
            isRecording = false,
            onSave = {},
            onResume = {},
            onPause = {},
            onDiscard = {}
        )
    }
}