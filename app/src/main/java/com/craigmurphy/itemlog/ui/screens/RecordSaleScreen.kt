package com.craigmurphy.itemlog.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.craigmurphy.itemlog.data.model.ItemResponse
import com.craigmurphy.itemlog.ui.components.ScreenHeader
import com.craigmurphy.itemlog.ui.components.SimpleTopBar
import com.craigmurphy.itemlog.viewmodel.RecordSaleViewModel

// Screen used to record merchandise sales for the selected event.
// Users select an item, enter quantity sold and optionally adjust sale price.
@Composable
fun RecordSaleScreen(
    eventId: Long,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    // Currently selected item from the dropdown.
    var selectedItem by remember { mutableStateOf<ItemResponse?>(null) }

    // Quantity sold entered by user.
    var quantitySold by remember { mutableStateOf("") }

    // Sale price entered by user.
    var salePrice by remember { mutableStateOf("") }

    // Controls whether the dropdown menu is open.
    var expanded by remember { mutableStateOf(false) }

    // ViewModel handles item loading and transaction creation.
    val viewModel: RecordSaleViewModel = viewModel()

    // Loads event items when the screen opens.
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
                    // Read-only field showing the selected item name.
                    OutlinedTextField(
                        value = selectedItem?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Selected Item") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Opens the item dropdown.
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Choose Item")
                    }

                    // Dropdown menu listing event items.
                    // Dropdown selection replaced manual item ID entry to improve usability.
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        items.forEach { item ->
                            DropdownMenuItem(
                                text = { Text("${item.name} (Stock: ${item.quantity})") },
                                onClick = {
                                    selectedItem = item

                                    // Defaults sale price to the item's current price.
                                    salePrice = item.price.toString()

                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quantity sold input.
            OutlinedTextField(
                value = quantitySold,
                onValueChange = { quantitySold = it },
                label = { Text("Quantity Sold") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Sale price input.
            OutlinedTextField(
                value = salePrice,
                onValueChange = { salePrice = it },
                label = { Text("Sale Price") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            // Displays validation/API errors.
            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Saves the sale transaction.
            // Backend also validates stock quantity to prevent overselling.
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

                // Button is disabled if there are no items to sell.
                enabled = items.isNotEmpty()
            ) {
                Text(if (isSaving) "Saving..." else "Save Sale")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cancels and returns to previous screen.
            TextButton(onClick = onCancelClick) {
                Text("Cancel")
            }
        }
    }
}