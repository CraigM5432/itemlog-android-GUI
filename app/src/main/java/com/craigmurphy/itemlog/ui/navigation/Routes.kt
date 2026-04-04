package com.craigmurphy.itemlog.ui.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val EVENTS = "events"
    const val CREATE_EVENT = "create_event"

    const val ITEMS = "items/{eventId}"
    const val ADD_ITEM = "add_item/{eventId}"
    const val RECORD_SALE = "record_sale/{eventId}"
    const val TRANSACTIONS = "transactions/{eventId}"

    fun itemsRoute(eventId: Long) = "items/$eventId"
    fun addItemRoute(eventId: Long) = "add_item/$eventId"
    fun recordSaleRoute(eventId: Long) = "record_sale/$eventId"
    fun transactionsRoute(eventId: Long) = "transactions/$eventId"
}