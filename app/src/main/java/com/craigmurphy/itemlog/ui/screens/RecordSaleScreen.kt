package com.craigmurphy.itemlog.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.craigmurphy.itemlog.data.model.ItemResponse
import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar
import com.craigmurphy.itemlog.viewmodel.RecordSaleViewModel

@Composable
fun RecordSaleScreen(
    eventId: Long,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    var selectedItem by remember { mutableStateOf<ItemResponse?>(null) }
    var quantitySold by remember { mutableStateOf("") }
    var salePrice by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val viewModel: RecordSaleViewModel = viewModel()

    LaunchedEffect(eventId) {
        viewModel.loadItems(eventId)
    }

    val items = viewModel.items.value
    val isLoadingItems = viewModel.isLoadingItems.value
    val isSaving = viewModel.isSaving.value
    val errorMessage = viewModel.errorMessage.value

    Scaffold(
        topBar = {
            SimpleTopBar("Record Sale")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenHeader("Record an item sale")

            Spacer(modifier = Modifier.height(24.dp))

            when {
                isLoadingItems -> {
                    Text("Loading items...")
                }

                items.isEmpty() -> {
                    Text("No items available for this event.")
                }

                else -> {
                    OutlinedTextField(
                        value = selectedItem?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Selected Item") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Choose Item")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        items.forEach { item ->
                            DropdownMenuItem(
                                text = { Text("${item.name} (Stock: ${item.quantity})") },
                                onClick = {
                                    selectedItem = item
                                    salePrice = item.price.toString()
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = quantitySold,
                onValueChange = { quantitySold = it },
                label = { Text("Quantity Sold") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = salePrice,
                onValueChange = { salePrice = it },
                label = { Text("Sale Price") },
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.createTransaction(
                        eventId = eventId,
                        selectedItemId = selectedItem?.itemId,
                        quantitySold = quantitySold,
                        salePrice = salePrice
                    ) {
                        onSaveClick()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = items.isNotEmpty()
            ) {
                Text(if (isSaving) "Saving..." else "Save Sale")
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = onCancelClick) {
                Text("Cancel")
            }
        }
    }
}