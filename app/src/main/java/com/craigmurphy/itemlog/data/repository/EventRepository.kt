package com.craigmurphy.itemlog.data.repository

import android.content.Context
import com.craigmurphy.itemlog.data.model.CreateEventRequest
import com.craigmurphy.itemlog.data.model.CreateItemRequest
import com.craigmurphy.itemlog.data.model.CreateTransactionRequest
import com.craigmurphy.itemlog.data.model.EventResponse
import com.craigmurphy.itemlog.data.model.ItemResponse
import com.craigmurphy.itemlog.data.model.TransactionResponse
import com.craigmurphy.itemlog.network.RetrofitClient
import retrofit2.HttpException

// Repository responsible for event, item, and transaction API calls.
// This acts as the data layer between the ViewModels and Retrofit.
class EventRepository(private val context: Context) {

    // Loads all events for the logged-in user.
    suspend fun getEvents(): Result<List<EventResponse>> {

        return try {

            // Calls GET /events.
            val response = RetrofitClient.create(context).getEvents()

            Result.success(response)

        } catch (e: HttpException) {

            // Handles backend HTTP errors.
            val errorBody = e.response()?.errorBody()?.string()

            Result.failure(Exception("HTTP ${e.code()}: $errorBody"))

        } catch (e: Exception) {

            // Handles network failures and unexpected exceptions.
            Result.failure(e)
        }
    }

    // Creates a new event.
    suspend fun createEvent(eventName: String, eventDate: String): Result<EventResponse> {

        return try {

            // Sends POST /events request.
            val response = RetrofitClient.create(context).createEvent(
                CreateEventRequest(
                    eventName = eventName,
                    eventDate = eventDate
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

    // Loads all items for an event.
    suspend fun getItems(eventId: Long): Result<List<ItemResponse>> {

        return try {

            // Calls GET /events/{eventId}/items.
            val response = RetrofitClient.create(context).getItems(eventId)

            Result.success(response)

        } catch (e: HttpException) {

            val errorBody = e.response()?.errorBody()?.string()

            Result.failure(Exception("HTTP ${e.code()}: $errorBody"))

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    // Creates a new item inside an event.
    suspend fun createItem(
        eventId: Long,
        name: String,
        price: Double,
        size: String?,
        quantity: Int,
        description: String?
    ): Result<ItemResponse> {

        return try {

            // Sends POST /events/{eventId}/items request.
            val response = RetrofitClient.create(context).createItem(
                eventId = eventId,
                request = CreateItemRequest(
                    name = name,
                    price = price,
                    size = size,
                    quantity = quantity,
                    description = description
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

    // Creates a new transaction (sale).
    suspend fun createTransaction(
        eventId: Long,
        itemId: Long,
        quantitySold: Int,
        salePrice: Double,
        paymentMethod: String
    ): Result<TransactionResponse> {

        return try {

            // Sends POST /events/{eventId}/transactions request.
            val response = RetrofitClient.create(context).createTransaction(
                eventId = eventId,
                request = CreateTransactionRequest(
                    itemId = itemId,
                    quantitySold = quantitySold,
                    salePrice = salePrice,
                    paymentMethod = paymentMethod
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

    // Loads transaction history for an event.
    suspend fun getTransactions(eventId: Long): Result<List<TransactionResponse>> {

        return try {

            // Calls GET /events/{eventId}/transactions.
            val response = RetrofitClient.create(context).getTransactions(eventId)

            Result.success(response)

        } catch (e: HttpException) {

            val errorBody = e.response()?.errorBody()?.string()

            Result.failure(Exception("HTTP ${e.code()}: $errorBody"))

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    // Downloads transaction history as CSV text.
    suspend fun exportTransactionsCsv(eventId: Long): Result<String> {

        return try {

            // Calls GET /events/{eventId}/transactions/export.
            val response = RetrofitClient.create(context).exportTransactionsCsv(eventId)

            Result.success(response)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    // Deletes an item.
    suspend fun deleteItem(eventId: Long, itemId: Long): Result<Unit> {

        return try {

            // Calls DELETE /events/{eventId}/items/{itemId}.
            RetrofitClient.create(context).deleteItem(eventId, itemId)

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    // Updates an existing item.
    suspend fun updateItem(
        eventId: Long,
        itemId: Long,
        name: String,
        price: Double,
        size: String?,
        quantity: Int,
        description: String?
    ): Result<ItemResponse> {

        return try {

            // Calls PUT /events/{eventId}/items/{itemId}.
            val response = RetrofitClient.create(context).updateItem(
                eventId = eventId,
                itemId = itemId,
                request = CreateItemRequest(
                    name = name,
                    price = price,
                    size = size,
                    quantity = quantity,
                    description = description
                )
            )

            Result.success(response)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    // Finds one event by ID.
    // Currently this loads all events first, then filters locally.
    suspend fun getEventById(eventId: Long): Result<EventResponse?> {

        return try {

            // Loads all events from the backend.
            val events = RetrofitClient.create(context).getEvents()

            // Finds the matching event locally.
            val event = events.find { it.eventId == eventId }

            Result.success(event)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}