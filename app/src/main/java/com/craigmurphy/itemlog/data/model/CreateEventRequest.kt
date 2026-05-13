package com.craigmurphy.itemlog.data.model

// Request body sent to the backend when creating a new event.
data class CreateEventRequest(
    val eventName: String,
    val eventDate: String
)