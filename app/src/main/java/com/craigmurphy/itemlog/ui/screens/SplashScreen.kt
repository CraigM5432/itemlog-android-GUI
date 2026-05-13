package com.craigmurphy.itemlog.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Splash screen shown when the app launches.
// This screen appears briefly while the app checks whether a JWT token exists.
@Composable
fun SplashScreen() {

    Column(
        modifier = Modifier.fillMaxSize(),

        // Centers content vertically.
        verticalArrangement = Arrangement.Center,

        // Centers content horizontally.
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // App title/logo text.
        Text(
            text = "ItemLog",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Short app description.
        Text(
            text = "Event sales and inventory tracking",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Loading spinner shown while navigation/authentication state is checked.
        CircularProgressIndicator()
    }
}