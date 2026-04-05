package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.craigmurphy.itemlog.data.repository.EventRepository
import kotlinx.coroutines.launch

class RecordSaleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EventRepository(application)

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun createTransaction(
        eventId: Long,
        itemId: String,
        quantitySold: String,
        salePrice: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            val parsedItemId = itemId.toLongOrNull()
            val parsedQuantitySold = quantitySold.toIntOrNull()
            val parsedSalePrice = salePrice.toDoubleOrNull()

            if (parsedItemId == null) {
                isLoading.value = false
                errorMessage.value = "Enter a valid item ID."
                return@launch
            }

            if (parsedQuantitySold == null) {
                isLoading.value = false
                errorMessage.value = "Enter a valid quantity sold."
                return@launch
            }

            if (parsedSalePrice == null) {
                isLoading.value = false
                errorMessage.value = "Enter a valid sale price."
                return@launch
            }

            val result = repository.createTransaction(
                eventId = eventId,
                itemId = parsedItemId,
                quantitySold = parsedQuantitySold,
                salePrice = parsedSalePrice
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