package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.craigmurphy.itemlog.data.repository.EventRepository
import kotlinx.coroutines.launch

class AddItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EventRepository(application)

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun createItem(
        eventId: Long,
        name: String,
        price: String,
        size: String,
        quantity: String,
        description: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            val parsedPrice = price.toDoubleOrNull()
            val parsedQuantity = quantity.toIntOrNull()

            if (name.isBlank()) {
                isLoading.value = false
                errorMessage.value = "Item name is required."
                return@launch
            }

            if (parsedPrice == null) {
                isLoading.value = false
                errorMessage.value = "Enter a valid price."
                return@launch
            }

            if (parsedQuantity == null) {
                isLoading.value = false
                errorMessage.value = "Enter a valid quantity."
                return@launch
            }

            val result = repository.createItem(
                eventId = eventId,
                name = name,
                price = parsedPrice,
                size = size.ifBlank { null },
                quantity = parsedQuantity,
                description = description.ifBlank { null }
            )

            isLoading.value = false

            if (result.isSuccess) {
                onSuccess()
            } else {
                errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
}