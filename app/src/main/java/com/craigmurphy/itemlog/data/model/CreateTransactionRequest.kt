package com.craigmurphy.itemlog.data.model

// Request body sent to the backend when recording a sale transaction.
data class CreateTransactionRequest(
    val itemId: Long,
    val quantitySold: Int,
    val salePrice: Double,
    val paymentMethod: String
)