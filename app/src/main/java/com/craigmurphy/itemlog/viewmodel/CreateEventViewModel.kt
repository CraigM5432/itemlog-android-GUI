package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.craigmurphy.itemlog.data.repository.EventRepository
import kotlinx.coroutines.launch

class CreateEventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EventRepository(application)

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun createEvent(
        eventName: String,
        eventDate: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            if (eventName.isBlank()) {
                isLoading.value = false
                errorMessage.value = "Event name is required."
                return@launch
            }

            if (eventDate.isBlank()) {
                isLoading.value = false
                errorMessage.value = "Event date is required."
                return@launch
            }

            val dateRegex = Regex("""\d{4}-\d{2}-\d{2}""")
            if (!dateRegex.matches(eventDate)) {
                isLoading.value = false
                errorMessage.value = "Use date format YYYY-MM-DD."
                return@launch
            }

            val result = repository.createEvent(eventName, eventDate)

            isLoading.value = false

            if (result.isSuccess) {
                onSuccess()
            } else {
                errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
}