package com.craigmurphy.itemlog.ui.components

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

// Reusable summary card showing the currently selected event context.
@Composable
fun EventSummaryCard(
    eventName: String,
    eventDate: String
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // Event name displayed
            Text(
                text = eventName,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Event date displayed
            Text(
                text = "Date: $eventDate",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}