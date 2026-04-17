package com.craigmurphy.itemlog.data.repository

import android.content.Context
import com.craigmurphy.itemlog.data.model.CreateEventRequest
import com.craigmurphy.itemlog.data.model.EventResponse
import com.craigmurphy.itemlog.network.RetrofitClient
import com.craigmurphy.itemlog.data.model.ItemResponse
import com.craigmurphy.itemlog.data.model.CreateItemRequest
import com.craigmurphy.itemlog.data.model.CreateTransactionRequest
import com.craigmurphy.itemlog.data.model.TransactionResponse
import retrofit2.HttpException

class EventRepository(private val context: Context) {

    suspend fun getEvents(): Result<List<EventResponse>> {
        return try {
            val response = RetrofitClient.create(context).getEvents()
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Result.failure(Exception("HTTP ${e.code()}: $errorBody"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createEvent(eventName: String, eventDate: String): Result<EventResponse> {
        return try {
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

    suspend fun getItems(eventId: Long): Result<List<ItemResponse>> {
        return try {
            val response = RetrofitClient.create(context).getItems(eventId)
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Result.failure(Exception("HTTP ${e.code()}: $errorBody"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createItem(
        eventId: Long,
        name: String,
        price: Double,
        size: String?,
        quantity: Int,
        description: String?
    ): Result<ItemResponse> {
        return try {
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

    suspend fun createTransaction(
        eventId: Long,
        itemId: Long,
        quantitySold: Int,
        salePrice: Double
    ): Result<TransactionResponse> {
        return try {
            val response = RetrofitClient.create(context).createTransaction(
                eventId = eventId,
                request = CreateTransactionRequest(
                    itemId = itemId,
                    quantitySold = quantitySold,
                    salePrice = salePrice
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

    suspend fun getTransactions(eventId: Long): Result<List<TransactionResponse>> {
        return try {
            val response = RetrofitClient.create(context).getTransactions(eventId)
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Result.failure(Exception("HTTP ${e.code()}: $errorBody"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}