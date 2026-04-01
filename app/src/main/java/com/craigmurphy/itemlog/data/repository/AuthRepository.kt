package com.craigmurphy.itemlog.data.repository

import com.craigmurphy.itemlog.data.model.LoginRequest
import com.craigmurphy.itemlog.data.model.LoginResponse
import com.craigmurphy.itemlog.network.RetrofitClient

class AuthRepository {

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val response = RetrofitClient.apiService.login(
                LoginRequest(username, password)
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}