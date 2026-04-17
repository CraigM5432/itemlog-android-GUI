package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.craigmurphy.itemlog.data.model.ItemResponse
import com.craigmurphy.itemlog.data.repository.EventRepository
import kotlinx.coroutines.launch

class RecordSaleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EventRepository(application)

    var items = mutableStateOf<List<ItemResponse>>(emptyList())
    var isLoadingItems = mutableStateOf(false)

    var isSaving = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun loadItems(eventId: Long) {
        viewModelScope.launch {
            isLoadingItems.value = true
            errorMessage.value = null

            val result = repository.getItems(eventId)

            isLoadingItems.value = false

            if (result.isSuccess) {
                items.value = result.getOrNull() ?: emptyList()
            } else {
                errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun createTransaction(
        eventId: Long,
        selectedItemId: Long?,
        quantitySold: String,
        salePrice: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isSaving.value = true
            errorMessage.value = null

            val parsedQuantitySold = quantitySold.toIntOrNull()
            val parsedSalePrice = salePrice.toDoubleOrNull()

            if (selectedItemId == null) {
                isSaving.value = false
                errorMessage.value = "Please select an item."
                return@launch
            }

            if (parsedQuantitySold == null) {
                isSaving.value = false
                errorMessage.value = "Enter a valid quantity sold."
                return@launch
            }

            if (parsedQuantitySold <= 0) {
                isSaving.value = false
                errorMessage.value = "Quantity sold must be greater than 0."
                return@launch
            }

            if (parsedSalePrice == null) {
                isSaving.value = false
                errorMessage.value = "Enter a valid sale price."
                return@launch
            }

            if (parsedSalePrice <= 0.0) {
                isSaving.value = false
                errorMessage.value = "Sale price must be greater than 0."
                return@launch
            }

            val selectedItem = items.value.find { it.itemId == selectedItemId }
            if (selectedItem != null && parsedQuantitySold > selectedItem.quantity) {
                isSaving.value = false
                errorMessage.value = "Not enough stock available."
                return@launch
            }

            val result = repository.createTransaction(
                eventId = eventId,
                itemId = selectedItemId,
                quantitySold = parsedQuantitySold,
                salePrice = parsedSalePrice
            )

            isSaving.value = false

            if (result.isSuccess) {
                onSuccess()
            } else {
                errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
}