package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.craigmurphy.itemlog.data.model.ItemResponse
import com.craigmurphy.itemlog.data.repository.EventRepository
import kotlinx.coroutines.launch

class ItemsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EventRepository(application)

    var items = mutableStateOf<List<ItemResponse>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun loadItems(eventId: Long) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            val result = repository.getItems(eventId)

            isLoading.value = false

            if (result.isSuccess) {
                items.value = result.getOrNull() ?: emptyList()
            } else {
                errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
}