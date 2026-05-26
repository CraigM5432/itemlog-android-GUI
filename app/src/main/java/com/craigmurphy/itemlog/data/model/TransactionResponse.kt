package com.craigmurphy.itemlog.data.model

// Response returned by the backend for transaction data.
data class TransactionResponse(
    val transactionId: Long,
    val quantitySold: Int,
    val salePrice: Double,
    val paymentMethod: String,
    val saleTime: String,
    val itemName: String,
    val totalAmount: Double
)