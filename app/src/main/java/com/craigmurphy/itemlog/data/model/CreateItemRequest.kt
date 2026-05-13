package com.craigmurphy.itemlog.data.model

// Request body sent to the backend when creating or updating an item.
data class CreateItemRequest(
    val name: String,
    val price: Double,
    val size: String?,
    val quantity: Int,
    val description: String?
)