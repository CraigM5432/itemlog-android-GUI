package com.craigmurphy.itemlog.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Reusable card component representing one event in the events list.
@Composable
fun EventCard(
    eventName: String,
    eventDate: String,
    onClick: () -> Unit
) {

    // Material Design card container.
    Card(
        modifier = Modifier

            // Makes the card stretch across available width.
            .fillMaxWidth()

            // Makes the entire card clickable.
            .clickable { onClick() }
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // Displays event name.
            Text(
                text = eventName,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Displays event date.
            Text(
                text = "Date: $eventDate",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}