package com.craigmurphy.itemlog.data.model

// Request body sent to the backend when registering a new user.
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)