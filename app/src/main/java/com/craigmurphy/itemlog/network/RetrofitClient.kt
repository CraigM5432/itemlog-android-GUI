package com.craigmurphy.itemlog.network

import android.content.Context
import com.craigmurphy.itemlog.data.local.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

// Creates the Retrofit API client used by the Android app.
// Automatically attaches the stored JWT token to protected backend requests.
object RetrofitClient {

    private const val BASE_URL = "https://itemlog-production.up.railway.app/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun getClient(context: Context): OkHttpClient {
        val tokenManager = TokenManager(context)

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()
                val path = originalRequest.url.encodedPath

                // Auth endpoints are public, so the JWT is only added to protected routes.
                if (!path.startsWith("/auth/")) {
                    val token = tokenManager.getToken()

                    if (!token.isNullOrBlank()) {
                        requestBuilder.addHeader("Authorization", "Bearer $token")
                    }
                }

                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    fun create(context: Context): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClient(context))

            // Scalars handles plain text responses such as CSV exports.
            .addConverterFactory(ScalarsConverterFactory.create())

            // Gson handles JSON request and response bodies.
            .addConverterFactory(GsonConverterFactory.create())

            .build()
            .create(ApiService::class.java)
    }
}