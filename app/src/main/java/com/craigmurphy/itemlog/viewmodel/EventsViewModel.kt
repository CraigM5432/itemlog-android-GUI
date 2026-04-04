package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.craigmurphy.itemlog.data.model.EventResponse
import com.craigmurphy.itemlog.data.repository.EventRepository
import kotlinx.coroutines.launch

class EventsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EventRepository(application)

    var events = mutableStateOf<List<EventResponse>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun loadEvents() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            val result = repository.getEvents()

            isLoading.value = false

            if (result.isSuccess) {
                events.value = result.getOrNull() ?: emptyList()
            } else {
                errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
}