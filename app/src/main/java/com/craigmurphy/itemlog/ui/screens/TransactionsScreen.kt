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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar
import com.craigmurphy.itemlog.viewmodel.TransactionsViewModel
import com.craigmurphy.itemlog.ui.components.EventSummaryCard

@Composable
fun TransactionsScreen(
    eventId: Long
) {
    val viewModel: TransactionsViewModel = viewModel()

    LaunchedEffect(eventId) {
        viewModel.loadTransactions(eventId)
    }

    val transactions = viewModel.transactions.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    val event = viewModel.event.value

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
            ScreenHeader("Transactions history")

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    Text("Loading transactions...")
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                transactions.isEmpty() -> {
                    Text("No transactions recorded for this event yet.")
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(transactions) { transaction ->
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "Transaction #${transaction.transactionId}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "Quantity Sold: ${transaction.quantitySold}")
                                    Text(text = "Sale Price: €${transaction.salePrice}")
                                    Text(text = "Time: ${transaction.saleTime}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}