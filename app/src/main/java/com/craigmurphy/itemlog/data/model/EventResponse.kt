package com.craigmurphy.itemlog.data.model

// Response returned by the backend for event data.
data class EventResponse(
    val eventId: Long,
    val eventName: String,
    val eventDate: String
)