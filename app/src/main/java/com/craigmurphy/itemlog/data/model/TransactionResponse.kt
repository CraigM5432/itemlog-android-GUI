package com.craigmurphy.itemlog.data.model

data class TransactionResponse(
    val transactionId: Long,
    val quantitySold: Int,
    val salePrice: Double,
    val saleTime: String
)