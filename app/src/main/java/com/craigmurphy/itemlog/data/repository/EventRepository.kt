package com.craigmurphy.itemlog.data.repository

import android.content.Context
import com.craigmurphy.itemlog.data.model.CreateEventRequest
import com.craigmurphy.itemlog.data.model.EventResponse
import com.craigmurphy.itemlog.network.RetrofitClient
import com.craigmurphy.itemlog.data.model.ItemResponse

class EventRepository(private val context: Context) {

    suspend fun getEvents(): Result<List<EventResponse>> {
        return try {
            val response = RetrofitClient.create(context).getEvents()
            Result.success(response)
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
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getItems(eventId: Long): Result<List<ItemResponse>> {
        return try {
            val response = RetrofitClient.create(context).getItems(eventId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}