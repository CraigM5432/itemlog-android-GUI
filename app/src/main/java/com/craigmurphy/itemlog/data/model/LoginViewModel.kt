package com.craigmurphy.itemlog.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craigmurphy.itemlog.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()

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

            val result = repository.login(username, password)

            isLoading.value = false

            if (result.isSuccess) {
                onSuccess()
            } else {
                errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
}