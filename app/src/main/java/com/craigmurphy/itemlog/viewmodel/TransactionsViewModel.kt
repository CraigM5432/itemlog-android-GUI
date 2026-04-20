package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.craigmurphy.itemlog.data.model.EventResponse
import com.craigmurphy.itemlog.data.model.TransactionResponse
import com.craigmurphy.itemlog.data.repository.EventRepository
import kotlinx.coroutines.launch

class TransactionsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EventRepository(application)

    var transactions = mutableStateOf<List<TransactionResponse>>(emptyList())
    var event = mutableStateOf<EventResponse?>(null)
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun loadTransactions(eventId: Long) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            val transactionsResult = repository.getTransactions(eventId)
            val eventResult = repository.getEventById(eventId)

            if (transactionsResult.isSuccess) {
                transactions.value = transactionsResult.getOrNull() ?: emptyList()
            } else {
                errorMessage.value = transactionsResult.exceptionOrNull()?.message
            }

            if (eventResult.isSuccess) {
                event.value = eventResult.getOrNull()
            }

            isLoading.value = false
        }
    }
}