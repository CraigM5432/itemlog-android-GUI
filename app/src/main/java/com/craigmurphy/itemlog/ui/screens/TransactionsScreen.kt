package com.craigmurphy.itemlog.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.craigmurphy.itemlog.ui.components.EmptyState
import com.craigmurphy.itemlog.ui.components.EventSummaryCard
import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar
import com.craigmurphy.itemlog.viewmodel.TransactionsViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TransactionsScreen(
    eventId: Long
) {
    val viewModel: TransactionsViewModel = viewModel()

    LaunchedEffect(eventId) {
        viewModel.loadTransactions(eventId)
    }

    val transactions = viewModel.transactions.value

    var searchQuery by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    val filteredTransactions = transactions.filter { transaction ->
        transaction.itemName.contains(searchQuery, ignoreCase = true) ||
                transaction.paymentMethod.contains(searchQuery, ignoreCase = true)
    }

    val totalTransactions = filteredTransactions.size

    val totalRevenue = filteredTransactions.fold(0.0) { total, transaction ->
        total + transaction.totalAmount
    }

    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    val event = viewModel.event.value

    val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")

    Scaffold(
        topBar = {
            SimpleTopBar("Transactions")
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            event?.let {
                EventSummaryCard(
                    eventName = it.eventName,
                    eventDate = it.eventDate
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            ScreenHeader("Transactions")

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Transaction Summary",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Total Transactions: $totalTransactions")
                    Text(text = "Total Revenue: €%.2f".format(totalRevenue))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                label = {
                    Text("Search transactions")
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(
                            onClick = {
                                searchQuery = ""
                                keyboardController?.hide()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    Text("Loading transactions history...")
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                transactions.isEmpty() -> {
                    EmptyState(
                        title = "No transactions yet",
                        message = "Record a sale from the Items screen to see transaction history here.",
                        symbol = "🧾"
                    )
                }

                filteredTransactions.isEmpty() -> {
                    EmptyState(
                        title = "No matching transactions",
                        message = "Try searching by item name or payment method.",
                        symbol = "🔍"
                    )
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredTransactions) { transaction ->

                            val formattedTime = try {
                                LocalDateTime.parse(transaction.saleTime, inputFormatter)
                                    .format(outputFormatter)
                            } catch (e: Exception) {
                                transaction.saleTime
                            }

                            val formattedPaymentMethod =
                                transaction.paymentMethod.lowercase()
                                    .replaceFirstChar { it.uppercase() }

                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = transaction.itemName,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(text = "Quantity: ${transaction.quantitySold}")
                                    Text(text = "Sale Price: €${transaction.salePrice}")
                                    Text(text = "Total: €${transaction.totalAmount}")
                                    Text(text = "Payment: $formattedPaymentMethod")
                                    Text(text = "Time: $formattedTime")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}