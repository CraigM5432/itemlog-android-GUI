package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.craigmurphy.itemlog.data.local.TokenManager
import com.craigmurphy.itemlog.data.repository.AuthRepository
import kotlinx.coroutines.launch

// ViewModel for the LoginScreen.
// ViewModel for the login screen.
// Validates login input, calls the backend, and stores the JWT after successful authentication.
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(application)
    private val tokenManager = TokenManager(application)

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun login(
        username: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            // Frontend validation prevents unnecessary API requests with empty credentials.
            if (username.isBlank()) {
                isLoading.value = false
                errorMessage.value = "Username is required."
                return@launch
            }

            if (password.isBlank()) {
                isLoading.value = false
                errorMessage.value = "Password is required."
                return@launch
            }

            val result = repository.login(username.trim(), password)

            isLoading.value = false

            if (result.isSuccess) {
                result.getOrNull()?.token?.let { tokenManager.saveToken(it) }
                onSuccess()
            } else {
                errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
}