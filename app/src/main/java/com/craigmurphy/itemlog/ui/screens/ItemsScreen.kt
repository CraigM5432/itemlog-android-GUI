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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar
import com.craigmurphy.itemlog.viewmodel.ItemsViewModel
import androidx.compose.material3.TextButton
import androidx.lifecycle.viewmodel.compose.viewModel
import com.craigmurphy.itemlog.viewmodel.DeleteItemViewModel

@Composable
fun ItemsScreen(
    eventId: Long,
    refreshTrigger: Boolean,
    onAddItemClick: (Long) -> Unit,
    onRecordSaleClick: (Long) -> Unit,
    onTransactionsClick: (Long) -> Unit,
    onExportCsvClick: (Long) -> Unit,
    onItemDeleted: () -> Unit
) {
    val viewModel: ItemsViewModel = viewModel()

    LaunchedEffect(eventId, refreshTrigger) {
        viewModel.loadItems(eventId)
    }

    val items = viewModel.items.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    val deleteViewModel: DeleteItemViewModel = viewModel()

    Scaffold(
        topBar = {
            SimpleTopBar("Items")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
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

            when {
                isLoading -> {
                    Text("Loading items...")
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                items.isEmpty() -> {
                    Text("No items found for this event yet. Use Add Item to create your first item.")
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items) { item ->
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
                                            deleteViewModel.deleteItem(eventId, item.itemId) {
                                                onItemDeleted()
                                            }
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
}