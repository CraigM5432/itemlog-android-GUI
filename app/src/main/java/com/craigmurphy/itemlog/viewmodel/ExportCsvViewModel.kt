package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.craigmurphy.itemlog.data.model.EventResponse
import com.craigmurphy.itemlog.data.repository.EventRepository
import kotlinx.coroutines.launch

class ExportCsvViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EventRepository(application)

    var csvContent = mutableStateOf("")
    var event = mutableStateOf<EventResponse?>(null)
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun loadCsv(eventId: Long) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            csvContent.value = ""

            val csvResult = repository.exportTransactionsCsv(eventId)
            val eventResult = repository.getEventById(eventId)

            if (csvResult.isSuccess) {
                csvContent.value = csvResult.getOrNull() ?: ""
            } else {
                errorMessage.value = csvResult.exceptionOrNull()?.message
            }

            if (eventResult.isSuccess) {
                event.value = eventResult.getOrNull()
            }

            isLoading.value = false
        }
    }
}