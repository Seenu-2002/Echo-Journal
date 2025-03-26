package com.seenu.dev.android.echojournal.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.echojournal.R
import com.seenu.dev.android.echojournal.data.entity.JournalTag
import com.seenu.dev.android.echojournal.data.entity.Mood
import com.seenu.dev.android.echojournal.presentation.asTimeFormat
import com.seenu.dev.android.echojournal.presentation.common.MoodUIMapper
import com.seenu.dev.android.echojournal.presentation.components.LoadingAlertDialog
import com.seenu.dev.android.echojournal.presentation.theme.EchoJournalTheme
import com.seenu.dev.android.echojournal.presentation.theme.LocalMoodColorPalette
import com.seenu.dev.android.echojournal.presentation.theme.MoodColorTint
import com.seenu.dev.android.echojournal.presentation.viewmodels.CreateJournalViewModel
import com.seenu.dev.android.echojournal.presentation.viewmodels.Status
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJournalScreen(
    modifier: Modifier,
    filePath: String,
    onBackClicked: () -> Unit,
    onCreated: () -> Unit
) {

    val viewmodel: CreateJournalViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewmodel.init(File(filePath))
    }

    val duration by viewmodel.duration.collectAsStateWithLifecycle()
    val currentPosition by viewmodel.currentPosition.collectAsStateWithLifecycle()
    val isPlaying by viewmodel.isPlaying.collectAsStateWithLifecycle()
    val suggestions by viewmodel.suggestions.collectAsStateWithLifecycle()
    val createStatus by viewmodel.createStatus.collectAsStateWithLifecycle()

    var showMoodSelectionBottomSheet by remember {
        mutableStateOf(false)
    }

    var title: String by rememberSaveable {
        mutableStateOf("")
    }
    var mood: Mood? by rememberSaveable {
        mutableStateOf(null)
    }
    var topics = rememberSaveable(saver = JournalTag.Saver) {
        mutableStateListOf<JournalTag>()
    }
    var description by rememberSaveable {
        mutableStateOf("")
    }
    var hasValidData = title.isNotBlank() && mood != null

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.new_entry),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            modifier = Modifier
                                .size(20.dp),
                            painter = painterResource(R.drawable.ic_back),
                            tint = MaterialTheme.colorScheme.secondary,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
        ) {
            val spacingBetweenRows = 4.dp
            TitleRow(
                title = title,
                mood = mood,
                onIconClicked = {
                    showMoodSelectionBottomSheet = true
                }
            ) {
                title = it
            }

            Spacer(modifier = Modifier.height(spacingBetweenRows))
            AudioPlayer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                isPlaying = isPlaying,
                duration = duration,
                currentPosition = currentPosition,
                moodColorTint = MoodUIMapper.getColorPalette(mood),
                onPause = { viewmodel.pause() },
                onPlay = { viewmodel.play() }
            )
            Spacer(modifier = Modifier.height(spacingBetweenRows))

            var topic by remember { mutableStateOf("") }
            TopicRow(
                modifier = Modifier.fillMaxWidth(),
                text = topic,
                selectedTopics = topics,
                suggestions = suggestions,
                onCreateTopic = {
                    viewmodel.createTag(topic)
                },
                onValueChange = {
                    topic = it
                    viewmodel.getSuggestions(it)
                },
                onTopicSelected = { tag ->
                    topics.add(tag)
                },
                onDeleteTag = { tag ->
                    topics.remove(tag)
                },
            )
            Spacer(modifier = Modifier.height(spacingBetweenRows))
            DescriptionRow(modifier = Modifier.fillMaxWidth(), text = description) {
                description = it
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        viewmodel.discard()
                        onBackClicked()
                    },
                    colors = ButtonDefaults.buttonColors().copy(containerColor = Color(0xFFEEF0FF))
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Button(
                    modifier = Modifier
                        .weight(1F)
                        .padding(start = 8.dp)
                        .background(
                            brush = Brush.verticalGradient( // TODO: Theme
                                listOf(
                                    Color(0xFF578CFF),
                                    Color(0xFF1F70F5)
                                )
                            ),
                            shape = ButtonDefaults.shape
                        )
                        .height(ButtonDefaults.MinHeight),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.outline
                    ),
                    onClick = {
                        viewmodel.save(
                            title,
                            mood!!,
                            topics,
                            description
                        )
                    },
                    enabled = hasValidData
                ) {
                    Row {
                        Icon(
                            painter = painterResource(R.drawable.ic_check),
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(R.string.confirm)
                        )
                    }
                }
            }
        }

        if (showMoodSelectionBottomSheet) {
            var localMoodState by remember {
                mutableStateOf(mood)
            }
            MoodSelectionBottomSheet(
                modifier = Modifier.fillMaxWidth(),
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                selected = localMoodState,
                onMoodSelected = {
                    localMoodState = it
                },
                onConfirm = {
                    mood = it
                    showMoodSelectionBottomSheet = false
                },
                onDismiss = {
                    showMoodSelectionBottomSheet = false
                }
            )
        }

        when (createStatus) {
            Status.SUCCESS -> onCreated()
            Status.LOADING -> LoadingAlertDialog(cancellable = false, onDismiss = {}, title = {})
            else -> {}
        }
    }

}

