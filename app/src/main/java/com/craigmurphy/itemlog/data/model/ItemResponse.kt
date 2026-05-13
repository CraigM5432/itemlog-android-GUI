package com.craigmurphy.itemlog.data.model

// Response returned by the backend for item data.
data class ItemResponse(
    val itemId: Long,
    val name: String,
    val price: Double,
    val size: String?,
    val quantity: Int,
    val description: String?
)