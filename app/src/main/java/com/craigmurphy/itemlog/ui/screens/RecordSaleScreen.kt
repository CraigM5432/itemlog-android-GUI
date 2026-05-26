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
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

// Screen used to record merchandise sales for the selected event.
// Users select an item, enter quantity sold and optionally adjust sale price.
@OptIn(ExperimentalMaterial3Api::class)
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

    var selectedPaymentMethod by remember { mutableStateOf("CASH") }
    var paymentDropdownExpanded by remember { mutableStateOf(false) }

    val paymentMethods = listOf("CASH", "CARD", "REVOLUT")
    val keyboardController = LocalSoftwareKeyboardController.current

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
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {
                        OutlinedTextField(
                            value = selectedItem?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Choose Item") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {
                            items.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Text("${item.name} - €${item.price} (Stock: ${item.quantity})")
                                    },
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

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = paymentDropdownExpanded,
                onExpandedChange = {
                    paymentDropdownExpanded = !paymentDropdownExpanded
                }
            ) {
                OutlinedTextField(
                    value = selectedPaymentMethod,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Payment Method") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = paymentDropdownExpanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = paymentDropdownExpanded,
                    onDismissRequest = {
                        paymentDropdownExpanded = false
                    }
                ) {
                    paymentMethods.forEach { method ->
                        DropdownMenuItem(
                            text = { Text(method) },
                            onClick = {
                                selectedPaymentMethod = method
                                paymentDropdownExpanded = false
                            }
                        )
                    }
                }
            }

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
                    keyboardController?.hide()
                    viewModel.createTransaction(
                        eventId = eventId,
                        selectedItemId = selectedItem?.itemId,
                        quantitySold = quantitySold,
                        salePrice = salePrice,
                        paymentMethod = selectedPaymentMethod
                    ) {
                        onSaveClick()
                    }
                },
                modifier = Modifier.fillMaxWidth(),

                // Button is disabled if there are no items to sell.
                enabled = items.isNotEmpty() && !isSaving
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