@Composable
fun AudioPlayer(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    duration: Int,
    currentPosition: Int,
    moodColorTint: MoodColorTint?,
    onPause: () -> Unit,
    onPlay: () -> Unit
) {
    Row(
        modifier = modifier
            .background(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .shadow(elevation = 8.dp, shape = CircleShape)
                .background(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface
                )
                .clickable(enabled = true, onClick = {
                    if (isPlaying) {
                        onPause()
                    } else {
                        onPlay()
                    }
                }),
            contentAlignment = Alignment.Center
        ) {
            val iconRes = if (isPlaying) {
                R.drawable.ic_pause
            } else {
                R.drawable.ic_play
            }
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(iconRes),
                tint = moodColorTint?.filled ?: MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        }

        LinearProgressIndicator(
            modifier = Modifier
                .weight(1F)
                .padding(horizontal = 8.dp),
            progress = { currentPosition.toFloat() / duration },
            gapSize = (-15).dp,
            color = moodColorTint?.filled ?: MaterialTheme.colorScheme.primary,
            trackColor = moodColorTint?.outline ?: MaterialTheme.colorScheme.inversePrimary,
            drawStopIndicator = {}
        )

        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = "${currentPosition.asTimeFormat()}/${duration.asTimeFormat()}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
private fun AudioPlayerPreview() {
    EchoJournalTheme {
        AudioPlayer(
            isPlaying = false,
            duration = 7000,
            currentPosition = 6000,
            moodColorTint = LocalMoodColorPalette.current.excited,
            onPause = { },
            onPlay = { }
        )
    }
}

@Composable
fun TitleRow(
    modifier: Modifier = Modifier,
    mood: Mood? = null,
    title: String,
    onIconClicked: () -> Unit,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onIconClicked) {
            val icon = if (mood == null) {
                R.drawable.ic_add_mood
            } else {
                MoodUIMapper.getDrawable(mood).filled
            }

            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(icon),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }
        BasicTextField(
            modifier = Modifier
                .weight(1F)
                .heightIn(min = 0.dp)
                .padding(start = 4.dp),
            value = title,
            textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
            onValueChange = onValueChange,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (title.isEmpty()) {
                        Text(
                            stringResource(
                                R.string.add_title,
                            ),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                    innerTextField()
                }
            },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
    }
}

@Preview
@Composable
private fun TitleRowPreview() {
    var title by remember { mutableStateOf("") }
    var mood: Mood? by remember { mutableStateOf(null) }
    EchoJournalTheme {
        TitleRow(
            Modifier,
            mood,
            title,
            onIconClicked = {
                mood = Mood.entries.random()
            }
        ) {
            title = it
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodSelectionBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    selected: Mood?,
    onMoodSelected: (Mood) -> Unit,
    onConfirm: (Mood) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        MoodSelectionBottomSheetContent(
            selected = selected,
            onMoodSelected = onMoodSelected,
            onCancel = onDismiss,
            onConfirm = onConfirm
        )
    }
}

@Composable
fun MoodSelectionBottomSheetContent(
    modifier: Modifier = Modifier,
    selected: Mood?,
    onMoodSelected: (Mood) -> Unit,
    onConfirm: (Mood) -> Unit,
    onCancel: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(R.string.how_are_you_doing),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (mood in Mood.entries) {
                val isSelected = mood == selected
                val drawable = MoodUIMapper.getDrawable(mood)
                val labelRes = MoodUIMapper.getLabelRes(mood)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = {
                        onMoodSelected(mood)
                    }) {
                        Icon(
                            painter = painterResource(if (isSelected) drawable.filled else drawable.outline),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                    }
                    Text(
                        text = stringResource(labelRes),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Button(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer),
                onClick = onCancel
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Button(
                modifier = Modifier
                    .weight(1F)
                    .padding(horizontal = 8.dp)
                    .background(
                        brush = Brush.verticalGradient( // TODO: Theme
                            listOf(
                                Color(0xFF578CFF),
                                Color(0xFF1F70F5)
                            )
                        ),
                        shape = ButtonDefaults.shape
                    )
                    .height(ButtonDefaults.MinHeight),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = Color.Transparent,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.outline
                ),
                onClick = {
                    onConfirm(selected!!)
                },
                enabled = selected != null,
                contentPadding = PaddingValues()
            ) {
                Text(
                    text = stringResource(R.string.confirm)
                )
            }
        }
    }
}

@Preview
@Composable
private fun MoodSelectionBottomSheetContentPreview() {
    var mood: Mood? by remember { mutableStateOf((Mood.entries + null).random()) }
    EchoJournalTheme {
        MoodSelectionBottomSheetContent(
            Modifier,
            mood,
            onMoodSelected = {
                mood = it
            },
            onConfirm = {

            },
            onCancel = {

            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TopicRow(
    modifier: Modifier = Modifier,
    text: String,
    selectedTopics: List<JournalTag>,
    suggestions: List<JournalTag>,
    onValueChange: (String) -> Unit,
    onCreateTopic: (String) -> Unit,
    onTopicSelected: (JournalTag) -> Unit,
    onDeleteTag: (JournalTag) -> Unit
) {

    var offset by remember { mutableStateOf(Offset.Zero) }
    val density = LocalDensity.current
    var hasFocus by remember { mutableStateOf(false) }

    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = " # ",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            FlowRow(
                maxLines = 4,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                for (topic in selectedTopics) {
                    SelectedTopicText(text = topic.tag, onRemoveClicked = {
                        onDeleteTag(topic)
                    })
                }

                BasicTextField(
                    modifier = Modifier
                        .weight(1F)
                        .padding(vertical = 8.dp)
                        .onGloballyPositioned {
                            offset = it.positionInParent() + Offset(
                                0F,
                                it.size.height.toFloat() + with(density) { 8.dp.toPx() })
                        }
                        .onFocusChanged {
                            hasFocus = it.isFocused
                        },
                    value = text,
                    textStyle = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    ),
                    onValueChange = onValueChange,
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (text.isEmpty()) {
                                Text(
                                    stringResource(R.string.topic),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                            innerTextField()
                        }
                    },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
                )
            }
        }
        if (hasFocus && text.isNotBlank()) {
            Popup(offset = IntOffset(offset.x.toInt(), offset.y.toInt())) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                        .background(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    ) {
                        var showCreate = true
                        for (suggestion in suggestions) {
                            if (selectedTopics.contains(suggestion)) {
                                continue
                            }

                            DropdownMenuRow(modifier = Modifier.clickable {
                                onTopicSelected(suggestion)
                            }, tag = suggestion)
                            if (suggestion.tag == text) {
                                showCreate = false
                            }
                        }
                        if (showCreate) {
                            CreateTopicRow(
                                modifier = Modifier.fillMaxWidth(),
                                text = text,
                                onClick = {
                                    onCreateTopic(text)
                                })
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DropdownMenuRowPreview() {
    EchoJournalTheme { DropdownMenuRow(tag = JournalTag(13213, "Test Tag")) }
}

@Composable
fun DropdownMenuRow(modifier: Modifier = Modifier, tag: JournalTag) {
    Row(modifier = modifier.padding(8.dp)) {
        Text(
            " # ", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary.copy(alpha = .5F)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = tag.tag,
            modifier = Modifier.weight(1F),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary
        )

    }
}

@Composable
fun CreateTopicRow(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.create_tag, text),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
private fun CreateTopicRowPreview() {
    CreateTopicRow(text = "sdljfbsdl") {}
}

@Preview
@Composable
private fun TopicRowPreview() {
    var text by remember { mutableStateOf("") }
    EchoJournalTheme {
        Box(modifier = Modifier.height(200.dp)) {
            TopicRow(
                text = text,
                suggestions = listOf(
                    JournalTag(121, "Amber"),
                    JournalTag(121, "Apple"),
                    JournalTag(121, "Axe"),
//                    JournalTag(121, "A")
                ),
                selectedTopics = listOf(
                    JournalTag(121, "Amber"),
                    JournalTag(121, "Apple"),
                    JournalTag(121, "App"),
                ),
                onValueChange = {
                    text = it
                },
                onCreateTopic = {

                },
                onTopicSelected = {

                },
                onDeleteTag = { tag ->

                }
            )
        }
    }
}

@Composable
fun SelectedTopicText(modifier: Modifier = Modifier, text: String, onRemoveClicked: () -> Unit) {
    Row(
        modifier = modifier
            .background(shape = CircleShape, color = Color(0xFFF2F2F7))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "#", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .5F))
        Text(
            modifier = Modifier.padding(4.dp),
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge
        )
        IconButton(modifier = Modifier.size(16.dp), onClick = onRemoveClicked) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(.5F),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun SelectedTopicTextPreview() {
    SelectedTopicText(text = "Sample") {
    }
}

@Composable
fun DescriptionRow(modifier: Modifier = Modifier, text: String, onValueChange: (String) -> Unit) {
    Row(modifier = modifier) {
        Icon(
            modifier = Modifier
                .padding(6.dp)
                .padding(start = 4.dp)
                .size(16.dp),
            painter = painterResource(R.drawable.ic_mode_edit_outline),
            tint = MaterialTheme.colorScheme.outlineVariant,
            contentDescription = null
        )
        BasicTextField(
            modifier = Modifier
                .weight(1F)
                .padding(vertical = 4.dp)
                .padding(start = 4.dp),
            value = text,
            maxLines = 6,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            ),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            onValueChange = onValueChange,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (text.isEmpty()) {
                        Text(
                            stringResource(R.string.add_description),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Preview
@Composable
private fun DescriptionRowPreview() {
    var desc by remember { mutableStateOf("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.") } // Generate long text please
    DescriptionRow(text = desc) { desc = it }
}