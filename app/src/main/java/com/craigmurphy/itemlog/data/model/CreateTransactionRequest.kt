package com.craigmurphy.itemlog.data.model

data class CreateTransactionRequest(
    val itemId: Long,
    val quantitySold: Int,
    val salePrice: Double
)