package com.craigmurphy.itemlog.data.model

data class CreateItemRequest(
    val name: String,
    val price: Double,
    val size: String?,
    val quantity: Int,
    val description: String?
)