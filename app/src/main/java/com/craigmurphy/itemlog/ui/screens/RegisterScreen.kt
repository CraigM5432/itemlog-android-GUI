package com.craigmurphy.itemlog.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar
import com.craigmurphy.itemlog.viewmodel.RegisterViewModel

// Registration screen.
// Collects new user details and delegates account creation to RegisterViewModel.
@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit
) {
    // UI state for registration form fields.
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val viewModel: RegisterViewModel = viewModel()

    Scaffold(
        topBar = {

            // Reusable top bar component.
            SimpleTopBar("Create Account")
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),

            // Centers content vertically.
            verticalArrangement = Arrangement.Center,

            // Centers content horizontally.
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Screen title/header.
            ScreenHeader("Register for ItemLog")

            Spacer(modifier = Modifier.height(24.dp))

            // Username field.
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email field.
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field.
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },

                // Hides typed password characters.
                visualTransformation = PasswordVisualTransformation(),

                modifier = Modifier.fillMaxWidth()
            )

            // Displays validation/API error messages.
            viewModel.errorMessage.value?.let {

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register button.
            Button(
                onClick = {

                    // Calls ViewModel registration function.
                    viewModel.register(username, email, password) {

                        // Triggered after successful registration.
                        onRegisterClick()
                    }
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                // Changes button text while loading.
                Text(if (viewModel.isLoading.value) "Registering..." else "Register")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Returns user to Login screen.
            TextButton(onClick = onBackClick) {
                Text("Back to Login")
            }
        }
    }
}