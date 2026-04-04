package com.craigmurphy.itemlog.data.repository

import com.craigmurphy.itemlog.data.model.LoginRequest
import com.craigmurphy.itemlog.data.model.LoginResponse
import com.craigmurphy.itemlog.network.RetrofitClient
import android.content.Context

class AuthRepository(private val context: Context) {

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val response = RetrofitClient.create(context).login(
                LoginRequest(username, password)
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}