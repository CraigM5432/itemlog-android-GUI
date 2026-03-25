package com.craigmurphy.itemlog.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar

data class EventUiModel(
    val id: Int,
    val name: String,
    val date: String
)

@Composable
fun EventsScreen(
    onCreateEventClick: () -> Unit,
    onEventClick: (Int) -> Unit
) {
    val events = listOf(
        EventUiModel(1, "Android Studio Testing", "2026-19-03"),
        EventUiModel(2, "Demonstration Testing", "2026-23-03"),
    )

    Scaffold(
        topBar = {
            SimpleTopBar("My Events")
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateEventClick) {
                Text("+")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            ScreenHeader("Select an event")

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(events) { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEventClick(event.id) }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = event.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Date: ${event.date}")
                        }
                    }
                }
            }
        }
    }
}