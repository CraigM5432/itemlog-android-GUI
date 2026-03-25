package com.craigmurphy.itemlog.data.model

data class LoginResponse(
    val token: String,
    val userId: Long,
    val username: String,
    val email: String
)