package com.craigmurphy.itemlog.data.model

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)