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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar

data class TransactionUiModel(
    val id: Int,
    val itemName: String,
    val quantitySold: Int,
    val salePrice: Double,
    val saleTime: String
)

@Composable
fun TransactionsScreen() {
    val transactions = listOf(
        TransactionUiModel(1, "T-Shirt", 2, 40.0, "2026-03-23 11:00"),
        TransactionUiModel(2, "Hoodie", 1, 35.0, "2026-03-23 12:30"),
        TransactionUiModel(3, "Cap", 3, 45.0, "2026-03-23 13:15")
    )

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
            ScreenHeader("Sales History")

            Spacer(modifier = Modifier.height(16.dp))

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
                                text = transaction.itemName,
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