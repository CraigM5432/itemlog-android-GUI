package com.craigmurphy.itemlog.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Reusable top bar used across screens for consistent navigation styling.
@Composable
fun SimpleTopBar(title: String) {

    Text(
        text = title,

        modifier = Modifier

            // Title stretch across the screen width
            .fillMaxWidth()

            // Spacing around the title text
            .padding(16.dp),

        // Uses Material Design headline styling.
        style = MaterialTheme.typography.headlineSmall
    )
}