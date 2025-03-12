package com.seenu.dev.android.echojournal.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.seenu.dev.android.echojournal.R
import com.seenu.dev.android.echojournal.presentation.routes.Route
import com.seenu.dev.android.echojournal.presentation.viewmodels.JournalListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalListScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: JournalListViewModel = hiltViewModel()
) {

    Scaffold(modifier = modifier.background(color = MaterialTheme.colorScheme.secondary), topBar = {
        TopAppBar(modifier = Modifier.heightIn(max = 56.dp), title = {
            Text(
                text = stringResource(R.string.entries_list_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }, actions = {
            IconButton(onClick = {
                navController.navigate(Route.Settings)
            }) {
                Icon(Icons.Outlined.Settings, contentDescription = null)
            }
        })
    }, floatingActionButton = {

        FloatingActionButton(
            containerColor = Color(0xFF578CFF),
            contentColor = Color.White,
            shape = CircleShape, onClick = {
                navController.navigate(Route.CreateRecord)
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
    }


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