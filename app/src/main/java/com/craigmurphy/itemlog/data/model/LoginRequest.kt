package com.craigmurphy.itemlog.data.model

// Request body sent to the backend when logging in.
data class LoginRequest(
    val username: String,
    val password: String
)