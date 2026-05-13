package com.craigmurphy.itemlog.network

import com.craigmurphy.itemlog.data.model.CreateEventRequest
import com.craigmurphy.itemlog.data.model.CreateItemRequest
import com.craigmurphy.itemlog.data.model.CreateTransactionRequest
import com.craigmurphy.itemlog.data.model.EventResponse
import com.craigmurphy.itemlog.data.model.ItemResponse
import com.craigmurphy.itemlog.data.model.LoginRequest
import com.craigmurphy.itemlog.data.model.LoginResponse
import com.craigmurphy.itemlog.data.model.RegisterRequest
import com.craigmurphy.itemlog.data.model.RegisterResponse
import com.craigmurphy.itemlog.data.model.TransactionResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Retrofit interface that defines all API endpoints used by the Android app.
// Each function maps to one backend route.
interface ApiService {

    // Sends login details to POST /auth/login.
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    // Sends registration details to POST /auth/register.
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    // Gets all events for the logged-in user from GET /events.
    @GET("events")
    suspend fun getEvents(): List<EventResponse>

    // Creates a new event using POST /events.
    @POST("events")
    suspend fun createEvent(
        @Body request: CreateEventRequest
    ): EventResponse

    // Gets all items for one event from GET /events/{eventId}/items.
    @GET("events/{eventId}/items")
    suspend fun getItems(
        @Path("eventId") eventId: Long
    ): List<ItemResponse>

    // Creates a new item using POST /events/{eventId}/items.
    @POST("events/{eventId}/items")
    suspend fun createItem(
        @Path("eventId") eventId: Long,
        @Body request: CreateItemRequest
    ): ItemResponse

    // Gets all transactions for one event from GET /events/{eventId}/transactions.
    @GET("events/{eventId}/transactions")
    suspend fun getTransactions(
        @Path("eventId") eventId: Long
    ): List<TransactionResponse>

    // Creates a new transaction using POST /events/{eventId}/transactions.
    @POST("events/{eventId}/transactions")
    suspend fun createTransaction(
        @Path("eventId") eventId: Long,
        @Body request: CreateTransactionRequest
    ): TransactionResponse

    // Exports transaction history as CSV from GET /events/{eventId}/transactions/export.
    @GET("events/{eventId}/transactions/export")
    suspend fun exportTransactionsCsv(
        @Path("eventId") eventId: Long
    ): String

    // Deletes an item using DELETE /events/{eventId}/items/{itemId}.
    @DELETE("events/{eventId}/items/{itemId}")
    suspend fun deleteItem(
        @Path("eventId") eventId: Long,
        @Path("itemId") itemId: Long
    ): Unit

    // Updates an item using PUT /events/{eventId}/items/{itemId}.
    @PUT("events/{eventId}/items/{itemId}")
    suspend fun updateItem(
        @Path("eventId") eventId: Long,
        @Path("itemId") itemId: Long,
        @Body request: CreateItemRequest
    ): ItemResponse
}