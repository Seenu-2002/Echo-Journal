package com.seenu.dev.android.echojournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.seenu.dev.android.echojournal.presentation.routes.Route
import com.seenu.dev.android.echojournal.presentation.screens.CreateJournalScreen
import com.seenu.dev.android.echojournal.presentation.screens.JournalListScreen
import com.seenu.dev.android.echojournal.presentation.theme.EchoJournalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EchoJournalTheme {
                val navController = rememberNavController()
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = Route.EntryList
                ) {
                    composable<Route.EntryList> {
                        JournalListScreen(
                            modifier = Modifier.fillMaxSize(),
                            createRecord = {
                                navController.navigate(Route.CreateRecord(it))
                            },
                            openSettings = {
                                navController.navigate(Route.Settings)
                            }
                        )
                    }
                    composable<Route.CreateRecord> {
                        CreateJournalScreen(
                            modifier = Modifier.fillMaxSize(),
                            filePath = it.toRoute<Route.CreateRecord>().audioPath,
                            onBackClicked = {
                                navController.popBackStack()
                            },
                            onCreated = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable<Route.Settings> { }
                }
            }
        }
    }
}