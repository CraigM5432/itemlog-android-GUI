package com.craigmurphy.itemlog.data.model

// Response returned by the backend for transaction data.
data class TransactionResponse(
    val transactionId: Long,
    val quantitySold: Int,
    val salePrice: Double,
    val saleTime: String
)