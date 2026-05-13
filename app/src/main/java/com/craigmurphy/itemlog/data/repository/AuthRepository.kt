package com.craigmurphy.itemlog.data.repository

import android.content.Context
import com.craigmurphy.itemlog.data.model.LoginRequest
import com.craigmurphy.itemlog.data.model.LoginResponse
import com.craigmurphy.itemlog.data.model.RegisterRequest
import com.craigmurphy.itemlog.data.model.RegisterResponse
import com.craigmurphy.itemlog.network.RetrofitClient
import retrofit2.HttpException

// Repository responsible for authentication-related API calls.
// Repositories act as the data layer between ViewModels and Retrofit.
class AuthRepository(private val context: Context) {

    // Sends login details to the backend API.
    suspend fun login(username: String, password: String): Result<LoginResponse> {

        return try {

            // Sends the login request through Retrofit.
            val response = RetrofitClient.create(context).login(
                LoginRequest(username, password)
            )

            // Wraps the successful response in Kotlin's Result class.
            Result.success(response)

        } catch (e: HttpException) {

            // Reads HTTP error responses returned by the backend.
            val errorBody = e.response()?.errorBody()?.string()

            Result.failure(Exception("HTTP ${e.code()}: $errorBody"))

        } catch (e: Exception) {

            // Handles unexpected errors such as no internet connection.
            Result.failure(e)
        }
    }

    // Sends registration details to the backend API.
    suspend fun register(
        username: String,
        email: String,
        password: String
    ): Result<RegisterResponse> {

        return try {

            // Sends the registration request through Retrofit.
            val response = RetrofitClient.create(context).register(
                RegisterRequest(
                    username = username,
                    email = email,
                    password = password
                )
            )

            // Wraps the successful response in Kotlin's Result class.
            Result.success(response)

        } catch (e: HttpException) {

            // Reads HTTP error responses returned by the backend.
            val errorBody = e.response()?.errorBody()?.string()

            Result.failure(Exception("HTTP ${e.code()}: $errorBody"))

        } catch (e: Exception) {

            // Handles unexpected errors such as network failures.
            Result.failure(e)
        }
    }
}