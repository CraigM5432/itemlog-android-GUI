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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.VisualTransformation

import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar
import com.craigmurphy.itemlog.viewmodel.LoginViewModel

// Login screen.
// Collects username/password input and delegates authentication to LoginViewModel.
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {

    // State storing the username entered by the user.
    var username by remember { mutableStateOf("") }

    // State storing the password entered by the user.
    var password by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    val viewModel: LoginViewModel = viewModel()

    Scaffold(
        topBar = {

            SimpleTopBar("ItemLog Login")
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
            ScreenHeader("Welcome to ItemLog")

            Spacer(modifier = Modifier.height(24.dp))

            // Username input field.
            OutlinedTextField(
                value = username,

                // Updates username state when user types.
                onValueChange = { username = it },

                label = { Text("Username") },

                modifier = Modifier.fillMaxWidth(),

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password input field.
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) {
                                Icons.Filled.VisibilityOff
                            } else {
                                Icons.Filled.Visibility
                            },
                            contentDescription = if (passwordVisible) {
                                "Hide password"
                            } else {
                                "Show password"
                            }
                        )
                    }
                },
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

            // Login button.
            Button(
                onClick = {
                    keyboardController?.hide()

                    viewModel.login(username, password) {
                        onLoginClick()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.isLoading.value
            ) {

                // Changes button text while loading.
                Text(if (viewModel.isLoading.value) "Loading..." else "Login")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Button navigating to the Register screen.
            TextButton(onClick = onRegisterClick) {
                Text("Create Account")
            }
        }
    }
}