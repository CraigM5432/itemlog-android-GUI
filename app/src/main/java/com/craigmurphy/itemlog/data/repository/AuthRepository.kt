package com.craigmurphy.itemlog.data.repository

import android.content.Context
import com.craigmurphy.itemlog.data.model.LoginRequest
import com.craigmurphy.itemlog.data.model.LoginResponse
import com.craigmurphy.itemlog.data.model.RegisterRequest
import com.craigmurphy.itemlog.data.model.RegisterResponse
import com.craigmurphy.itemlog.network.RetrofitClient
import retrofit2.HttpException

class AuthRepository(private val context: Context) {

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val response = RetrofitClient.create(context).login(
                LoginRequest(username, password)
            )
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Result.failure(Exception("HTTP ${e.code()}: $errorBody"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        username: String,
        email: String,
        password: String
    ): Result<RegisterResponse> {
        return try {
            val response = RetrofitClient.create(context).register(
                RegisterRequest(
                    username = username,
                    email = email,
                    password = password
                )
            )
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Result.failure(Exception("HTTP ${e.code()}: $errorBody"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}