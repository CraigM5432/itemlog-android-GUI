package com.craigmurphy.itemlog.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar
import com.craigmurphy.itemlog.viewmodel.CreateEventViewModel
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

// Screen used to create a new event.
@Composable
fun CreateEventScreen(
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    // Stores event name typed by the user.
    var eventName by remember { mutableStateOf("") }

    // Stores event date typed by the user.
    var eventDate by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    // ViewModel responsible for validation and API call.
    val viewModel: CreateEventViewModel = viewModel()

    Scaffold(
        topBar = {
            SimpleTopBar("Create Event")
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenHeader("Add a new event")

            Spacer(modifier = Modifier.height(24.dp))

            // Event input.
            OutlinedTextField(
                value = eventName,
                onValueChange = { eventName = it },
                label = { Text("Event Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Event input.
            OutlinedTextField(
                value = eventDate,
                onValueChange = { eventDate = it },
                label = { Text("Event Date - YYYY-MM-DD") },
                modifier = Modifier.fillMaxWidth()
            )

            // Displays validation /API errors.
            viewModel.errorMessage.value?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Saves the event by calling the ViewModel.
            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.createEvent(eventName, eventDate) {
                        onSaveClick()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (viewModel.isLoading.value) "Saving..." else "Save Event")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cancels event creation and returns to previous screen.
            TextButton(onClick = onCancelClick) {
                Text("Cancel")
            }
        }
    }
}