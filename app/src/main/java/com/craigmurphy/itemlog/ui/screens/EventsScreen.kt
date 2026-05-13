package com.craigmurphy.itemlog.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.craigmurphy.itemlog.ui.components.EmptyState
import com.craigmurphy.itemlog.ui.components.EventCard
import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar
import com.craigmurphy.itemlog.viewmodel.EventsViewModel

// Main screen shown after login.
// Displays all events belonging to the logged-in user.
@Composable
fun EventsScreen(
    refreshTrigger: Boolean,
    message: String?,
    onMessageShown: () -> Unit,
    onCreateEventClick: () -> Unit,
    onEventClick: (Long) -> Unit,
    onLogoutClick: () -> Unit,
    onSessionExpired: () -> Unit
) {
    // ViewModel responsible for loading events from the API.
    val viewModel: EventsViewModel = viewModel()

    // Snackbar state used for temporary success messages.
    val snackbarHostState = remember { SnackbarHostState() }

    // Reloads events when refreshTrigger changes.
    LaunchedEffect(refreshTrigger) {
        viewModel.loadEvents()
    }

    val events = viewModel.events.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    val unauthorized = viewModel.unauthorized.value

    // If the API returns 401 Unauthorized, trigger session-expired handling.
    LaunchedEffect(unauthorized) {
        if (unauthorized) {
            onSessionExpired()
        }
    }

    // Shows success messages passed from another screen.
    LaunchedEffect(message) {
        if (!message.isNullOrBlank()) {
            snackbarHostState.showSnackbar(message)
            onMessageShown()
        }
    }

    Scaffold(
        topBar = {
            SimpleTopBar("My Events")
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateEventClick) {
                Text("+")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            ScreenHeader("Your Events")

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Select an event to manage items and sales.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Logout button clears token and returns to login.
            TextButton(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                // Loading state.
                isLoading -> {
                    Text("Loading your events...")
                }

                // Error state.
                errorMessage != null -> {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                // Empty state if no events exist.
                events.isEmpty() -> {
                    EmptyState(
                        title = "No events yet",
                        message = "Tap the + button to create your first event and start tracking items and sales.",
                        symbol = "📅"
                    )
                }

                // Event list.
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(events) { event ->
                            EventCard(
                                eventName = event.eventName,
                                eventDate = event.eventDate,
                                onClick = {
                                    onEventClick(event.eventId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}