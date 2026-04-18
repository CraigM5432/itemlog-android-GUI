package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.craigmurphy.itemlog.data.repository.EventRepository
import kotlinx.coroutines.launch

class DeleteItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EventRepository(application)

    var isDeleting = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun deleteItem(
        eventId: Long,
        itemId: Long,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isDeleting.value = true
            errorMessage.value = null

            val result = repository.deleteItem(eventId, itemId)

            isDeleting.value = false

            if (result.isSuccess) {
                onSuccess()
            } else {
                errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
}