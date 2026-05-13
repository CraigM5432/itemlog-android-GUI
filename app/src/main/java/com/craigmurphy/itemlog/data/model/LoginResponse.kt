package com.craigmurphy.itemlog.data.model

// Response returned by the backend after successful login.
// The token is stored securely and used for protected API requests.
data class LoginResponse(
    val token: String,
    val userId: Long,
    val username: String,
    val email: String
)