package com.craigmurphy.itemlog.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.craigmurphy.itemlog.data.model.TransactionResponse
import com.craigmurphy.itemlog.data.repository.EventRepository
import kotlinx.coroutines.launch

class TransactionsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EventRepository(application)

    var transactions = mutableStateOf<List<TransactionResponse>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun loadTransactions(eventId: Long) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            val result = repository.getTransactions(eventId)

            isLoading.value = false

            if (result.isSuccess) {
                transactions.value = result.getOrNull() ?: emptyList()
            } else {
                errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
}