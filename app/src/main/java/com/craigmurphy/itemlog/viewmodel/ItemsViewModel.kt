package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.craigmurphy.itemlog.data.model.EventResponse
import com.craigmurphy.itemlog.data.model.ItemResponse
import com.craigmurphy.itemlog.data.repository.EventRepository
import kotlinx.coroutines.launch

class ItemsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EventRepository(application)

    var items = mutableStateOf<List<ItemResponse>>(emptyList())
    var event = mutableStateOf<EventResponse?>(null)
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun loadItems(eventId: Long) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            val itemsResult = repository.getItems(eventId)
            val eventResult = repository.getEventById(eventId)

            if (itemsResult.isSuccess) {
                items.value = itemsResult.getOrNull() ?: emptyList()
            } else {
                errorMessage.value = itemsResult.exceptionOrNull()?.message
            }

            if (eventResult.isSuccess) {
                event.value = eventResult.getOrNull()
            }

            isLoading.value = false
        }
    }
}