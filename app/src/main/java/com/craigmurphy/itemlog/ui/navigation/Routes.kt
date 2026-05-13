package com.craigmurphy.itemlog.ui.navigation

// Central location for all navigation route strings.
// Keeping routes here avoids repeated hard-coded strings across the app.
object Routes {

    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val EVENTS = "events"
    const val CREATE_EVENT = "create_event"

    const val ITEMS = "items/{eventId}"
    const val ADD_ITEM = "add_item/{eventId}"
    const val RECORD_SALE = "record_sale/{eventId}"
    const val TRANSACTIONS = "transactions/{eventId}"
    const val EXPORT_CSV = "export_csv/{eventId}"

    const val EDIT_ITEM =
        "edit_item/{eventId}/{itemId}/{name}/{price}/{size}/{quantity}/{description}"

    fun itemsRoute(eventId: Long) = "items/$eventId"
    fun addItemRoute(eventId: Long) = "add_item/$eventId"
    fun recordSaleRoute(eventId: Long) = "record_sale/$eventId"
    fun transactionsRoute(eventId: Long) = "transactions/$eventId"
    fun exportCsvRoute(eventId: Long) = "export_csv/$eventId"

    fun editItemRoute(
        eventId: Long,
        itemId: Long,
        name: String,
        price: Double,
        size: String?,
        quantity: Int,
        description: String?
    ): String {
        // Route arguments are encoded so spaces/special characters do not break navigation.
        val safeName = java.net.URLEncoder.encode(name, "UTF-8")
        val safeSize = java.net.URLEncoder.encode(size ?: "", "UTF-8")
        val safeDescription = java.net.URLEncoder.encode(description ?: "", "UTF-8")

        return "edit_item/$eventId/$itemId/$safeName/$price/$safeSize/$quantity/$safeDescription"
    }
}