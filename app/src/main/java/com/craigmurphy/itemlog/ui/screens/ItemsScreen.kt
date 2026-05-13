package com.craigmurphy.itemlog.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.craigmurphy.itemlog.data.model.ItemResponse
import com.craigmurphy.itemlog.ui.components.EmptyState
import com.craigmurphy.itemlog.ui.components.EventSummaryCard
import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar
import com.craigmurphy.itemlog.viewmodel.DeleteItemViewModel
import com.craigmurphy.itemlog.viewmodel.ItemsViewModel
import kotlinx.coroutines.launch

// Item management screen for a selected event.
// Allows users to view, add, edit, delete items, record sales, view transactions and export CSV.
@Composable
fun ItemsScreen(
    eventId: Long,
    refreshTrigger: Boolean,
    message: String?,
    onMessageShown: () -> Unit,
    onAddItemClick: (Long) -> Unit,
    onRecordSaleClick: (Long) -> Unit,
    onTransactionsClick: (Long) -> Unit,
    onExportCsvClick: (Long) -> Unit,
    onEditItemClick: (ItemResponse) -> Unit,
    onItemDeleted: () -> Unit
) {
    // ViewModel for loading event and item data.
    val viewModel: ItemsViewModel = viewModel()

    // Separate ViewModel for item deletion.
    val deleteViewModel: DeleteItemViewModel = viewModel()

    // Stores the item currently waiting for delete confirmation.
    var itemPendingDelete by remember { mutableStateOf<ItemResponse?>(null) }

    // Snackbar state for showing temporary messages.
    val snackbarHostState = remember { SnackbarHostState() }

    // Coroutine scope used to show snackbar messages after deletion.
    val coroutineScope = rememberCoroutineScope()

    val event = viewModel.event.value

    // Reloads items whenever the event ID or refresh trigger changes.
    LaunchedEffect(eventId, refreshTrigger) {
        viewModel.loadItems(eventId)
    }

    // Shows messages passed from other screens.
    LaunchedEffect(message) {
        if (!message.isNullOrBlank()) {
            snackbarHostState.showSnackbar(message)
            onMessageShown()
        }
    }

    val items = viewModel.items.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    val deleteErrorMessage = deleteViewModel.errorMessage.value

    Scaffold(
        topBar = {
            SimpleTopBar("Items")
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Displays selected event summary when available.
            event?.let {
                EventSummaryCard(
                    eventName = it.eventName,
                    eventDate = it.eventDate
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Displays action buttons.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onAddItemClick(eventId) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Add Item")
                }

                Button(
                    onClick = { onRecordSaleClick(eventId) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Record Sale")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { onTransactionsClick(eventId) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Transactions")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { onExportCsvClick(eventId) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Export CSV")
            }

            Spacer(modifier = Modifier.height(16.dp))

            ScreenHeader("Items for this event")

            Spacer(modifier = Modifier.height(12.dp))

            // Shows delete-related errors.
            deleteErrorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            when {
                isLoading -> {
                    Text("Loading event items...")
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                items.isEmpty() -> {
                    EmptyState(
                        title = "No items yet",
                        message = "Use Add Item to create the first item for this event.",
                        symbol = "📦"
                    )
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items) { item ->

                            // Card displaying one item.
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(text = "Price: €${item.price}")
                                    Text(text = "Stock: ${item.quantity}")
                                    Text(text = "Size: ${item.size ?: "N/A"}")
                                    Text(text = "Description: ${item.description ?: "N/A"}")

                                    Spacer(modifier = Modifier.height(8.dp))

                                    TextButton(
                                        onClick = {
                                            onEditItemClick(item)
                                        }
                                    ) {
                                        Text("Edit")
                                    }

                                    TextButton(
                                        onClick = {
                                            itemPendingDelete = item
                                        }
                                    ) {
                                        Text("Delete")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Confirmation dialog prevents accidental item deletion.
    itemPendingDelete?.let { item ->
        AlertDialog(
            onDismissRequest = {
                itemPendingDelete = null
            },
            title = {
                Text("Delete Item")
            },
            text = {
                Text("Are you sure you want to delete \"${item.name}\"?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteViewModel.deleteItem(eventId, item.itemId) {
                            itemPendingDelete = null
                            onItemDeleted()

                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Item deleted successfully.")
                            }
                        }
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        itemPendingDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}