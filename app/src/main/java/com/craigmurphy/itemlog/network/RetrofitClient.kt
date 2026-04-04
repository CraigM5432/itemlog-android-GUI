package com.craigmurphy.itemlog.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import com.craigmurphy.itemlog.data.local.TokenManager

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun getClient(context: Context): OkHttpClient {
        val tokenManager = TokenManager(context)

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()

                val token = tokenManager.getToken()
                if (token != null) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
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
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}