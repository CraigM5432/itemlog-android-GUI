package com.craigmurphy.itemlog.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

// Reusable section header component used throughout the app.
@Composable
fun ScreenHeader(text: String) {

    Text(
        text = text,

        style = MaterialTheme.typography.headlineSmall
    )
}