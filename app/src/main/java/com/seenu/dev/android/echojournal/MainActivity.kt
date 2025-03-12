package com.seenu.dev.android.echojournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seenu.dev.android.echojournal.presentation.routes.Route
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Route.EntryList
                    ) {
                        composable<Route.EntryList> {
                            JournalListScreen(
                                modifier = Modifier.fillMaxSize(),
                                navController = navController
                            )
                        }
                        composable<Route.CreateRecord> { }
                        composable<Route.Settings> { }
                    }
                }
            }
        }
    }
}