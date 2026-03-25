package com.craigmurphy.itemlog.network

import com.craigmurphy.itemlog.data.model.CreateEventRequest
import com.craigmurphy.itemlog.data.model.CreateItemRequest
import com.craigmurphy.itemlog.data.model.CreateTransactionRequest
import com.craigmurphy.itemlog.data.model.EventResponse
import com.craigmurphy.itemlog.data.model.ItemResponse
import com.craigmurphy.itemlog.data.model.LoginRequest
import com.craigmurphy.itemlog.data.model.LoginResponse
import com.craigmurphy.itemlog.data.model.MessageResponse
import com.craigmurphy.itemlog.data.model.RegisterRequest
import com.craigmurphy.itemlog.data.model.TransactionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): MessageResponse

    @GET("events")
    suspend fun getEvents(): List<EventResponse>

    @POST("events")
    suspend fun createEvent(
        @Body request: CreateEventRequest
    ): EventResponse

    @GET("events/{eventId}/items")
    suspend fun getItems(
        @Path("eventId") eventId: Long
    ): List<ItemResponse>

    @POST("events/{eventId}/items")
    suspend fun createItem(
        @Path("eventId") eventId: Long,
        @Body request: CreateItemRequest
    ): ItemResponse

    @GET("events/{eventId}/transactions")
    suspend fun getTransactions(
        @Path("eventId") eventId: Long
    ): List<TransactionResponse>

    @POST("events/{eventId}/transactions")
    suspend fun createTransaction(
        @Path("eventId") eventId: Long,
        @Body request: CreateTransactionRequest
    ): TransactionResponse
}