package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.craigmurphy.itemlog.data.repository.EventRepository
import kotlinx.coroutines.launch

class ExportCsvViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EventRepository(application)

    var csvContent = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun loadCsv(eventId: Long) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            csvContent.value = ""

            val result = repository.exportTransactionsCsv(eventId)

            isLoading.value = false

            if (result.isSuccess) {
                csvContent.value = result.getOrNull() ?: ""
            } else {
                errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
}