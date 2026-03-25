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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar

data class ItemUiModel(
    val id: Int,
    val name: String,
    val price: Double,
    val quantity: Int
)

@Composable
fun ItemsScreen(
    onAddItemClick: () -> Unit,
    onRecordSaleClick: () -> Unit,
    onTransactionsClick: () -> Unit
) {
    val items = listOf(
        ItemUiModel(1, "T-Shirt", 20.0, 15),
        ItemUiModel(2, "Hoodie", 35.0, 8),
        ItemUiModel(3, "Cap", 15.0, 20)
    )

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
                    onClick = onAddItemClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Add Item")
                }

                Button(
                    onClick = onRecordSaleClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Record Sale")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onTransactionsClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Transactions")
            }

            Spacer(modifier = Modifier.height(16.dp))

            ScreenHeader("Event Items")

            Spacer(modifier = Modifier.height(12.dp))

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
                        }
                    }
                }
            }
        }
    }
}