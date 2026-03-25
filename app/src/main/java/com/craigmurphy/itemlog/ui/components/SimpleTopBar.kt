package com.craigmurphy.itemlog.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SimpleTopBar(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(16.dp)
    )
}