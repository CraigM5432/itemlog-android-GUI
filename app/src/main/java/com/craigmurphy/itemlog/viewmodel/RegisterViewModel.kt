package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.craigmurphy.itemlog.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(application)

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun register(
        username: String,
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            if (username.isBlank()) {
                isLoading.value = false
                errorMessage.value = "Username is required."
                return@launch
            }

            if (email.isBlank()) {
                isLoading.value = false
                errorMessage.value = "Email is required."
                return@launch
            }

            if (password.isBlank()) {
                isLoading.value = false
                errorMessage.value = "Password is required."
                return@launch
            }

            val result = repository.register(username, email, password)

            isLoading.value = false

            if (result.isSuccess) {
                onSuccess()
            } else {
                errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
}